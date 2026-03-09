package com.m3verificaciones.appweb.messages.exception;

public class ApiConstraintViolationException extends RuntimeException {
    public ApiConstraintViolationException(String message) {
        super(message);
    }
}
