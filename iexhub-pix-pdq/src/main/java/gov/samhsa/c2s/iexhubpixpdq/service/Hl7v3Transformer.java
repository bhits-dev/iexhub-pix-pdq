package gov.samhsa.c2s.iexhubpixpdq.service;

import gov.samhsa.c2s.iexhubpixpdq.service.exception.Hl7v3TransformerException;

/**
 * The Interface Hl7v3Transformer.
 */
public interface Hl7v3Transformer {


    /**
     * Transform to hl7v3 pix xml.
     *
     * @param xml     the xml
     * @param XSLTURI the xslturi
     * @return the string
     * @throws Hl7v3TransformerException the hl7v3 transformer exception
     */
    public String transformToHl7v3PixXml(String xml, String XSLTURI);

    /**
     * Transform c32 to green ccd.
     *
     * @param mrn       the medical record no of patient
     * @param mrnDomain the eHRdomain id
     * @param xsltUri   the xsl for pixquery
     * @return the string
     * @throws Hl7v3TransformerException the hl7v3 transformer exception
     */
    public String getPixQueryXml(String mrn, String mrnDomain, String xsltUri)
            throws Hl7v3TransformerException;
}
