package gov.samhsa.c2s.iexhubpixpdq.service;

import gov.samhsa.c2s.common.url.ResourceUrlProvider;

public interface PixXslUrlProvider extends ResourceUrlProvider {
    public abstract String getUrl(XslResource xslResource);
}
