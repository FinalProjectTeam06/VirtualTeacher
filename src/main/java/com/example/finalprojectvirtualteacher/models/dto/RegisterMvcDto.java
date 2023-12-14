package com.example.finalprojectvirtualteacher.models.dto;

import jakarta.validation.constraints.*;

public class RegisterMvcDto {
    @Size(min = 2, max = 20, message = "First name should be between 2 and 20 symbols")
    private String firstName;


    @Size(min = 2, max = 20, message = "Last name should be between 2 and 20 symbols")
    private String lastName;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "email should be valid")
    private String email;


    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "The password must be at least 8 symbols and should contain capital letter, digit, and special symbol")
    private String password;


    @Size(min = 8 ,  message = "Confirm password must be at least 8 characters long")
    private String passwordConfirm;

    private boolean isTeacher;


    public RegisterMvcDto() {
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

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }
}
