package com.example.finalprojectvirtualteacher.services;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.exceptions.FileUploadException;
import com.example.finalprojectvirtualteacher.helpers.AssignmentsHelper;
import com.example.finalprojectvirtualteacher.models.Assignment;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.Lecture;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.repositories.contracts.AssignmentRepository;
import com.example.finalprojectvirtualteacher.services.contacts.*;
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
    private final CourseService courseService;

    @Autowired
    public AssignmentServiceImpl(LectureService lectureService, AssignmentsHelper assignmentsHelper, AssignmentRepository assignmentRepository, GradeService gradeService, UserService userService, CourseService courseService) {
        this.lectureService = lectureService;
        this.assignmentsHelper = assignmentsHelper;
        this.assignmentRepository = assignmentRepository;
        this.gradeService = gradeService;
        this.userService = userService;
        this.courseService = courseService;
    }

    @Override
    public List<Assignment> getAll() {
        return assignmentRepository.getAll();
    }

    @Override
    public List<Assignment> getByTeacherForGrade(int teacherId) {
        User user = userService.getById(teacherId);
        if (user.getRole().getId() == 1) {
            throw new AuthorizationException(PERMISSION_ERROR);
        }
        return assignmentRepository.getByTeacherForGrade(teacherId);
    }

    @Override
    public List<Assignment> getByUserSubmitted(int userId) {
        return assignmentRepository.getByUserSubmitted(userId);
    }

    @Override
    public List<Assignment> getAllAssignmentsForCourse(int courseId) {
        return assignmentRepository.getAllAssignmentsForCourse(courseId);
    }

    @Override
    public Assignment getById(int assignmentId) {
        return assignmentRepository.getById(assignmentId);
    }

    @Override
    public Lecture submitAssignment(User user, int lectureId, MultipartFile multipartFile) {
        try {
            Assignment assignment = assignmentRepository.getByUserSubmittedToLecture(user.getId(), lectureId);
            return assignmentRepository.update(assignment);
        } catch (EntityNotFoundException e) {
            try {
                Assignment assignment = new Assignment();
                Lecture lecture = lectureService.getById(lectureId);
                String assignmentUrl = assignmentsHelper.uploadAssignment(multipartFile);
                assignment.setAssignmentUrl(assignmentUrl);
                assignment.setUser(user);
                assignment.setLecture(lecture);
                assignment.setGrade(gradeService.getById(1));
                return assignmentRepository.submitAssignment(assignment);
            } catch (IOException ex) {
                throw new FileUploadException(FILE_UPLOAD_ERROR, ex);
            }
        }
    }

    @Override
    public List<Assignment> getByUserSubmittedToCourse(int userId, int courseId) {
        return assignmentRepository.getByUserSubmittedToCourse(userId, courseId);
    }

    public double getGradeForCourse(int userId, int courseId) {
        Course course = courseService.getById(courseId);
        List<Assignment> submittedAssignments = assignmentRepository.getByUserSubmittedToCourse(userId, courseId);
        int submittedAssignmentsCount = submittedAssignments.size();
        int assignmentsToSubmit = course.getLectures().size();
        int sum = 0;
        for (Assignment submittedAssignment : submittedAssignments) {
            sum += submittedAssignment.getGrade().getId();
        }
        double resultCountGrades = (assignmentsToSubmit - submittedAssignmentsCount) * 2 + sum;
        return resultCountGrades / assignmentsToSubmit;
    }

    @Override
    public Assignment grade(int assignmentId, int gradeId, int courseId, int studentId) {
        Assignment assignment = getById(assignmentId);
        assignment.setGrade(gradeService.getById(gradeId));
        assignment = assignmentRepository.grade(assignment);

        List<Assignment> courseAssignments = getAllAssignmentsForCourse(courseId);
        List<Assignment> userSubmittedAssignments = getByUserSubmittedToCourse(studentId, courseId);
        if (courseAssignments.size() == userSubmittedAssignments.size()) {
            boolean isGraded = true;
            for (Assignment courseAssignment : userSubmittedAssignments) {
                if (courseAssignment.getGrade().getId() == 1) {
                    isGraded = false;
                }
            }
            if (isGraded) {
                userService.setEnrollmentCourseStatusToGraduated(studentId, courseId);
            }
        }
        return assignment;
    }
}
