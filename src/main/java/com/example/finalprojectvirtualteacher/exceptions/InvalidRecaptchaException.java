package com.example.finalprojectvirtualteacher.exceptions;

public class InvalidRecaptchaException extends RuntimeException {

    public InvalidRecaptchaException(String message) {
        super(message);
    }

}