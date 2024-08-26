package com.kcm.jpacalender.exception;

public class NoAuthenticationUser extends RuntimeException {
    public NoAuthenticationUser(String message) {
        super(message);
    }
}
