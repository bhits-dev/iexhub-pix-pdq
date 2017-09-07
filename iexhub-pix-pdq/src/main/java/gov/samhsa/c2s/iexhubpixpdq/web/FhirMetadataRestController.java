package gov.samhsa.c2s.iexhubpixpdq.web;

import gov.samhsa.c2s.iexhubpixpdq.config.IexhubPixPdqProperties;
import gov.samhsa.c2s.iexhubpixpdq.service.FhirMetadataService;
import gov.samhsa.c2s.iexhubpixpdq.service.dto.FhirMetadataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FhirMetadataRestController {
    @Autowired
    private FhirMetadataService fhirMetadataService;

    @GetMapping(value = "/metadata", produces = IexhubPixPdqProperties.Fhir.MediaType.APPLICATION_FHIR_JSON_UTF8_VALUE)
    public FhirMetadataDto getMetadata() {
        return fhirMetadataService.generateFhirMetadata();
    }
}
