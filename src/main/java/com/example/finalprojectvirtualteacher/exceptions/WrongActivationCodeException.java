package com.example.finalprojectvirtualteacher.exceptions;

public class WrongActivationCodeException extends RuntimeException{
    public WrongActivationCodeException(String msg) {
        super(msg);
    }

}