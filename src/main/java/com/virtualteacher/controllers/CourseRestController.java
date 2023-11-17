package com.virtualteacher.controllers;

import com.virtualteacher.exceptions.AuthorizationException;
import com.virtualteacher.exceptions.EntityNotFoundException;
import com.virtualteacher.helpers.AuthenticationHelper;
import com.virtualteacher.models.Course;
import com.virtualteacher.models.User;
import com.virtualteacher.models.dto.CourseDto;
import com.virtualteacher.services.contacts.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseRestController {

    private final CourseService courseService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public CourseRestController(CourseService courseService, AuthenticationHelper authenticationHelper) {
        this.courseService = courseService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping()
    public List<Course> getAll() {
        return courseService.getAll();
    }

    @GetMapping("/{id}")
    public Course getById(@PathVariable int id) {
        return courseService.getById(id);
    }

    @PostMapping
    public Course create(@RequestHeader HttpHeaders httpHeaders, @Valid @RequestBody CourseDto courseDto) {
        try {
            User creator = authenticationHelper.tryGetUser(httpHeaders);
            return courseService.create(courseDto, creator);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @PutMapping("/{courseId}")
    public Course update(@RequestHeader HttpHeaders httpHeaders, @PathVariable int courseId, @Valid @RequestBody CourseDto courseDto) {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            return courseService.update(courseDto, user, courseId);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{courseId}")
    public void delete(@RequestHeader HttpHeaders httpHeaders, @PathVariable int courseId) {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            courseService.delete(courseId, user);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
