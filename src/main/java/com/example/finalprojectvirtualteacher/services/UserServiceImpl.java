package com.example.finalprojectvirtualteacher.services;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.exceptions.EntityDuplicateException;
import com.example.finalprojectvirtualteacher.models.UserFilterOptions;
import com.example.finalprojectvirtualteacher.models.dto.UserDto;
import com.example.finalprojectvirtualteacher.models.dto.UserDtoUpdate;
import com.example.finalprojectvirtualteacher.services.contacts.UserService;
import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.repositories.contracts.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    public static final String PERMISSION_ERROR = "You don't have permission.";

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.userRepository = repository;
    }

    @Override
    public List<User> getAll(UserFilterOptions userFilterOptions) {
        return userRepository.get(userFilterOptions);
    }

    @Override
    public User getById(int id) {
        if (userRepository.getById(id) == null) {
            throw new EntityNotFoundException("User", "id", id);
        }
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
        userRepository.deleteUser(user);
    }

    private void checkPermission(User user, int userId){
        if (!user.getRole().getName().equals("admin") && user.getId()!=userId){
            throw new AuthorizationException(PERMISSION_ERROR);
        }
    }

}