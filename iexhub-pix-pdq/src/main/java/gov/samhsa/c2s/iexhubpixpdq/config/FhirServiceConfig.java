package gov.samhsa.c2s.iexhubpixpdq.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.validation.FhirValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FhirServiceConfig {


    private final IexhubPixPdqProperties iexhubPixPdqProperties;

    public FhirServiceConfig(IexhubPixPdqProperties iexhubPixPdqProperties) {
        this.iexhubPixPdqProperties = iexhubPixPdqProperties;
    }

    @Bean
    public FhirContext fhirContext() {
        FhirContext fhirContext = FhirContext.forDstu3();
        fhirContext.getRestfulClientFactory().setSocketTimeout(Integer.parseInt(iexhubPixPdqProperties.getFhir().getClientSocketTimeoutInMs()));
        return fhirContext;
    }

    @Bean
    public IGenericClient fhirClient() {
        // Create a client
        return fhirContext().newRestfulGenericClient(iexhubPixPdqProperties.getFhir().getServerUrl());
    }

    @Bean
    public IParser fhirXmlParser() {
        return fhirContext().newXmlParser();
    }

    @Bean
    public IParser fhirJsonParser() {
        return fhirContext().newJsonParser();
    }

    @Bean
    public FhirValidator fhirValidator() {
        return fhirContext().newValidator();
    }


}
