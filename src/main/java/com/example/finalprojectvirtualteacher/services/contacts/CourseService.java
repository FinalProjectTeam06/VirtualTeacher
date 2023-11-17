package com.example.finalprojectvirtualteacher.services.contacts;

import com.example.finalprojectvirtualteacher.models.dto.CourseDto;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.User;

import java.util.List;

public interface CourseService {
    List<Course> getAll();

    Course getById(int id);

    Course create(CourseDto courseDto, User creator);

    Course update(CourseDto courseDto, User user, int courseId);

    void delete(int courseId, User user);

}
