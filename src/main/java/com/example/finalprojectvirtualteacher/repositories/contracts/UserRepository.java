package com.example.finalprojectvirtualteacher.repositories.contracts;

import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.UserFilterOptions;

import java.util.List;

public interface UserRepository {
    List<User> getAll();

    List<User> getAll(UserFilterOptions userFilterOptions);

    List<User> getAllTeachers();

    User getById(int id);

    User getByEmail(String email);

    User create(User user);

    User updateUser(User updatedUser);

    void deleteUser(User user);

    void setCourseEnrollmentStatusToFinished(int userId, int courseId);
    void setEnrollmentCourseStatusToGraduated(int userId, int courseId);

    List<User> getAllStudents();
    void deleteAllRatesByUser(int userId);

}
