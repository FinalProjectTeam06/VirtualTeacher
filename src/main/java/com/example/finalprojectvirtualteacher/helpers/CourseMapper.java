package com.example.finalprojectvirtualteacher.helpers;

import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.dto.CourseDto;
import com.example.finalprojectvirtualteacher.services.contacts.TopicService;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {
    private final TopicService topicService;

    public CourseMapper(TopicService topicService) {
        this.topicService = topicService;
    }

    public Course fromDtoIn(CourseDto courseDto, User creator) {
        Course course = new Course();
        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());
        course.setTopic(topicService.getById(courseDto.getTopicId()));
        course.setPublished(false);
        course.setCreator(creator);
        course.setStartDate(courseDto.getStartDate());
        course.setMinGrade(courseDto.getMinGrade());
        return course;
    }

    public Course fromDtoUpdate(CourseDto courseDto, Course course) {
        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());
        course.setStartDate(courseDto.getStartDate());
        return course;
    }
    public CourseDto toDtoUpdate(Course course) {
        CourseDto courseDto=new CourseDto();
        courseDto.setTitle(course.getTitle());
        courseDto.setDescription(course.getDescription());
        courseDto.setStartDate(course.getStartDate());
        return courseDto;
    }
}
