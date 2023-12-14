package com.example.finalprojectvirtualteacher.helpers.mappers;

import com.example.finalprojectvirtualteacher.models.Lecture;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.dto.LectureDto;
import com.example.finalprojectvirtualteacher.repositories.contracts.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LectureMapper {
    private final CourseRepository courseRepository;

    @Autowired
    public LectureMapper(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Lecture fromDto(LectureDto lectureDto, User creator) {
        Lecture lecture = new Lecture();
        lecture.setTitle(lectureDto.getTitle());
        lecture.setDescription(lectureDto.getDescription());
        lecture.setAssignmentUrl(lectureDto.getAssignmentUrl());
        lecture.setVideoUrl(lectureDto.getVideoUrl());
        lecture.setTeacher(creator);
        lecture.setCourse(courseRepository.getById(lectureDto.getCourseId()));

        return lecture;
    }

}
