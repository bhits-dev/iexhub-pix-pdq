package gov.samhsa.c2s.iexhubpixpdq.config;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Component
@Data
@ConfigurationProperties(prefix = "c2s.iexhub-pix-pdq")
public class IexhubPixPdqProperties {

    @NotBlank
    private String pixManagerServiceEndPoint;

    @NotBlank
    private String pixDomainId;

    @NotBlank
    private String pixDomainName;

    @NotBlank
    private String globalDomainId;

    @NotBlank
    private String globalDomainIdTypeCode;

    @NotNull
    @Valid
    private Fhir fhir;

    @Data
    public static class Fhir {
        @NotNull
        @Valid
        private CapabilityStatement capabilityStatement;

        public interface MediaType {
            String APPLICATION_FHIR_JSON_UTF8_VALUE = "application/fhir+json;charset=UTF-8";
        }

        @Data
        public static class CapabilityStatement {
            @NotBlank
            private String publisher;
            @NotNull
            @Valid
            private Software software;
            @NotNull
            @Valid
            private Implementation implementation;

            @Data
            public static class Software {
                @NotBlank
                private String name;
                @NotBlank
                private String version;
            }

            @Data
            public static class Implementation {
                @NotBlank
                private String description;
            }
        }
    }
}
