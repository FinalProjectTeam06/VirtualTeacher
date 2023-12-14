package com.example.finalprojectvirtualteacher.services.contacts;

import com.example.finalprojectvirtualteacher.models.FilterOptions;
import com.example.finalprojectvirtualteacher.models.Rate;
import com.example.finalprojectvirtualteacher.models.dto.CourseDto;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.dto.RateDto;

import java.util.List;

public interface CourseService {
    List<Course> getAll(FilterOptions filterOptions);

    List<Course> getAll();

    List<Course> getAllActiveCoursesNotEnrolled(User user);

    Course getById(int id);
    int getAllEnrollments();
    List<Course> getAllByUserCompleted(int userId);
    List<Course> getAllByUserNotCompleted(int userId);
    List<Course> getAllByUserGraduated(int userId);

    List<Course> getAllPublishedCoursesFromTeacher(User user);
    List<Course> getAllNotPublishedCoursesFromTeacher(User user);

    Course create(CourseDto courseDto, User creator);

    Course update(CourseDto courseDto, User user, int courseId);

    void delete(int courseId, User user);
    Course publishCourse(int courseId, User user);
    List<Rate> getAllUserRates(int userId);
    Course rateCourse(int courseId, User user, RateDto rateDto);
    Double getCourseRating(Course course);

    void deleteAllCoursesFromUser(int userId);

    void deleteAllRatesFromUser(int userId);

}
