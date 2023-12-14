package com.example.finalprojectvirtualteacher.repositories.contracts;

import com.example.finalprojectvirtualteacher.models.*;

import java.util.List;

public interface CourseRepository{
    List<Course> getAll(FilterOptions filterOptions);
    List<Course> getAll();

    Course getById(int id);
    List<Course> getAllByUserCompleted(int userId);
    List<Course> getAllByUserNotCompleted(int userId);
    List<Course> getAllByUserGraduated(int userId);

    Rate getRating(int courseId, int userId);


    Course create(Course course);

    Course update(Course course);

    void delete(Course course);

    List<Lecture> getAllByTeacherId(int id);
    List<Rate> getAllUserRates(int userId);


    Course rateCourse(Rate rate);
    Course updateRating(Rate rate);

    List<Course> getAllPublishedCoursesFromTeacher(User user);

    List<Course> getAllNotPublishedCoursesFromTeacher(User user);

    List<Course> getAllActiveCoursesNotEnrolled(User user);


    void deleteAllCoursesFromUser(int userId);
    void deleteAllRatesFromUser(int userId);
    void deleteAllRatesFromCourse(int courseId);
    void deleteAllEnrollmentsFromCourse(int courseId);

}
