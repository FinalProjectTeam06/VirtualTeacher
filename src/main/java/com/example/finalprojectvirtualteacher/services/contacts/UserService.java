package com.example.finalprojectvirtualteacher.services.contacts;

import com.example.finalprojectvirtualteacher.models.UserFilterOptions;
import com.example.finalprojectvirtualteacher.models.dto.UserDtoUpdate;
import com.example.finalprojectvirtualteacher.models.User;

import java.util.List;

public interface UserService {
    List<User> getAll(UserFilterOptions userFilterOptions);
    List<User> getAll();
    List<User> getAllStudents();
    List<User> getAllTeachers();

    User getById(int id);
    User getByEmail(String currentEmail);

    User create(User user);

    User updateUser(User user, User updatedUser, UserDtoUpdate userDtoUpdate);

    void deleteUser(int id, User user);

    void deleteAssignmentsFromUserAndLecture(int userId);

    User enrollCourse(User user, int courseId);
    void setEnrollmentCourseStatusToFinished(int userId, int courseId);
    void setEnrollmentCourseStatusToGraduated(int userId, int courseId);

    User addProfilePhoto(User user, String url);
    void activateAccount(int code);
    void resendActivationCode(String username);
    void sendActivationEmail(User user);
    void inviteFriend(User inviter, String friendEmail);

}
