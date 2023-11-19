package com.example.finalprojectvirtualteacher.repositories.contracts;

import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.Rate;

import java.util.List;

public interface CourseRepository {
    List<Course> getAll();

    Course getById(int id);
    Rate getRating(int courseId, int userId);

    Course create(Course course);

    Course update(Course course);

    void delete(Course course);

    Course rateCourse(Rate rate);
    Course updateRating(Rate rate);

}
