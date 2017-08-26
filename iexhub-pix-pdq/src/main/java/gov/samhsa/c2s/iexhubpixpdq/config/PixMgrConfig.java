package gov.samhsa.c2s.iexhubpixpdq.config;


import gov.samhsa.c2s.common.document.transformer.XmlTransformer;
import gov.samhsa.c2s.common.document.transformer.XmlTransformerImpl;
import gov.samhsa.c2s.common.marshaller.SimpleMarshaller;
import gov.samhsa.c2s.common.marshaller.SimpleMarshallerImpl;
import gov.samhsa.c2s.pixclient.service.PixManagerService;
import gov.samhsa.c2s.pixclient.service.PixManagerServiceImpl;
import gov.samhsa.c2s.pixclient.util.PixManagerMessageHelper;
import gov.samhsa.c2s.pixclient.util.PixManagerRequestXMLToJava;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PixMgrConfig {

    private final IexhubPixPdqProperties iexhubPixPdqProperties;

    public PixMgrConfig(IexhubPixPdqProperties iexhubPixPdqProperties) {
        this.iexhubPixPdqProperties = iexhubPixPdqProperties;
    }

    @Bean
    public PixManagerService pixManagerService() {
        return new PixManagerServiceImpl(iexhubPixPdqProperties.getPixManagerServiceEndPoint());
    }

    @Bean
    public SimpleMarshaller simpleMarshaller() {
        return new SimpleMarshallerImpl();
    }

    @Bean
    public XmlTransformer xmlTransformer(){
        return new XmlTransformerImpl(simpleMarshaller());
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
