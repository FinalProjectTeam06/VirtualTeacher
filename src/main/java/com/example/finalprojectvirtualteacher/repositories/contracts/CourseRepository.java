package com.example.finalprojectvirtualteacher.repositories.contracts;

import com.example.finalprojectvirtualteacher.models.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository{
    List<Course> getAll(FilterOptions filterOptions);
    List<Course> getAll();

    Course getById(int id);
    List<Course> getAllByUserCompleted(int userId);
    List<Course> getAllByUserNotCompleted(int userId);
    Rate getRating(int courseId, int userId);


    Course create(Course course);

    Course update(Course course);

    void delete(Course course);

    List<Lecture> getAllByTeacherId(int id);

    Course rateCourse(Rate rate);
    Course updateRating(Rate rate);

}
