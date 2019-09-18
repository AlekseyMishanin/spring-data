package com.mishanin.springdata.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ProductErrorResponse> handleRTException(ProductNotFoundExceprion e){
        ProductErrorResponse productErrorResponse = new ProductErrorResponse();
        productErrorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        productErrorResponse.setMessage(e.getMessage());
        productErrorResponse.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(productErrorResponse, HttpStatus.BAD_REQUEST);
    }
}
