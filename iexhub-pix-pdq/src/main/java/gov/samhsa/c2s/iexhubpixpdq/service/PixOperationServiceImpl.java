package gov.samhsa.c2s.iexhubpixpdq.service;

import gov.samhsa.c2s.common.marshaller.SimpleMarshaller;
import gov.samhsa.c2s.common.marshaller.SimpleMarshallerException;
import gov.samhsa.c2s.iexhubpixpdq.config.IexhubPixPdqProperties;
import gov.samhsa.c2s.iexhubpixpdq.exception.PixOperationException;
import gov.samhsa.c2s.iexhubpixpdq.service.dto.FhirPatientDto;
import gov.samhsa.c2s.iexhubpixpdq.service.dto.PixPatientDto;
import gov.samhsa.c2s.pixclient.service.PixManagerService;
import gov.samhsa.c2s.pixclient.util.PixManagerBean;
import gov.samhsa.c2s.pixclient.util.PixManagerMessageHelper;
import gov.samhsa.c2s.pixclient.util.PixManagerRequestXMLToJava;
import gov.samhsa.c2s.pixclient.util.PixPdqConstants;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201302UV02;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201310UV02;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PixOperationServiceImpl implements PixOperationService {
    private final PixManagerRequestXMLToJava requestXMLToJava;
    private final PixManagerService pixMgrService;
    private final PixManagerMessageHelper pixManagerMessageHelper;
    private final IexhubPixPdqProperties iexhubPixPdqProperties;
    private final Hl7v3Transformer hl7v3Transformer;
    private final SimpleMarshaller simpleMarshaller;

    @Autowired
    public PixOperationServiceImpl(PixManagerRequestXMLToJava requestXMLToJava, PixManagerService pixMgrService, PixManagerMessageHelper pixManagerMessageHelper, IexhubPixPdqProperties iexhubPixPdqProperties,
                                   Hl7v3Transformer hl7v3Transformer, SimpleMarshaller simpleMarshaller) {
        this.requestXMLToJava = requestXMLToJava;
        this.pixMgrService = pixMgrService;
        this.pixManagerMessageHelper = pixManagerMessageHelper;
        this.iexhubPixPdqProperties = iexhubPixPdqProperties;
        this.hl7v3Transformer = hl7v3Transformer;
        this.simpleMarshaller = simpleMarshaller;
    }

    @Override
    public String addPerson(String reqXMLPath) {
        final PixManagerBean pixMgrBean = new PixManagerBean();

        // Convert c32 to pixadd string

        PRPAIN201301UV02 request;

        MCCIIN000002UV01 response;
        // Delegate to webServiceTemplate for the actual pixadd
        try {
            request = requestXMLToJava.getPIXAddReqObject(reqXMLPath);
            response = pixMgrService.pixManagerPRPAIN201301UV02(request);
            pixManagerMessageHelper.getAddUpdateMessage(response, pixMgrBean,
                    PixPdqConstants.PIX_ADD.getMsg());
        } catch (JAXBException | IOException e) {
            pixManagerMessageHelper.getGeneralExpMessage(e, pixMgrBean,
                    PixPdqConstants.PIX_ADD.getMsg());
            log.error(e.getMessage() + e);
        }
        log.debug("response" + pixMgrBean.getAddMessage());
        return pixMgrBean.getAddMessage();
    }

    @Override
    public String updatePerson(String reqXMLPath) {
        final PixManagerBean pixMgrBean = new PixManagerBean();

        log.debug("Received request to PIXUpdate");

        PRPAIN201302UV02 request;

        MCCIIN000002UV01 response;
        // Delegate to webServiceTemplate for the actual pixadd
        try {

            request = requestXMLToJava.getPIXUpdateReqObject(reqXMLPath);

            response = pixMgrService.pixManagerPRPAIN201302UV02(request);
            pixManagerMessageHelper.getAddUpdateMessage(response, pixMgrBean,
                    PixPdqConstants.PIX_UPDATE.getMsg());
        } catch (JAXBException | IOException e) {
            pixManagerMessageHelper.getGeneralExpMessage(e, pixMgrBean,
                    PixPdqConstants.PIX_UPDATE.getMsg());
            log.error(e.getMessage());
        }
        log.debug("response" + pixMgrBean.getAddMessage());
        return pixMgrBean.getUpdateMessage();
    }

    @Override
    public PixManagerBean queryPerson(String reqXMLPath) {
        final PixManagerBean pixMgrBean = new PixManagerBean();

        log.debug("Received request to PIXQuery");

        PRPAIN201309UV02 request;

        PRPAIN201310UV02 response;
        // Delegate to webServiceTemplate for the actual pixadd
        try {

            request = requestXMLToJava.getPIXQueryReqObject(reqXMLPath);

            response = pixMgrService.pixManagerPRPAIN201309UV02(request);
            pixManagerMessageHelper.getQueryMessage(response, pixMgrBean);
        } catch (JAXBException | IOException e) {
            pixManagerMessageHelper.getGeneralExpMessage(e, pixMgrBean,
                    PixPdqConstants.PIX_QUERY.getMsg());
            log.error(e.getMessage());
        }
        log.debug("response" + pixMgrBean.getQueryMessage() + pixMgrBean.getQueryIdMap());
        return pixMgrBean;
    }

    @Override
    public String getPersonEid(String reqXMLPath) {
        final PixManagerBean pixMgrBean = queryPerson(reqXMLPath);
        String eid = pixMgrBean.getQueryIdMap().entrySet().stream()
                .filter(map -> iexhubPixPdqProperties.getGlobalDomainId().equals(map.getKey()))
                .map(map -> map.getValue())
                .collect(Collectors.joining());
        log.info("Eid \t" + eid);
        return eid;
    }

    @Override
    public String registerPerson(FhirPatientDto fhirPatientDto) {
        // Convert FHIR Patient to PatientDto
        PixPatientDto pixPatientDto = fhirPatientDtoToPixPatientDto(fhirPatientDto);
        PixManagerBean pixMgrBean = init(pixPatientDto);
        // Translate PatientDto to PixAddRequest XML
        String pixAddXml = buildFhirPatient2PixAddXml(pixPatientDto);
        // Invoke addPerson method that register patient to openempi
        pixMgrBean.setAddMessage(addPerson(pixAddXml));
        Assert.hasText(
                pixMgrBean.getAddMessage(),
                "Add Success!");
  /*      eid = pixService.getEid(mrn);
        Assert.hasText(eid, "EID cannot be retrieved from MPI!");    */
        return pixMgrBean.getAddMessage();
    }

    @Override
    public String editPerson(String id,FhirPatientDto fhirPatientDto){
        //Convert FHIR patient to PatientDto
        PixPatientDto pixPatientDto=fhirPatientDtoToPixPatientDto(fhirPatientDto);
        PixManagerBean pixMgrBean=init(pixPatientDto);
        //Translate PatientDto to Pix
        String pixUpdateXml=buildFhirPatient2PixUpdateXml(pixPatientDto);
        //Invoke updatePerson method
        pixMgrBean.setUpdateMessage(updatePerson(pixUpdateXml));
        Assert.hasText(pixMgrBean.getUpdateMessage(),
                "Update Success");

        return pixMgrBean.getUpdateMessage();
    }

    private String buildFhirPatient2PixAddXml(PixPatientDto pixPatientDto) {

        String hl7PixAddXml = null;
        try {
            hl7PixAddXml = hl7v3Transformer.transformToHl7v3PixXml(
                    simpleMarshaller.marshal(pixPatientDto),
                    XslResource.XSLT_FHIR_PATIENT_DTO_TO_PIX_ADD.getFileName());
        } catch (SimpleMarshallerException e) {
            log.error("Error in JAXB Transfroming", e);
            throw new PixOperationException(e);
        }
        return hl7PixAddXml;

    }

    private String buildFhirPatient2PixUpdateXml(PixPatientDto pixPatientDto){
        String h17PixUpdateXml=null;
        try{
            h17PixUpdateXml=hl7v3Transformer.transformToHl7v3PixXml(simpleMarshaller.marshal(pixPatientDto),
                    XslResource.XSLT_FHIR_PATIENT_DTO_TO_PIX_UPDATE.getFileName());
        }catch(SimpleMarshallerException e){
            log.error("Error in JAXB Transforming",e);
            throw new PixOperationException(e);
        }
        return h17PixUpdateXml;
    }

    private PixManagerBean init(PixPatientDto pixPatientDto) {
        pixPatientDto.setIdRoot(iexhubPixPdqProperties.getPixDomainId());
        pixPatientDto.setIdAssigningAuthorityName(iexhubPixPdqProperties.getPixDomainName());
        return new PixManagerBean();
    }

    private PixPatientDto fhirPatientDtoToPixPatientDto(FhirPatientDto fhirPatientDto) {
        PixPatientDto pixPatientDto = new PixPatientDto();
        pixPatientDto.setBirthTimeValue(getBirthDate(fhirPatientDto.getPatient().getBirthDate()));
        pixPatientDto.setPatientFirstName(fhirPatientDto.getPatient().getNameFirstRep().getGivenAsSingleString());
        pixPatientDto.setPatientLastName(fhirPatientDto.getPatient().getNameFirstRep().getFamily());
        pixPatientDto.setIdExtension(fhirPatientDto.getPatient().getIdentifier().get(0).getValue());
        pixPatientDto.setAdministrativeGenderCode(getAdminGenderCode(fhirPatientDto.getPatient().getGender().name()));
        //TODO :: Need to set email and telephone no seperately
        pixPatientDto.setTelecomValue(
                fhirPatientDto.getPatient().getTelecom().stream()
                        .map(value -> value.getValue()).findFirst().orElse(""));

        //TODO:: Set Address Values

        return pixPatientDto;
    }

    private String getAdminGenderCode(String genderName) {
        String genderCode="U";
        if (genderName.equalsIgnoreCase(Enumerations.AdministrativeGender.MALE.name())) {
            genderCode = "M";
        } else if (genderName.equalsIgnoreCase(Enumerations.AdministrativeGender.FEMALE.name())) {
            genderCode = "F";
        } else if (genderName.equalsIgnoreCase(Enumerations.AdministrativeGender.OTHER.name())) {
            genderCode = "O";
        } else if (genderName.equalsIgnoreCase(Enumerations.AdministrativeGender.UNKNOWN.name())) {
            genderCode = "U";
        }
        return genderCode;
    }

    private String getBirthDate(Date utilDate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        StringBuffer sb = new StringBuffer();
        return simpleDateFormat.format(utilDate);
    }
}
