package com.agentic.ai.exception;

public class SchipholApiException extends RuntimeException {
    private final int statusCode;

    public SchipholApiException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public SchipholApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() { return statusCode; }
}
