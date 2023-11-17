package com.virtualteacher.controllers;

import com.virtualteacher.exceptions.AuthorizationException;
import com.virtualteacher.exceptions.EntityDuplicateException;
import com.virtualteacher.exceptions.EntityNotFoundException;
import com.virtualteacher.helpers.AuthenticationHelper;
import com.virtualteacher.helpers.UserMapper;
import com.virtualteacher.models.User;
import com.virtualteacher.models.dto.UserDto;
import com.virtualteacher.models.dto.UserDtoUpdate;
import com.virtualteacher.services.contacts.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;

    @Autowired
    public UserRestController(UserService userService, AuthenticationHelper authenticationHelper, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
    }

    @GetMapping
    public List<User> getAll(@RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return userService.getAll();
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public User getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return userService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{email}")
    public User getByEmail(@RequestHeader HttpHeaders headers, @PathVariable String email) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return userService.getByEmail(email);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{firstName}")
    public User getByFirstName(@RequestHeader HttpHeaders headers, @PathVariable String firstName) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return userService.getByFirstName(firstName);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping()
    public User create(@Valid @RequestBody UserDto userDto) {
        try {
            User user = userMapper.fromDto(userDto);
            userService.create(user);
            return user;
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }




    @PutMapping("/{userId}")
    public User updateUserInfo(@RequestHeader HttpHeaders headers,
                               @Valid @RequestBody UserDtoUpdate userDtoUpdate,
                               @PathVariable int userId){
        try {
            User user= authenticationHelper.tryGetUser(headers);
            User userToUpdate=userService.getById(userId);
            userService.updateUser(user, userToUpdate, userDtoUpdate);
            return userToUpdate;
        }catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

//    @DeleteMapping("/{id}")
//    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
//        try {
//            User user = authenticationHelper.tryGetUser(headers);
//            userService.deleteUser(id, user);
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        } catch (AuthorizationException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        }
//    }
}