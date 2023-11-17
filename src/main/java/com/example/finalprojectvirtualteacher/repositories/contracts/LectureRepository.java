package com.example.finalprojectvirtualteacher.repositories.contracts;

import com.example.finalprojectvirtualteacher.models.Lecture;

import java.util.List;

public interface LectureRepository {



    List<Lecture> getAll();
    Lecture getById(int id);

    Lecture create(Lecture lecture);

    void delete(int id);

    Lecture update(Lecture updatedLecture);

}
