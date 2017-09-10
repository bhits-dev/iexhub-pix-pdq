package gov.samhsa.c2s.iexhubpixpdq.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PixPatientDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class PixPatientDto {
	private String messageId;
	private String idExtension;
	private String idRoot;
	private String idAssigningAuthorityName;
	private String ssn;
	private String patientFirstName;
	private String patientMiddleName;
	private String patientLastName;
	private String patientEmailHome;
	private String telecomUse;
	private String telecomValue;
	private String emailValue;
	private String administrativeGenderCode;
	private String birthTimeValue;
	private String addrStreetAddressLine1;
	private String addrStreetAddressLine2;
	private String addrCity;
	private String addrState;
	private String addrPostalCode;
	private String addCountry;
	private String maritalStatusCode;
}
