package com.m3verificaciones.appweb.messages.exception;

public class ApiInternalErrorException extends RuntimeException {
    public ApiInternalErrorException(String message) {
        super(message);
    }
}
