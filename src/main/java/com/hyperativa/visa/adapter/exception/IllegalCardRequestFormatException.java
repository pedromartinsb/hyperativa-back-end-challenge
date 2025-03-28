package com.hyperativa.visa.adapter.exception;

public class IllegalCardRequestFormatException extends RuntimeException {
    public IllegalCardRequestFormatException(String message) {
        super(message);
    }
}
