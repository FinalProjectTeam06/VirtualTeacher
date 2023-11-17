package com.virtualteacher.services.contacts;

import com.virtualteacher.models.Lecture;
import com.virtualteacher.models.User;
import com.virtualteacher.models.dto.LectureDto;

import java.util.List;

public interface LectureService {



    List<Lecture> getAll();

    Lecture getById(int id);

     Lecture create(LectureDto lectureDto, User creator);

    Lecture update(LectureDto lectureDto,User user,int lectureId);

    void delete (int id,User user);






}
