package com.d101.frientree.exception.user;

public class CustomJwtException extends RuntimeException {

    public CustomJwtException(String msg) {
        super(msg);
    }
}

