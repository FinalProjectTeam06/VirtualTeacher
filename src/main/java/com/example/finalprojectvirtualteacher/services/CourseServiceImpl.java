package com.example.finalprojectvirtualteacher.services;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.helpers.CourseMapper;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.dto.CourseDto;
import com.example.finalprojectvirtualteacher.repositories.contracts.CourseRepository;
import com.example.finalprojectvirtualteacher.services.contacts.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    public static final String PERMISSION_ERROR = "You don't have permission.";
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    @Override
    public List<Course> getAll() {
        return courseRepository.getAll();
    }

    @Override
    public Course getById(int id) {
        return courseRepository.getById(id);
    }

    @Override
    public Course create(CourseDto courseDto, User creator) {
        Course course=courseMapper.fromDtoIn(courseDto, creator);
        checkCreatePermission(creator);
        return courseRepository.create(course);
    }

    @Override
    public Course update(CourseDto courseDto, User user, int courseId) {
        Course course=getById(courseId);
        courseMapper.fromDtoUpdate(courseDto, course);
        checkPermission(course, user);
        return courseRepository.update(course);
    }

    @Override
    public void delete(int courseId, User user) {
        Course course=getById(courseId);
        checkPermission(course, user);
        courseRepository.delete(course);
    }

    private void checkCreatePermission(User user){
        if (!user.getRole().getName().equals("admin") && !user.getRole().getName().equals("teacher")){
            throw new AuthorizationException(PERMISSION_ERROR);
        }
    }

    private void checkPermission(Course course, User user){
        if (!course.getCreator().equals(user) && !user.getRole().getName().equals("admin")){
            throw new AuthorizationException(PERMISSION_ERROR);
        }
    }
}
