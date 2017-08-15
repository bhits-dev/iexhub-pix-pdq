package gov.samhsa.c2s.iexhubpixpdq.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "c2s.iexhub-pix-pdq")
@Slf4j
public class IexhubPixPdqProperties {

    @NotEmpty
    public String pixManagerServiceEndPoint;

    @NotEmpty
    public String pixDomainId;

    @NotEmpty
    public String pixDomainName;


}
