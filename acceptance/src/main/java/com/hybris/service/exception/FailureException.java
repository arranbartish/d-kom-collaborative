package com.hybris.service.exception;

public class FailureException extends RuntimeException {

    public FailureException(String message) {
        this(message, null);
    }

    public FailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
