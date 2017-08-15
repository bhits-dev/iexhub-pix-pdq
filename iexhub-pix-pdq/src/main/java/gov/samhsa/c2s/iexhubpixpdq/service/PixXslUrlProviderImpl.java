package gov.samhsa.c2s.iexhubpixpdq.service;

public class PixXslUrlProviderImpl implements PixXslUrlProvider {

    @Override
    public String getUrl(XslResource xslResource) {
        final String packageName = this.getClass().getPackage().getName();
        final String fileName = xslResource.getFileName();
        return getUrl(packageName, fileName);
    }

}

