package com.potalab.http.auth.digest.exception;

public class NonceNotCreatedException extends Exception {
    public NonceNotCreatedException(String message) {
        super(message);
    }
}
