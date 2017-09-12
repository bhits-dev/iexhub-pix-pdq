package gov.samhsa.c2s.iexhubpixpdq.service;

import gov.samhsa.c2s.common.marshaller.SimpleMarshaller;
import gov.samhsa.c2s.common.marshaller.SimpleMarshallerException;
import gov.samhsa.c2s.iexhubpixpdq.config.IexhubPixPdqProperties;
import gov.samhsa.c2s.iexhubpixpdq.service.dto.FhirPatientDto;
import gov.samhsa.c2s.iexhubpixpdq.service.dto.PixPatientDto;
import gov.samhsa.c2s.iexhubpixpdq.service.exception.PatientNotFoundException;
import gov.samhsa.c2s.iexhubpixpdq.service.exception.PixOperationException;
import gov.samhsa.c2s.pixclient.service.PixManagerService;
import gov.samhsa.c2s.pixclient.util.PixManagerBean;
import gov.samhsa.c2s.pixclient.util.PixManagerMessageHelper;
import gov.samhsa.c2s.pixclient.util.PixManagerRequestXMLToJava;
import gov.samhsa.c2s.pixclient.util.PixPdqConstants;
import lombok.extern.slf4j.Slf4j;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201302UV02;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201310UV02;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Map;

import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertNotNull;

@Service
@Slf4j
public class PixOperationServiceImpl implements PixOperationService {
    private final PixManagerRequestXMLToJava requestXMLToJava;
    private final PixManagerService pixMgrService;
    private final PixManagerMessageHelper pixManagerMessageHelper;
    private final IexhubPixPdqProperties iexhubPixPdqProperties;
    private final Hl7v3Transformer hl7v3Transformer;
    private final SimpleMarshaller simpleMarshaller;
    private final PixPatientDtoConverter pixPatientDtoConverter;
    private String SAMPLE_QUERY_REQUEST_XML = "empi_pixquery_sample.xml";

    @Autowired
    public PixOperationServiceImpl(PixManagerRequestXMLToJava requestXMLToJava, PixManagerService pixMgrService, PixManagerMessageHelper pixManagerMessageHelper, IexhubPixPdqProperties iexhubPixPdqProperties,
                                   Hl7v3Transformer hl7v3Transformer, SimpleMarshaller simpleMarshaller, PixPatientDtoConverter pixPatientDtoConverter) {
        this.requestXMLToJava = requestXMLToJava;
        this.pixMgrService = pixMgrService;
        this.pixManagerMessageHelper = pixManagerMessageHelper;
        this.iexhubPixPdqProperties = iexhubPixPdqProperties;
        this.hl7v3Transformer = hl7v3Transformer;
        this.simpleMarshaller = simpleMarshaller;
        this.pixPatientDtoConverter = pixPatientDtoConverter;
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
        log.debug("response" + pixMgrBean.getUpdateMessage());
        return pixMgrBean.getUpdateMessage();
    }

    @Override
    public String queryForEnterpriseId(String patientId, String patientMrnOid) {
        final PixManagerBean pixMgrBean = new PixManagerBean();
        try {
            //First, get sample request object
            PRPAIN201309UV02 request = requestXMLToJava.getPIXQueryReqObject(SAMPLE_QUERY_REQUEST_XML);

            //Next, change the sample request data to include the right query params
            org.hl7.v3.II patientIdentifierValue = request.getControlActProcess().getQueryByParameter().getValue().getParameterList().getPatientIdentifier().get(0).getValue().get(0);
            patientIdentifierValue.setRoot(patientMrnOid);
            patientIdentifierValue.setExtension(patientId);

            //Query
            PRPAIN201310UV02 response = pixMgrService.pixManagerPRPAIN201309UV02(request);
            pixManagerMessageHelper.setQueryMessage(response, pixMgrBean);

            String globalDomainId = iexhubPixPdqProperties.getGlobalDomainId();
            if (pixMgrBean.isSuccess()) {
                String enterpriseIdValue = pixMgrBean.getQueryIdMap().entrySet().stream()
                        .filter(map -> globalDomainId.equals(map.getKey()))
                        .map(Map.Entry::getValue)
                        .collect(joining());

                log.debug("Found EnterpriseIdValue = " + enterpriseIdValue);

                if (enterpriseIdValue != null && !enterpriseIdValue.isEmpty()) {
                    //Convert to this format: d3bb3930-7241-11e3-b4f7-00155d3a2124^^^&2.16.840.1.113883.4.357&ISO
                    String enterpriseId = enterpriseIdValue + "^^^&" + globalDomainId + "&" + iexhubPixPdqProperties.getGlobalDomainIdTypeCode();
                    log.info("Found EnterpriseId = " + enterpriseId);
                    return enterpriseId;
                } else {
                    log.error("Pix Query was successful, but no matching value found that matches with identifier " + globalDomainId);
                    throw new PatientNotFoundException("No patient identifier found that matches with the Identifier Domain value: " + globalDomainId);
                }

            } else {
                log.error("Pix Query found no matching Patient:" + patientId + " Oid:" + patientMrnOid);
                throw new PatientNotFoundException("Pix Query found no matching Patient. Query Message = " + pixMgrBean.getQueryMessage());
            }

        } catch (JAXBException | IOException e) {
            log.error("Error when converting QUERY_REQUEST_XML to PRPAIN201301UV02 request object", e);
            throw new PixOperationException("Error when converting QUERY_REQUEST_XML to PRPAIN201301UV02 request object", e);
        }
    }

    @Override
    public String registerPerson(FhirPatientDto fhirPatientDto) {

        // Convert FHIR Patient to PatientDto
        PixPatientDto pixPatientDto = pixPatientDtoConverter.fhirPatientDtoToPixPatientDto(fhirPatientDto);
        // Translate PatientDto to PixAddRequest XML
        String pixAddXml = buildFhirPatient2PixAddXml(pixPatientDto);
        // Invoke addPerson method that register patient to openempi
        String addMessage = addPerson(pixAddXml);
        log.debug("server response " + addMessage);
        assertNotNull(addMessage);
        return addMessage;
    }

    @Override
    public String editPerson(String patientId, FhirPatientDto fhirPatientDto) {
        // TODO:: Assert patienid

        //Convert FHIR patient to PatientDto
        PixPatientDto pixPatientDto = pixPatientDtoConverter.fhirPatientDtoToPixPatientDto(fhirPatientDto);
        //Translate PatientDto to Pix
        String pixUpdateXml = buildFhirPatient2PixUpdateXml(pixPatientDto);
        //Invoke updatePerson method
        String updateMessage = updatePerson(pixUpdateXml);
        log.debug("server response " + updateMessage);
        assertNotNull(updateMessage);

        return updateMessage;
    }

    private String buildFhirPatient2PixAddXml(PixPatientDto pixPatientDto) {

        String hl7PixAddXml;
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

    private String buildFhirPatient2PixUpdateXml(PixPatientDto pixPatientDto) {
        String h17PixUpdateXml;
        try {
            h17PixUpdateXml = hl7v3Transformer.transformToHl7v3PixXml(simpleMarshaller.marshal(pixPatientDto),
                    XslResource.XSLT_FHIR_PATIENT_DTO_TO_PIX_UPDATE.getFileName());
        } catch (SimpleMarshallerException e) {
            log.error("Error in JAXB Transforming", e);
            throw new PixOperationException(e);
        }
        return h17PixUpdateXml;
    }


}
