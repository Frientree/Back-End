package com.d101.frientree.exception.user;

public class PasswordNotMatchingException extends RuntimeException {

    public PasswordNotMatchingException(String message) {
        super(message);
    }
}
