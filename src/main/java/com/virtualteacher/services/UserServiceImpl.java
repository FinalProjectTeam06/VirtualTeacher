package com.virtualteacher.services;

import com.virtualteacher.exceptions.AuthorizationException;
import com.virtualteacher.exceptions.EntityDuplicateException;
import com.virtualteacher.exceptions.EntityNotFoundException;
import com.virtualteacher.models.User;
import com.virtualteacher.models.UserFilterOptions;
import com.virtualteacher.models.dto.UserDtoUpdate;
import com.virtualteacher.repositories.contracts.UserRepository;
import com.virtualteacher.services.contacts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.userRepository = repository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public List<User> get(UserFilterOptions userFilterOptions) {
        return userRepository.get(userFilterOptions);
    }

    @Override
    public User getById(int id) {
        if (userRepository.getById(id) == null) {
            throw new EntityNotFoundException("User", "id", id);
        }
        return userRepository.getById(id);
    }

    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public User create(User user) {
        boolean duplicateExists = true;
        try {
            userRepository.getByUsername(user.getEmail());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("User", "username", user.getEmail());
        }

        return userRepository.create(user);
    }


    @Override
    public User getByEmail(String email) {
        User user = userRepository.getByEmail(email);

        if (user == null) {
            throw new EntityNotFoundException("User", "email", email);
        }

        return user;
    }

    @Override
    public User getByFirstName(String firstName) {
        User user = userRepository.getByFirstName(firstName);

        if (user == null) {
            throw new EntityNotFoundException("User", "firstName", firstName);
        }

        return user;
    }

    @Override
    public User updateUser(User user, User updatedUser, UserDtoUpdate userDtoUpdate) {
        if (userDtoUpdate.getFirstName() != null) {
            updatedUser.setFirstName(userDtoUpdate.getFirstName());
        }
        if (userDtoUpdate.getLastName() != null) {
            updatedUser.setLastName(userDtoUpdate.getLastName());
        }
        if (userDtoUpdate.getEmail() != null) {
            updatedUser.setEmail(userDtoUpdate.getEmail());
        }
        if (userDtoUpdate.getPassword() != null &&
                userDtoUpdate.getPassword().equals(userDtoUpdate.getPasswordConfirm())) {
            updatedUser.setPassword(userDtoUpdate.getPassword());
        }
        userRepository.updateUser(updatedUser);
        return updatedUser;
    }

    //todo delete user command must be added




}