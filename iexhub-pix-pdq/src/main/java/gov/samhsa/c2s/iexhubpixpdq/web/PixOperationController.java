package gov.samhsa.c2s.iexhubpixpdq.web;

import gov.samhsa.c2s.iexhubpixpdq.config.IexhubPixPdqProperties;
import gov.samhsa.c2s.iexhubpixpdq.service.PixOperationService;
import gov.samhsa.c2s.iexhubpixpdq.service.dto.FhirPatientDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PixOperationController {

    @Autowired
    private PixOperationService pixOperationService;

    @GetMapping("/patients/{patientId}/mrn-oid/{patientMrnOid}/enterprise-id")
    @ResponseStatus(HttpStatus.OK)
    public String getPersonEid(@PathVariable String patientId,
                               @PathVariable String patientMrnOid) {
        return pixOperationService.queryForEnterpriseId(patientId, patientMrnOid);
    }

    @PostMapping(value = "/Patient", consumes = IexhubPixPdqProperties.Fhir.MediaType.APPLICATION_FHIR_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void addPerson(@RequestBody FhirPatientDto fhirPatientDto) {
        pixOperationService.registerPerson(fhirPatientDto);
    }

    @PutMapping(value = "/Patient/{patientId}", consumes = IexhubPixPdqProperties.Fhir.MediaType.APPLICATION_FHIR_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void updatePerson(@PathVariable String patientId, @RequestBody FhirPatientDto fhirPatientDto) {
        pixOperationService.editPerson(patientId, fhirPatientDto);
    }
}
