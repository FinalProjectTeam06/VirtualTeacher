package com.example.finalprojectvirtualteacher.models;

import java.util.Optional;

public class UserFilterOptions {
    private Optional<String> firstName;
    private Optional<String> lastName;
    private Optional<String> email;


    public UserFilterOptions(
            String lastName,
            String email,
            String firstName) {
        this.lastName = Optional.ofNullable(lastName);
        this.email = Optional.ofNullable(email);
        this.firstName = Optional.ofNullable(firstName);
    }

    public Optional<String> getLastName() {
        return lastName;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public Optional<String> getFirstName() {
        return firstName;
    }


}