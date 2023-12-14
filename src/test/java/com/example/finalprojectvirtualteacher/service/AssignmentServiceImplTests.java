package com.example.finalprojectvirtualteacher.service;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.exceptions.FileUploadException;
import com.example.finalprojectvirtualteacher.helpers.AssignmentsHelper;
import com.example.finalprojectvirtualteacher.models.*;
import com.example.finalprojectvirtualteacher.repositories.contracts.AssignmentRepository;
import com.example.finalprojectvirtualteacher.repositories.contracts.CourseRepository;
import com.example.finalprojectvirtualteacher.services.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.finalprojectvirtualteacher.Helpers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssignmentServiceImplTests {

    @Mock
    AssignmentRepository assignmentRepository;

    @InjectMocks
    AssignmentServiceImpl assignmentService;

    @Mock
    UserServiceImpl userService;
    @Mock
    CourseRepository courseRepository;
    @Mock
    LectureServiceImpl lectureService;
    @Mock
    AssignmentsHelper assignmentsHelper;
    @Mock
    GradeServiceImpl gradeService;
    @Mock
    CourseServiceImpl courseService;


    @Test
    void getAll_Should_CallRepository() {
        Assignment assignment = createMockAssignment();
        Assignment assignment1 = createMockAssignment();

        List<Assignment> getAll = new ArrayList<>();
        getAll.add(assignment1);
        getAll.add(assignment);

        when(assignmentRepository.getAll()).thenReturn(getAll);

        List<Assignment> result = assignmentService.getAll();

        assertEquals(result, getAll);
        verify(assignmentRepository, Mockito.times(1)).getAll();

    }

    @Test
    void getByTeacherForGrade_Should_CallRepository() {
        User teacher = createMockTeacher();
        int teacherId = teacher.getId();
        Assignment assignment = createMockAssignment();
        assignment.setUser(teacher);


        when(userService.getById(teacher.getId())).thenReturn(teacher);
        when(assignmentService.getByTeacherForGrade(teacher.getId()))
                .thenReturn(Arrays.asList(assignment));

        List<Assignment> result = assignmentService.getByTeacherForGrade(teacher.getId());

        assertNotNull(result);
    }

    @Test
    void getByTeacherForGrade_Should_Throw_Exception() {
        User user = createMockUser();

        when(userService.getById(user.getId())).thenReturn(user);

        assertThrows(AuthorizationException.class,
                () -> assignmentService.getByTeacherForGrade(user.getId()));
    }

    @Test
    void getByUserSubmitted_Should_CallRepository() {
        User user = createMockUser();
        List<Assignment> list = new ArrayList<>();
        list.add(createMockAssignment());
        list.add(createMockAssignment());

        when(assignmentRepository.getByUserSubmitted(user.getId())).thenReturn(list);

        List<Assignment> result = assignmentService.getByUserSubmitted(user.getId());

        assertEquals(result, list);
        verify(assignmentRepository, times(1)).getByUserSubmitted(user.getId());

    }

    @Test
    void getAllAssignmentsForCourse_Should_CallRepository() {
        Course course = createMockCourse();
        List<Assignment> list = new ArrayList<>();
        list.add(createMockAssignment());
        list.add(createMockAssignment());

        when(assignmentRepository.getAllAssignmentsForCourse(course.getId())).thenReturn(list);

        List<Assignment> result = assignmentService.getAllAssignmentsForCourse(course.getId());

        assertEquals(result, list);
        verify(assignmentRepository, times(1)).getAllAssignmentsForCourse(course.getId());

    }

    @Test
    void getById_Should_CallRepository() {
        Assignment assignment = createMockAssignment();

        when(assignmentRepository.getById(assignment.getId())).thenReturn(assignment);

        assignmentService.getById(assignment.getId());
        verify(assignmentRepository, times(1)).getById(assignment.getId());
    }

    @Test
    void deleteAllAssignmentsSubmissionsFromCourse() {
        // Call the service method
        assignmentService.deleteAllAssignmentsSubmissionsFromCourse(1);

        // Verify that assignmentRepository method was called
        verify(assignmentRepository, times(1)).deleteAllAssignmentsSubmissionsFromCourse(anyInt());
    }

    @Test
    public void SubmitAssignment_Should_CallRepository() throws IOException, FileUploadException {

        User mockUser = createMockUser();
        MultipartFile mockFile = mock(MultipartFile.class);
        Lecture mockLecture = createMockLecture();
        Grade mockGrade = createMockGrade();
        Assignment assignment = new Assignment();
        assignment.setGrade(mockGrade);
        assignment.setLecture(mockLecture);
        assignment.setGrade(mockGrade);

        when(assignmentRepository.getByUserSubmittedToLecture(mockUser.getId(), mockLecture.getId()))
                .thenThrow(new EntityNotFoundException("Assignment not found"));

        when(lectureService.getById(mockLecture.getId())).thenReturn(mockLecture);
        when(assignmentsHelper.uploadAssignment(eq(mockFile))).thenReturn("mockAssignmentUrl");
        when(gradeService.getById(mockGrade.getId())).thenReturn(mockGrade);

        when(assignmentRepository.submitAssignment(assignment)).thenReturn(mockLecture);

        Lecture result = assignmentService.submitAssignment(mockUser, mockLecture.getId(), mockFile);

        assertNotNull(result);

    }


    @Test
    void getByUserSubmittedToCourse_Should_CallRepository() {
        User user = createMockUser();
        Course course = createMockCourse();
        Assignment assignment = createMockAssignment();
        assignment.setUser(user);

        List<Assignment> list = new ArrayList<>();
        list.add(assignment);


        when(assignmentRepository.getByUserSubmittedToCourseAndGraded(user.getId(), course.getId())).thenReturn(list);

        assignmentService.getByUserSubmittedToCourse(user.getId(), course.getId());

        verify(assignmentRepository, times(1))
                .getByUserSubmittedToCourseAndGraded(user.getId(), course.getId());

    }


    @Test
    void grade_Should_getGrade_ForSuccessAssignment() {
        Assignment assignment = createMockAssignment();
        Grade grade = createMockGrade();
        assignment.setGrade(grade);
        Course course = createMockCourse();
        User user = createMockUser();


        when(assignmentService.getById(assignment.getId())).thenReturn(assignment);
        when(gradeService.getById(grade.getId())).thenReturn(grade);

        when(assignmentRepository.grade(assignment)).thenReturn(assignment);

        when(assignmentService.getAllAssignmentsForCourse(course.getId()))
                .thenReturn(List.of(assignment));

        Assignment result = assignmentService.grade(assignment.getId(), grade.getId(), course.getId(), user.getId());

        assertNotNull(result);
        assertEquals(result, assignment);

    }

    @Test
    void deleteAllAssignmentsSubmissionsFromCourse_Should_CallRepository() {
        int courseId = 1;

        assignmentService.deleteAllAssignmentsSubmissionsFromCourse(courseId);

        verify(assignmentRepository, times(1)).deleteAllAssignmentsSubmissionsFromCourse(courseId);
    }

    @Test
    void grade_Should_Throw_EntityNotFoundException_When_Assignment_Not_Found() {
        int assignmentId = 1;
        int gradeId = 2;
        int courseId = 3;
        int studentId = 4;

        when(assignmentService.getById(assignmentId))
                .thenThrow(new EntityNotFoundException("Assignment not found"));

        assertThrows(EntityNotFoundException.class,
                () -> assignmentService.grade(assignmentId, gradeId, courseId, studentId));
    }

    @Test
    void getByUserSubmittedToCourse() {
        // Mock the behavior of the assignmentRepository
        when(assignmentRepository.getByUserSubmittedToCourseAndGraded(anyInt(), anyInt())).thenReturn(new ArrayList<>());

        // Call the service method
        List<Assignment> assignments = assignmentService.getByUserSubmittedToCourse(1, 1);

        // Assertions
        assertNotNull(assignments);

        // Verify that the repository method was called
        verify(assignmentRepository, times(1)).getByUserSubmittedToCourseAndGraded(anyInt(), anyInt());
    }

}
