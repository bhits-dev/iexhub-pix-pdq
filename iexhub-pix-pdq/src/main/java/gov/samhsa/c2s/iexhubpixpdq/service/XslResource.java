package gov.samhsa.c2s.iexhubpixpdq.service;

public enum XslResource {
    XSLT_FHIR_PATIENT_DTO_TO_PIX_ADD("xsl/patientDtoHl7v3PixAdd.xsl"),
    XSLT_FHIR_PATIENT_DTO_TO_PIX_UPDATE("xsl/patientDtoHl7v3PixUpdate.xsl"),
    XSLT_FHIR_PATIENT_DTO_TO_PIX_QUERY("xsl/patientDtoHl7v3PixQuery.xsl");

    private String fileName;

    XslResource(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }
}
