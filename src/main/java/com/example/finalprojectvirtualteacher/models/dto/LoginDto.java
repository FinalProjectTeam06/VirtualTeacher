package com.example.finalprojectvirtualteacher.models.dto;

import com.example.finalprojectvirtualteacher.models.RecaptchaResponse;
import jakarta.validation.constraints.*;
import org.checkerframework.common.aliasing.qual.Unique;

public class LoginDto  {

    @NotEmpty(message = "Email can't be empty.")
    @Email
    private String email;
    @NotEmpty(message = "Password can't be empty.")
    private String password;
    private String recaptchaResponse;
    public LoginDto() {
    }


    public String getRecaptchaResponse() {
        return recaptchaResponse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
