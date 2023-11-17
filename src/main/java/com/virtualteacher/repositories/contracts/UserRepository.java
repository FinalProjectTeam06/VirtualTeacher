package com.virtualteacher.repositories.contracts;

import com.virtualteacher.models.User;
import com.virtualteacher.models.UserFilterOptions;

import java.util.List;

public interface UserRepository {
    List<User> getAll();

    User getById(int id);

    User getByUsername(String firstName);

    User getByEmail(String email);
    User getByFirstName(String firstName);
    User create(User user);

    void updateUser(User updatedUser);

    void deleteUser(User user);

    List<User> get(UserFilterOptions userFilterOptions);

    User addProfilePhoto(User user);
}
