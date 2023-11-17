package com.example.finalprojectvirtualteacher.repositories.contracts;

import com.example.finalprojectvirtualteacher.models.Course;

import java.util.List;

public interface CourseRepository {
    List<Course> getAll();

    Course getById(int id);

    Course create(Course course);

    Course update(Course course);

    void delete(Course course);
}
