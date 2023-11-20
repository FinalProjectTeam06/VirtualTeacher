package com.example.finalprojectvirtualteacher.repositories.contracts;

import com.example.finalprojectvirtualteacher.models.Lecture;

import java.util.List;

public interface LectureRepository {



    List<Lecture> getAll();
    Lecture getById(int id);

    Lecture create(Lecture lecture);

    void delete(int id);

    Lecture update(Lecture updatedLecture);

    Lecture addAssignment(Lecture lecture);

    Note createNote(Note note1);

    Note getNote(int lectureId, int userId);

    Note updateNote(Note note);

}
