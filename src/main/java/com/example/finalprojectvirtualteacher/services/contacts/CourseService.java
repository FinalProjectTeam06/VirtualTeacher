package com.example.finalprojectvirtualteacher.services.contacts;

import com.example.finalprojectvirtualteacher.models.FilterOptions;
import com.example.finalprojectvirtualteacher.models.dto.CourseDto;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.dto.RateDto;

import java.util.List;

public interface CourseService {
    List<Course> getAll(FilterOptions filterOptions);
    List<Course> getAll();

    Course getById(int id);
    int getAllEnrollments();

    Course create(CourseDto courseDto, User creator);

    Course update(CourseDto courseDto, User user, int courseId);

    void delete(int courseId, User user);

    Course rateCourse(int courseId, User user, RateDto rateDto);
    Double getCourseRating(Course course);

}
