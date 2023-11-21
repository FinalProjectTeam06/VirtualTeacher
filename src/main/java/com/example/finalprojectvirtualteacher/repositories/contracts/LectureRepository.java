package com.example.finalprojectvirtualteacher.repositories.contracts;

import com.example.finalprojectvirtualteacher.models.Assignment;
import com.example.finalprojectvirtualteacher.models.Lecture;
import com.example.finalprojectvirtualteacher.models.Note;

import java.util.List;

public interface LectureRepository {



    List<Lecture> getAll();
    Lecture getById(int id);

    Lecture create(Lecture lecture);

    void delete(int id);

    Lecture update(Lecture updatedLecture);

    Note createNote(Note note1);

    Note getNote(int lectureId, int userId);

    Note updateNote(Note note);

    Lecture submitAssignment(Assignment assignment);
}
