package com.example.finalprojectvirtualteacher.repositories.contracts;

import com.example.finalprojectvirtualteacher.models.Assignment;
import com.example.finalprojectvirtualteacher.models.Lecture;
import com.example.finalprojectvirtualteacher.models.Note;

import java.util.List;

public interface LectureRepository {



    List<Lecture> getAll();
    Lecture getById(int id);

    List<Lecture> lecturesByCourseId(int id);

    Lecture getByTitle(String title);

    Lecture create(Lecture lecture);

    void delete(int id);

    Lecture update(Lecture updatedLecture);

    Lecture submitAssignment(Assignment assignment);

    void deleteAllLecturesByUser(int userId);

    void deleteAllLecturesFromCourse(int courseId);
}
