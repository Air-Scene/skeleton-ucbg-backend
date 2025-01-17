package com.example.demo.config;

import com.example.demo.auth.exception.RegistrationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<ErrorResponse> handleRegistrationException(RegistrationException ex) {
        return ResponseEntity
            .badRequest()
            .body(new ErrorResponse(ex.getMessage()));
    }

    record ErrorResponse(String message) {}
} 