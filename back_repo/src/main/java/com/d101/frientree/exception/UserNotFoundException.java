package com.d101.frientree.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("message: Fail");
    }
}
