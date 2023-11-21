package com.example.finalprojectvirtualteacher.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.checkerframework.common.aliasing.qual.Unique;

public class UserDto extends LoginDto {

    @NotNull(message = "First name can't be empty")
    @Size(min = 2, max = 20, message = "First name should be between 2 and 20 symbols.")
    private String firstName;
    @NotNull(message = "Last name can't be empty")
    @Size(min = 2, max = 20, message = "Last name should be between 2 and 20 symbols.")
    private String lastName;
    @Email
    @Unique
    private String email;

    @NotNull
    private int roleId;

    @NotNull(message = "Password can't be empty.")
    @Size(min = 8, message = "Password should be at least 8 symbols.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "regex not match")
    private String passwordConfirm;


    public UserDto() {
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

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}




