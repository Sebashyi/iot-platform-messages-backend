package com.m3verificaciones.appweb.messages.exception;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

        // Constantes para evitar duplicación (Solución a S1192)
        private static final String ERROR_KEY = "error";
        private static final String MESSAGE_KEY = "message";
        private static final String FIELD_KEY = "field";
        private static final String DETAILS_KEY = "details";

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<Map<String, String>> handleInvalidJson(HttpMessageNotReadableException ex) {
                return ResponseEntity
                                .badRequest()
                                .body(Map.of(
                                                ERROR_KEY, "Malformed JSON or type mismatch",
                                                DETAILS_KEY, ex.getMostSpecificCause().getMessage()));
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
                var fieldError = ex.getBindingResult().getFieldError();
                return ResponseEntity
                                .badRequest()
                                .body(Map.of(
                                                ERROR_KEY, "Validation error",
                                                FIELD_KEY, fieldError != null ? fieldError.getField() : "Unknown field",
                                                MESSAGE_KEY, fieldError != null ? fieldError.getDefaultMessage()
                                                                : "Unknown validation error"));
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
                return ResponseEntity
                                .badRequest()
                                .body(Map.of(
                                                ERROR_KEY, "Constraint violation",
                                                MESSAGE_KEY, ex.getMessage()));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
                return ResponseEntity
                                .internalServerError()
                                .body(Map.of(
                                                ERROR_KEY, "Unknown error",
                                                MESSAGE_KEY, ex.getMessage()));
        }

        // Nuevo handler para tu excepción personalizada
        @ExceptionHandler(ApiValidationException.class)
        public ResponseEntity<Map<String, String>> handleApiValidation(ApiValidationException ex) {
                return ResponseEntity
                                .badRequest()
                                .body(Map.of(
                                                ERROR_KEY, "Validation error",
                                                MESSAGE_KEY, ex.getMessage()));
        }

        // Nuevo handler para tu excepción personalizada al momento de hacer un unpdate
        // a un mensaje
        @ExceptionHandler(MessageUpdateException.class)
        public ResponseEntity<Map<String, String>> handleMessageUpdateException(MessageUpdateException ex) {
                return ResponseEntity
                                .badRequest()
                                .body(Map.of(
                                                ERROR_KEY, "Message update failed",
                                                MESSAGE_KEY, ex.getMessage(),
                                                DETAILS_KEY, ex.getCause() != null ? ex.getCause().getMessage()
                                                                : "No additional details"));
        }

        @ExceptionHandler(NoResultsFoundException.class)
        public ResponseEntity<Map<String, String>> handleNoResultsFound(NoResultsFoundException ex) {
                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(Map.of(
                                                ERROR_KEY, "No results found",
                                                MESSAGE_KEY, ex.getMessage()));
        }
}