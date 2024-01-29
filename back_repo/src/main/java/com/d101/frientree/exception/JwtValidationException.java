package com.d101.frientree.exception;

public class JwtValidationException extends RuntimeException {

    public JwtValidationException (String message) {
        super(message);
    }
}
