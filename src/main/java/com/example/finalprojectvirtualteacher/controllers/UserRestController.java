package com.example.finalprojectvirtualteacher.controllers;

import com.example.finalprojectvirtualteacher.exceptions.*;
import com.example.finalprojectvirtualteacher.helpers.AuthenticationHelper;
import com.example.finalprojectvirtualteacher.helpers.UserMapper;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.UserFilterOptions;
import com.example.finalprojectvirtualteacher.models.dto.UserDto;
import com.example.finalprojectvirtualteacher.models.dto.UserDtoUpdate;
import com.example.finalprojectvirtualteacher.services.contacts.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
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
    public List<User> getAll(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email) {
        UserFilterOptions userFilterOptions = new UserFilterOptions(firstName, lastName, email);
        return userService.getAll(userFilterOptions);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable int id) {
        try {
            return userService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @PostMapping
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
                               @PathVariable int userId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userToUpdate = userService.getById(userId);
            return userService.updateUser(user, userToUpdate, userDtoUpdate);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            userService.deleteUser(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/course/{courseId}")
    public User enrollCourse(@RequestHeader HttpHeaders httpHeaders, @PathVariable int courseId) {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            return userService.enrollCourse(user, courseId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


    @PostMapping("/activate/{code}")
    public String activateUser(@PathVariable int code) {
        try {
            userService.activateAccount(code);
            return "Account activated";
        } catch (WrongActivationCodeException e) {
            return "Code has expired! Resend code.";
        } catch (ForbiddenOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @PostMapping("/activate/new-code")
    public String sendNewActivationCode(Principal principal) {
        try {
            userService.resendActivationCode(principal.getName());
            return "New activation code sent";
        } catch (ForbiddenOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

}