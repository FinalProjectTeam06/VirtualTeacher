package com.virtualteacher.repositories.contracts;

import com.virtualteacher.models.Course;

import java.util.List;

public interface CourseRepository {
    List<Course> getAll();

    Course getById(int id);

}
