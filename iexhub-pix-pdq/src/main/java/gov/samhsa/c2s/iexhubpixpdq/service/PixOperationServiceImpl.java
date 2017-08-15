package gov.samhsa.c2s.iexhubpixpdq.service;

import gov.samhsa.c2s.iexhubpixpdq.config.IexhubPixPdqProperties;
import gov.samhsa.c2s.pixclient.client.PixManagerClientService;
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
import java.util.stream.Collectors;

@Service
@Slf4j
public class PixOperationServiceImpl implements PixOperationService {
    /**
     * The request xml to java.
     */
    @Autowired
    private PixManagerRequestXMLToJava requestXMLToJava;

    /**
     * The pix mgr service.
     */
    @Autowired
    private PixManagerService pixMgrService;

    /**
     * The pix manager message helper.
     */
    @Autowired
    private PixManagerMessageHelper pixManagerMessageHelper;

    @Autowired
    private IexhubPixPdqProperties iexhubPixPdqProperties;


    @Override
    public String addPerson(String reqXMLPath) {
        final PixManagerBean pixMgrBean = new PixManagerBean();

        // Convert c32 to pixadd string

        PRPAIN201301UV02 request = new PRPAIN201301UV02();

        MCCIIN000002UV01 response = new MCCIIN000002UV01();
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

        PRPAIN201302UV02 request = new PRPAIN201302UV02();

        MCCIIN000002UV01 response = new MCCIIN000002UV01();
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

        PRPAIN201309UV02 request = new PRPAIN201309UV02();

        PRPAIN201310UV02 response = new PRPAIN201310UV02();
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
        String eid = pixMgrBean.getQueryIdMap().entrySet().stream()
                .filter(map -> iexhubPixPdqProperties.pixDomainId.equals(map.getKey()))
                .map(map -> map.getValue())
                .collect(Collectors.joining());
        log.info("Eid \t" + eid);
        return pixMgrBean;
    }
}
