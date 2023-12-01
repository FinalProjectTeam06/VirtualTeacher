package com.example.finalprojectvirtualteacher.repositories.contracts;

import com.example.finalprojectvirtualteacher.models.Assignment;
import com.example.finalprojectvirtualteacher.models.Lecture;

import java.util.List;

public interface AssignmentRepository {
    Lecture submitAssignment(Assignment assignment);
    List<Assignment> getByTeacherForGrade(int teacherId);

    List<Assignment> getAll();

    Assignment getById(int assignmentId);

    Assignment grade(Assignment assignment);
}
