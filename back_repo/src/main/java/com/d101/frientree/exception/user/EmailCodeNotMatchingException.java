package com.d101.frientree.exception.user;

public class EmailCodeNotMatchingException extends RuntimeException {

    public EmailCodeNotMatchingException(String message) {
        super(message);
    }
}
