package com.example.finalprojectvirtualteacher.services;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.exceptions.FileUploadException;
import com.example.finalprojectvirtualteacher.helpers.AssignmentsHelper;
import com.example.finalprojectvirtualteacher.models.Assignment;
import com.example.finalprojectvirtualteacher.models.Lecture;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.repositories.contracts.AssignmentRepository;
import com.example.finalprojectvirtualteacher.services.contacts.AssignmentService;
import com.example.finalprojectvirtualteacher.services.contacts.GradeService;
import com.example.finalprojectvirtualteacher.services.contacts.LectureService;
import com.example.finalprojectvirtualteacher.services.contacts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@Service
public class AssignmentServiceImpl implements AssignmentService {
    public static final String FILE_UPLOAD_ERROR = "File can't be uploaded.";
    public static final String PERMISSION_ERROR = "You dont have permission.";
    private final LectureService lectureService;
    private final AssignmentsHelper assignmentsHelper;
    private final AssignmentRepository assignmentRepository;
    private final GradeService gradeService;
    private final UserService userService;

    @Autowired
    public AssignmentServiceImpl(LectureService lectureService, AssignmentsHelper assignmentsHelper, AssignmentRepository assignmentRepository, GradeService gradeService, UserService userService) {
        this.lectureService = lectureService;
        this.assignmentsHelper = assignmentsHelper;
        this.assignmentRepository = assignmentRepository;
        this.gradeService = gradeService;
        this.userService = userService;
    }
    @Override
    public List<Assignment> getAll() {
        return assignmentRepository.getAll();
    }

    @Override
    public List<Assignment> getByTeacherForGrade(int teacherId) {
        User user=userService.getById(teacherId);
        if (user.getRole().getId()==1){
            throw new AuthorizationException(PERMISSION_ERROR);
        }
        return assignmentRepository.getByTeacherForGrade(teacherId);
    }

    @Override
    public Assignment getById(int assignmentId) {
        return assignmentRepository.getById(assignmentId);
    }

    @Override
    public Lecture submitAssignment(User user, int lectureId, MultipartFile multipartFile) {
        try {
            Lecture lecture = lectureService.getById(lectureId);
            String assignmentUrl = assignmentsHelper.uploadAssignment(multipartFile);
            Assignment assignment = new Assignment();
            assignment.setAssignmentUrl(assignmentUrl);
            assignment.setUser(user);
            assignment.setLecture(lecture);
            assignment.setGrade(gradeService.getById(1));
            return assignmentRepository.submitAssignment(assignment);
        } catch (IOException e) {
            throw new FileUploadException(FILE_UPLOAD_ERROR, e);
        }
    }

    @Override
    public Assignment grade(int assignmentId, int gradeId) {
        Assignment assignment=getById(assignmentId);
        assignment.setGrade(gradeService.getById(gradeId));
        return assignmentRepository.grade(assignment);
    }
}
