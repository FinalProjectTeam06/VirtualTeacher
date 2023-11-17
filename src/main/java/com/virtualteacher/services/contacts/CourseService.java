package com.virtualteacher.services.contacts;

import com.virtualteacher.models.Course;
import com.virtualteacher.models.User;
import com.virtualteacher.models.dto.CourseDto;

import java.util.List;

public interface CourseService {
    List<Course> getAll();

    Course getById(int id);

    Course create(CourseDto courseDto, User creator);

    Course update(CourseDto courseDto, User user, int courseId);

    void delete(int courseId, User user);

}
