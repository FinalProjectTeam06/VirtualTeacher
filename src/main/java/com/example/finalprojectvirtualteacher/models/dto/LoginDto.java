package com.example.finalprojectvirtualteacher.models.dto;

import jakarta.validation.constraints.*;
import org.checkerframework.common.aliasing.qual.Unique;

public class LoginDto {
    @NotEmpty(message = "Email can't be empty.")
    @Email
    private String email;
    @NotEmpty(message = "Password can't be empty.")
    private String password;


    public LoginDto() {
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
