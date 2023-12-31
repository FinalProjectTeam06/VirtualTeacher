package com.example.finalprojectvirtualteacher.services.contacts;

import com.example.finalprojectvirtualteacher.models.Assignment;
import com.example.finalprojectvirtualteacher.models.Note;
import com.example.finalprojectvirtualteacher.models.dto.LectureDto;
import com.example.finalprojectvirtualteacher.models.Lecture;
import com.example.finalprojectvirtualteacher.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LectureService {


    List<Lecture> getAll();

    Lecture getById(int id);

    List<Lecture> getByCourseId(int id);

    Lecture create(LectureDto lectureDto, User creator);

    Lecture update(LectureDto lectureDto,User user,int lectureId);

    void delete (int id,User user);

    void deleteAllLecturesByUser(int userId);
    void deleteAllLecturesFromCourse(int courseId);
}
