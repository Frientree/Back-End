package com.d101.frientree.exception;

public class EmailDuplicatedException extends RuntimeException {

    public EmailDuplicatedException(String message) {
        super(message);
    }
}
