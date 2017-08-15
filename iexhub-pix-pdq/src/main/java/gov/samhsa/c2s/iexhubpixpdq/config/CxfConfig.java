package gov.samhsa.c2s.iexhubpixpdq.config;


import gov.samhsa.c2s.common.marshaller.SimpleMarshaller;
import gov.samhsa.c2s.common.marshaller.SimpleMarshallerImpl;
import gov.samhsa.c2s.pixclient.service.PixManagerService;
import gov.samhsa.c2s.pixclient.service.PixManagerServiceImpl;
import gov.samhsa.c2s.pixclient.util.PixManagerMessageHelper;
import gov.samhsa.c2s.pixclient.util.PixManagerRequestXMLToJava;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CxfConfig {

    @Autowired
    private IexhubPixPdqProperties iexhubPixPdqProperties;

    @Bean
    public PixManagerService pixManagerService() {
        return new PixManagerServiceImpl(iexhubPixPdqProperties.getPixManagerServiceEndPoint());
    }

    @Bean
    public SimpleMarshaller simpleMarshaller() {
        return new SimpleMarshallerImpl();
    }

    @Bean
    public PixManagerRequestXMLToJava pixManagerRequestXMLToJava() {
        return new PixManagerRequestXMLToJava(simpleMarshaller());
    }

    @Bean
    public PixManagerMessageHelper pixManagerMessageHelper() {
        return new PixManagerMessageHelper();
    }

}
