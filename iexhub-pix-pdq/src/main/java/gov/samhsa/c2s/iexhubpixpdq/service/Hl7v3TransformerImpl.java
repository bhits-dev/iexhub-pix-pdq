package gov.samhsa.c2s.iexhubpixpdq.service;

import gov.samhsa.c2s.common.document.transformer.XmlTransformer;
import gov.samhsa.c2s.iexhubpixpdq.exception.Hl7v3TransformerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Optional;

/**
 * The Class Hl7v3TransformerImpl.
 */
@Service
@Slf4j
public class Hl7v3TransformerImpl implements Hl7v3Transformer {

    private final XmlTransformer xmlTransformer;

    private final ResourceLoader resourceLoader;

    public Hl7v3TransformerImpl(XmlTransformer xmlTransformer, ResourceLoader resourceLoader) {
        this.xmlTransformer = xmlTransformer;
        this.resourceLoader = resourceLoader;
    }


    @Override
    public String getPixQueryXml(String mrn, String mrnDomain, String xsltUri) throws Hl7v3TransformerException{
        String newxsltUri = "";
        try {
            final String extension = "@extension";
            final String root = "@root";
            String queryXML = xsltUri;
            if (null != xsltUri && !xsltUri.startsWith("<?xml")) {
                // read the xsl file from resources folder
                final InputStream styleIs = Thread.currentThread()
                        .getContextClassLoader().getResourceAsStream(xsltUri);
                queryXML = IOUtils.toString(styleIs, "UTF-8");
            }
            newxsltUri = queryXML.replaceAll(extension, mrn);
            newxsltUri = newxsltUri.replaceAll(root, mrnDomain);
        } catch (final Exception e) {
            final String errorMessage = "Error happended when trying to mrn data to hl7v3PixQuery";
            log.error(errorMessage, e);
            final Hl7v3TransformerException transformerException = new Hl7v3TransformerException(
                    errorMessage, e);
            throw transformerException;
        }
        return newxsltUri;
    }

    @Override
    public String transformToHl7v3PixXml(String xml, String XSLTURI) {

        final String xslUrl = Thread.currentThread()
                .getContextClassLoader().getResource(XSLTURI).toString();
        final String hl7v3PixXML = xmlTransformer.transform(xml, xslUrl,
                Optional.empty(), Optional.empty());
        log.debug("hl7v3PixXML:");
        log.debug(hl7v3PixXML);
        return hl7v3PixXML;

    }
}
