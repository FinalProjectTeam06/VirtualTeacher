package com.example.finalprojectvirtualteacher.models.dto;

import jakarta.validation.constraints.*;
import org.checkerframework.common.aliasing.qual.Unique;


public class UserDtoUpdate {
    @NotEmpty(message = "name can't be empty")
    @Size(min = 2, max = 20, message = "First name should be between 2 and 20 symbols.")
    private String firstName;
    @NotNull(message = "name can't be empty")
    @Size(min = 2, max = 20, message = "Last name should be between 2 and 20 symbols.")
    private String lastName;

    @NotNull
    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,}$", message = "regex not match")
    private String password;

    @NotNull(message = "Confirm password should matches password field.")
    private String passwordConfirm;

    private String profilePhoto;


    public UserDtoUpdate() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

}