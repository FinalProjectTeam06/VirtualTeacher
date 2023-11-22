package com.example.finalprojectvirtualteacher.repositories.contracts;

import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.FilterOptions;
import com.example.finalprojectvirtualteacher.models.Lecture;
import com.example.finalprojectvirtualteacher.models.Rate;

import java.util.List;

public interface CourseRepository {

    Course getById(int id);
    Rate getRating(int courseId, int userId);

    List<Course> getAll(FilterOptions filterOptions);

    Course create(Course course);

    Course update(Course course);

    void delete(Course course);

    List<Lecture> getAllByTeacherId(int id);

    Course rateCourse(Rate rate);
    Course updateRating(Rate rate);

}
