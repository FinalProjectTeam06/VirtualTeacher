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

    List<Assignment> getByUserSubmitted(int userId);
    List<Assignment> getByUserSubmittedToCourseAndGraded(int userId, int courseId);
    Assignment getByUserSubmittedToLecture(int userId, int lectureId);

    Lecture update(Assignment assignment);

    List<Assignment> getAllAssignmentsForCourse(int courseId);

    void deleteAssignmentsFromUserAndLecture(int userId);
}
