package com.example.finalprojectvirtualteacher.models.dto;

import java.util.Optional;

public class UserFilterOptionsDto {
    private String firstName;
    private String email;
    private String lastName;

    public UserFilterOptionsDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
