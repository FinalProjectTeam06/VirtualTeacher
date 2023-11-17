package com.virtualteacher.services.contacts;

import com.virtualteacher.models.Course;

import java.util.List;

public interface CourseService {
    List<Course> getAll();

    Course getById(int id);
}
