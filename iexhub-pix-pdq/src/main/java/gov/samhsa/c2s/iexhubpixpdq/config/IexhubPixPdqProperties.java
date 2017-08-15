package gov.samhsa.c2s.iexhubpixpdq.config;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "c2s.iexhub-pix-pdq")
public class IexhubPixPdqProperties {

    @NotEmpty
    private String pixManagerServiceEndPoint;

    @NotEmpty
    private String pixDomainId;

    @NotEmpty
    private String pixDomainName;

    @NotEmpty
    private String globalDomainId;


}
