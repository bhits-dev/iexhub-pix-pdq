package gov.samhsa.c2s.iexhubpixpdq.web;

import gov.samhsa.c2s.iexhubpixpdq.service.PixOperationService;
import gov.samhsa.c2s.iexhubpixpdq.service.dto.FhirPatientDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RestController
@Slf4j
public class PixOperationController {


    private final PixOperationService pixOperationService;

    public PixOperationController(PixOperationService pixOperationService) {
        this.pixOperationService = pixOperationService;
    }

    @PostMapping("/eid")
    @ResponseStatus(HttpStatus.OK)
    public String getPersonEid(@RequestBody String reqXMLPath) {
        return pixOperationService.getPersonEid(reqXMLPath);
    }

    @RequestMapping(value="/Patient", consumes= MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void addPerson(@RequestBody FhirPatientDto fhirPatientDto) {
         pixOperationService.registerPerson(fhirPatientDto);

    }

    private String getRequest(String reqXml) {
        String sampleReq = null;
        try (InputStream ioStream = ClassLoader.getSystemResourceAsStream(reqXml)) {
            sampleReq = IOUtils.toString(ioStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage() + e);
        }
        return sampleReq;
    }
}
