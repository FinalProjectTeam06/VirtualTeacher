package com.example.finalprojectvirtualteacher.services.contacts;

import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.UserFilterOptions;
import com.example.finalprojectvirtualteacher.models.dto.UserDtoUpdate;
import com.example.finalprojectvirtualteacher.models.User;

import java.util.List;

public interface UserService {
    List<User> getAll(UserFilterOptions userFilterOptions);
    List<User> getAll();
    List<User> getAllTeachers();

    User getById(int id);
    User getByEmail(String currentEmail);

    User create(User user);

    User updateUser(User user, User updatedUser, UserDtoUpdate userDtoUpdate);

    void deleteUser(int id, User user);

    User enrollCourse(User user, int courseId);
}
