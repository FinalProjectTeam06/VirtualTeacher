package com.virtualteacher.helpers;

import com.virtualteacher.models.Lecture;
import com.virtualteacher.models.User;
import com.virtualteacher.models.dto.LectureDto;
import com.virtualteacher.services.contacts.CourseService;
import org.springframework.stereotype.Component;

@Component
public class LectureMapper {

   private final CourseService courseService;

    public LectureMapper(CourseService courseService) {
        this.courseService = courseService;
    }

    public Lecture fromDto(LectureDto lectureDto, User creator){
        Lecture lecture = new Lecture();
        lecture.setTitle(lectureDto.getTitle());
        lecture.setDescription(lectureDto.getDescription());
        lecture.setAssignmentUrl(lectureDto.getAssignment());
        lecture.setVideoUrl(lectureDto.getVideoUrl());
       lecture.setCourse(courseService.getById(lectureDto.getCourseId()));

        return lecture;
    }
}
