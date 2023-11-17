package com.virtualteacher.services;

import com.virtualteacher.models.Course;
import com.virtualteacher.repositories.contracts.CourseRepository;
import com.virtualteacher.services.contacts.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> getAll() {
        return courseRepository.getAll();
    }

    @Override
    public Course getById(int id) {
        return courseRepository.getById(id);
    }
}
