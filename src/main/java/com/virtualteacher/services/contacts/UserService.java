package com.virtualteacher.services.contacts;

import com.virtualteacher.models.User;
import com.virtualteacher.models.UserFilterOptions;
import com.virtualteacher.models.dto.UserDtoUpdate;

import java.util.List;

public interface UserService {
    List<User> getAll();

    List<User> get(UserFilterOptions userFilterOptions);

    User getById(int id);

    User create(User user);

    public User getByEmail(String email);

    User getByFirstName(String firstName);

    User updateUser(User user, User updatedUser, UserDtoUpdate userDtoUpdate);
}
