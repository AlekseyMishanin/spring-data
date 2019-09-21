package com.mishanin.springdata.errors;

public class AccessDeniedProductException extends RuntimeException {
    public AccessDeniedProductException(String message) {
        super(message);
    }
}
