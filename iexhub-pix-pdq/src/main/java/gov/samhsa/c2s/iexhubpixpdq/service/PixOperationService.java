package gov.samhsa.c2s.iexhubpixpdq.service;


import gov.samhsa.c2s.iexhubpixpdq.service.dto.FhirPatientDto;
import gov.samhsa.c2s.pixclient.client.PixManagerClientService;

public interface PixOperationService extends PixManagerClientService {

    String getPersonEid(String reqXMLPath);
    String registerPerson(FhirPatientDto fhirPatientDto);
    String editPerson(String id,FhirPatientDto fhirPatientDto);
}

