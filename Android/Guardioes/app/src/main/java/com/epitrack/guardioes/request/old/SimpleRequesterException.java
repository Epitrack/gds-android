package com.epitrack.guardioes.request.old;

/**
 * Created by miqueiaslopes on 09/09/15.
 */
public class SimpleRequesterException extends Exception {

    public SimpleRequesterException() {
    }

    public SimpleRequesterException(String detailMessage) {
        super(detailMessage);
    }

    public SimpleRequesterException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public SimpleRequesterException(Throwable throwable) {
        super(throwable);
    }
}
