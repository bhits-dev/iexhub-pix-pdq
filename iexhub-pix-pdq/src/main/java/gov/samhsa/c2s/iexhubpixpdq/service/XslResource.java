package gov.samhsa.c2s.iexhubpixpdq.service;

public enum XslResource {
    XSLT_PATIENT_DTO_TO_PIX_ADD("patientDtoHl7v3PixAdd.xsl"),
    XSLT_PATIENT_DTO_TO_PIX_UPDATE("patientDtoHl7v3PixUpdate.xsl"),
    XSLT_PATIENT_DTO_TO_PIX_QUERY("patientDtoHl7v3PixQuery.xsl");

    private String fileName;

    XslResource(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }
}
