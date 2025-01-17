package com.example.demo.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class TokenRefreshException extends AuthenticationException {
    public TokenRefreshException(String msg) {
        super(msg);
    }

    public TokenRefreshException(String msg, Throwable cause) {
        super(msg, cause);
    }
} 