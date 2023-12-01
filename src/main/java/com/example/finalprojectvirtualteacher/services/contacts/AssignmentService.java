package com.example.finalprojectvirtualteacher.services.contacts;

import com.example.finalprojectvirtualteacher.models.Assignment;
import com.example.finalprojectvirtualteacher.models.Lecture;
import com.example.finalprojectvirtualteacher.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AssignmentService {
    List<Assignment> getAll();
    List<Assignment> getByTeacherForGrade(int teacherId);
    Assignment getById(int assignmentId);
    Lecture submitAssignment(User user, int lectureId, MultipartFile multipartFile);

    Assignment grade(int assignmentId, int gradeId);

}
