package com.m3verificaciones.appweb.messages.exception;

public class MessageUpdateException extends RuntimeException {
    public MessageUpdateException(String message) {
        super(message);
    }
    
    public MessageUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public MessageUpdateException(Throwable cause) {
        super(cause);
    }
}