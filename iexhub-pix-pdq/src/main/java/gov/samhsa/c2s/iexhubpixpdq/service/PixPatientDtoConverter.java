package gov.samhsa.c2s.iexhubpixpdq.service;

import gov.samhsa.c2s.iexhubpixpdq.service.dto.FhirPatientDto;
import gov.samhsa.c2s.iexhubpixpdq.service.dto.PixPatientDto;
import org.springframework.util.StringUtils;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class PixPatientDtoConverter {

    /**
     * Signup dto to pix patient dto.
     *
     * @param fhirPatientDto
     * @return the pix patient dto
     */
    public static PixPatientDto signupDtoToPixPatientDto(FhirPatientDto fhirPatientDto) {
        PixPatientDto pixPatientDto = new PixPatientDto();
        pixPatientDto.setBirthTimeValue(fhirPatientDto.getPatient().getBirthDate().toString());
        pixPatientDto.setPatientFirstName(fhirPatientDto.getPatient().getNameFirstRep().getGivenAsSingleString());
        pixPatientDto.setPatientLastName(fhirPatientDto.getPatient().getNameFirstRep().getFamily());
        pixPatientDto.setIdExtension(fhirPatientDto.getPatient().getIdentifier().get(0).getValue());
        pixPatientDto.setAdministrativeGenderCode(fhirPatientDto.getPatient().getGender().name());
        pixPatientDto.setAddrCity(fhirPatientDto.getPatient().getAddress().get(0).getCity());
        pixPatientDto.setAddrPostalCode(fhirPatientDto.getPatient().getAddress().get(0).getPostalCode());
        pixPatientDto.setAddrState(fhirPatientDto.getPatient().getAddress().get(0).getState());
        return pixPatientDto;
    }

}
