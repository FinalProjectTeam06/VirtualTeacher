package com.virtualteacher.repositories.contracts;

import com.virtualteacher.models.Course;
import com.virtualteacher.models.User;

import java.util.List;

public interface CourseRepository {
    List<Course> getAll();

    Course getById(int id);

    Course create(Course course);

    Course update(Course course);

    void delete(Course course);
}
