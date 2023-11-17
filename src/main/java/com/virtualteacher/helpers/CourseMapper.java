package com.virtualteacher.helpers;

import com.virtualteacher.models.Course;
import com.virtualteacher.models.User;
import com.virtualteacher.models.dto.CourseDto;
import com.virtualteacher.services.contacts.CourseService;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {
    private final CourseService courseService;

    public CourseMapper(CourseService courseService) {
        this.courseService = courseService;
    }


    public Course fromDtoIn(CourseDto courseDto, User creator) {
        Course course= new Course();
        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());
//        course.setTopic(); //TODO set topic when create getTopicById
        course.setPublished(false);
        course.setCreator(creator);
        course.setStartDate(courseDto.getStartDate());
        return course;
    }

    public Course fromDtoUpdate(CourseDto courseDto, int courseId){
        Course course=courseService.getById(courseId);
        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());
        return course;
    }
}
