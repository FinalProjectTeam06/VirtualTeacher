package com.example.finalprojectvirtualteacher.controllers;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.helpers.AuthenticationHelper;
import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.Rate;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.dto.CourseDto;
import com.example.finalprojectvirtualteacher.models.dto.RateDto;
import com.example.finalprojectvirtualteacher.services.contacts.CourseService;
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

    @PostMapping("/rating/{courseId}")
    public Course rateCourse(@RequestHeader HttpHeaders httpHeaders,
                             @PathVariable int courseId, @Valid @RequestBody RateDto rateDto){
        try {
            User user= authenticationHelper.tryGetUser(httpHeaders);
            return courseService.rateCourse(courseId, user,rateDto);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/rating/{courseId}")
    public String getCourseRate(@PathVariable int courseId){
        try {
            return courseService.getCourseRating(courseService.getById(courseId)).toString();
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
