package com.example.finalprojectvirtualteacher.services;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.exceptions.EntityDuplicateException;
import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.UserFilterOptions;
import com.example.finalprojectvirtualteacher.models.dto.UserDtoUpdate;
import com.example.finalprojectvirtualteacher.repositories.contracts.UserRepository;
import com.example.finalprojectvirtualteacher.services.contacts.CourseService;
import com.example.finalprojectvirtualteacher.services.contacts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    public static final String PERMISSION_ERROR = "You don't have permission.";

    private final UserRepository userRepository;
    private final CourseService courseService;

    @Autowired
    public UserServiceImpl(UserRepository repository, CourseService courseService) {
        this.userRepository = repository;
        this.courseService = courseService;
    }

    @Override
    public List<User> getAll(UserFilterOptions userFilterOptions) {
        return userRepository.getAll(userFilterOptions);
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public List<User> getAllTeachers() {
        return userRepository.getAllTeachers();
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByEmail(String userEmail) {
        return userRepository.getByEmail(userEmail);
    }

    @Override
    public User create(User user) {
        boolean duplicateExists = true;
        try {
            userRepository.getByEmail(user.getEmail());
        } catch (EntityNotFoundException e) {
                duplicateExists = false;
        }
        if (duplicateExists) {
            throw new EntityDuplicateException("User", "username", user.getEmail());
        }

        return userRepository.create(user);
    }

    @Override
    public User updateUser(User user, User updatedUser, UserDtoUpdate userDtoUpdate) {
        checkPermission(user, updatedUser.getId());
        if (userDtoUpdate.getFirstName() != null) {
            updatedUser.setFirstName(userDtoUpdate.getFirstName());
        }
        if (userDtoUpdate.getLastName() != null) {
            updatedUser.setLastName(userDtoUpdate.getLastName());
        }
        if (userDtoUpdate.getPassword() != null &&
                userDtoUpdate.getPassword().equals(userDtoUpdate.getPasswordConfirm())) {
            updatedUser.setPassword(userDtoUpdate.getPassword());
        }
        return userRepository.updateUser(updatedUser);
    }

    @Override
    public void deleteUser(int id, User user) {
        checkPermission(user, id);
        User userToDelete = getById(id);
        userRepository.deleteUser(userToDelete);
    }

    @Override
    public User enrollCourse(User user, int courseId) {
        Course course = courseService.getById(courseId);
        user.addCourse(course);
        return userRepository.updateUser(user);
    }

    @Override
    public User addProfilePhoto(User user, String url) {
        user.setProfilePictureUrl(url);
        return userRepository.updateUser(user);
    }

    private void checkPermission(User user, int userId) {
        if (!user.getRole().getName().equals("admin") && user.getId() != userId) {
            throw new AuthorizationException(PERMISSION_ERROR);
        }
    }
}