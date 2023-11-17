package com.virtualteacher.helpers;

import com.virtualteacher.models.Course;
import com.virtualteacher.models.User;
import com.virtualteacher.models.dtos.CourseDto;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

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
}
