package com.example.finalprojectvirtualteacher.helpers;

import com.example.finalprojectvirtualteacher.models.Lecture;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.dto.LectureDto;
import com.example.finalprojectvirtualteacher.services.contacts.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LectureMapper {

    private final CourseService courseService;

    @Autowired
    public LectureMapper(CourseService courseService) {
        this.courseService = courseService;
    }

    public Lecture fromDto(LectureDto lectureDto, User creator) {
        Lecture lecture = new Lecture();
        lecture.setTitle(lectureDto.getTitle());
        lecture.setDescription(lectureDto.getDescription());
        lecture.setAssignmentUrl(lectureDto.getAssignment());
        lecture.setVideoUrl(lectureDto.getVideoUrl());
        lecture.setTeacher(creator);
        lecture.setCourse(courseService.getById(lectureDto.getCourseId()));

        return lecture;
    }
}
