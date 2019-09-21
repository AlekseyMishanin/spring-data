package com.mishanin.springdata.errors;

public class ProductNotFoundExceprion extends RuntimeException {
    public ProductNotFoundExceprion(String message) {
        super(message);
    }
}
