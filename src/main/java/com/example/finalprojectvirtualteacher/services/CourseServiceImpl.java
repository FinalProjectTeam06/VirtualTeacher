package com.example.finalprojectvirtualteacher.services;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.helpers.mappers.CourseMapper;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.Rate;
import com.example.finalprojectvirtualteacher.models.FilterOptions;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.dto.CourseDto;
import com.example.finalprojectvirtualteacher.models.dto.RateDto;
import com.example.finalprojectvirtualteacher.repositories.contracts.CourseRepository;
import com.example.finalprojectvirtualteacher.services.contacts.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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
    public List<Course> getAll(FilterOptions filterOptions){
        return courseRepository.getAll(filterOptions);
    }

    @Override
    public List<Course> getAll() {
        return courseRepository.getAll();
    }



    @Override
    public List<Course> getAllActiveCoursesNotEnrolled(User user) {
        return courseRepository.getAllActiveCoursesNotEnrolled(user);
    }

    @Override
    public List<Course> getAllByUserCompleted(int userId) {
        return courseRepository.getAllByUserCompleted(userId);
    }

    @Override
    public List<Course> getAllByUserNotCompleted(int userId) {
        return courseRepository.getAllByUserNotCompleted(userId);
    }

    @Override
    public List<Course> getAllPublishedCoursesFromTeacher(User user){
        checkModifyPermission(user);
        return courseRepository.getAllPublishedCoursesFromTeacher(user);
    }

    @Override
    public List<Course> getAllNotPublishedCoursesFromTeacher(User user) {
        checkModifyPermission(user);
        return courseRepository.getAllNotPublishedCoursesFromTeacher(user);
    }

    @Override
    public Course getById(int id) {
        return courseRepository.getById(id);
    }
    public int getAllEnrollments(){
        int count=0;
        for (Course course : getAll()) {
            count+=course.getStudents().size();
        }
        return count;
    }

    @Override
    public Course create(CourseDto courseDto, User creator) {
        Course course = courseMapper.fromDtoIn(courseDto, creator);
        checkModifyPermission(creator);
        return courseRepository.create(course);
    }

    @Override
    public Course update(CourseDto courseDto, User user, int courseId) {
        Course course = getById(courseId);
        courseMapper.fromDtoUpdate(courseDto, course);
        checkPermission(course, user);
        return courseRepository.update(course);
    }

    @Override
    public void delete(int courseId, User user) {
        Course course = getById(courseId);
        checkPermission(course, user);
        courseRepository.delete(course);
    }
    @Override
    public Course publishCourse(int courseId, User user){
        Course course=getById(courseId);
        course.setPublished(true);
        return courseRepository.update(course);
    }

    @Override
    public Course rateCourse(int courseId, User user, RateDto rateDto) {
        Course course = getById(courseId);
        try {
            Rate rate = courseRepository.getRating(courseId, user.getId());
        } catch (EntityNotFoundException e) {
            if (!course.getRates().stream().map(Rate::getUser).toList().contains(user)) {
                Rate rate = new Rate();
                rate.setUser(user);
                rate.setCourse(getById(courseId));
                rate.setRateValue(rateDto.getRateValue());
                rate.setComment(rateDto.getComment());
                course.addRateToCourse(rate);
                course.setRating(getCourseRating(course));
                courseRepository.update(course);
                return courseRepository.rateCourse(rate);
            }
        }
        Rate rate = courseRepository.getRating(courseId, user.getId());
        if (!rateDto.getComment().isEmpty()) {
            rate.setComment(rateDto.getComment());
        }
        rate.setRateValue(rateDto.getRateValue());
        course.setRating(getCourseRating(course));
        courseRepository.update(course);
        return courseRepository.updateRating(rate);
    }

    @Override
    public Double getCourseRating(Course course) {
        Set<Rate> rates = course.getRates();
        if (course.getRates().isEmpty()) {
            return 5.0;
        }
        double sum = 0.0;
        for (Rate rate : rates) {
            sum += rate.getRateValue();
        }
        return sum / rates.size();
    }





    private void checkModifyPermission(User user) {
        if (!user.getRole().getName().equals("admin") && !user.getRole().getName().equals("teacher")) {
            throw new AuthorizationException(PERMISSION_ERROR);
        }
    }

    public void checkPermission(Course course, User user) {
        if (!course.getCreator().equals(user) && user.getRole().getId()!=3) {
            throw new AuthorizationException(PERMISSION_ERROR);
        }
    }
}
