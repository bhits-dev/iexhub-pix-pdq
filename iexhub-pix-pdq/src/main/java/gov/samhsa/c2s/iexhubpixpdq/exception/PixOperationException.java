package gov.samhsa.c2s.iexhubpixpdq.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PixOperationException extends RuntimeException {
    public PixOperationException() {
    }

    public PixOperationException(String message) {
        super(message);
    }

    public PixOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PixOperationException(Throwable cause) {
        super(cause);
    }

    public PixOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}