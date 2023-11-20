package com.example.finalprojectvirtualteacher.services.contacts;

import com.example.finalprojectvirtualteacher.models.dto.LectureDto;
import com.example.finalprojectvirtualteacher.models.Lecture;
import com.example.finalprojectvirtualteacher.models.User;

import java.util.List;

public interface LectureService {



    List<Lecture> getAll();

    Lecture getById(int id);

     Lecture create(LectureDto lectureDto, User creator);

    Lecture update(LectureDto lectureDto,User user,int lectureId);

    void delete (int id,User user);

    void addAssignment(Lecture lecture, String assignment);

    void uploadSubmission(int userId, int lectureId, String assignment);
}
