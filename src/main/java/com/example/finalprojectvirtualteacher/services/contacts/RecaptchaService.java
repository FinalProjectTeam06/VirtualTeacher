package com.example.finalprojectvirtualteacher.services.contacts;

public interface RecaptchaService {
    void validateRecaptcha(String response);
    boolean verify(String response);
}
