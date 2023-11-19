package com.example.finalprojectvirtualteacher.repositories.contracts;

import com.example.finalprojectvirtualteacher.models.UserFilterOptions;
import com.example.finalprojectvirtualteacher.models.User;

import java.util.List;

public interface UserRepository {
    List<User> getAll();

    User getById(int id);

    User getByEmail(String email);
    User create(User user);

    User updateUser(User updatedUser);

    void deleteUser(User user);

    List<User> get(UserFilterOptions userFilterOptions);

}
