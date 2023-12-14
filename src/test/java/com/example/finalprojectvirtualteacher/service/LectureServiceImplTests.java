package com.example.finalprojectvirtualteacher.service;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.helpers.AssignmentsHelper;
import com.example.finalprojectvirtualteacher.helpers.mappers.LectureMapper;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.Lecture;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.dto.LectureDto;
import com.example.finalprojectvirtualteacher.repositories.contracts.CourseRepository;
import com.example.finalprojectvirtualteacher.repositories.contracts.LectureRepository;
import com.example.finalprojectvirtualteacher.services.CourseServiceImpl;
import com.example.finalprojectvirtualteacher.services.LectureServiceImpl;
import com.example.finalprojectvirtualteacher.services.contacts.AssignmentService;
import com.example.finalprojectvirtualteacher.services.contacts.CommentService;
import com.example.finalprojectvirtualteacher.services.contacts.NoteService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.example.finalprojectvirtualteacher.Helpers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LectureServiceImplTests {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private AssignmentService assignmentService;

    @Mock
    private NoteService noteService;

    @Mock
    private CommentService commentService;

    @Mock
    LectureRepository lectureRepository;

    @InjectMocks
    LectureServiceImpl lectureService;
    @InjectMocks
    private CourseServiceImpl courseService;

    @Mock
    LectureMapper mapper;
    @Mock
    AuthorizationException authorizationException;


    @Test
    void getAll_Should_CallRepository_When_GetAllLectures() {
        List<Lecture> lectureList = new ArrayList<>();
        lectureList.add(createMockLecture());
        lectureList.add(createMockLecture());

        when(lectureRepository.getAll()).thenReturn(lectureList);

        List<Lecture> result = lectureService.getAll();

        assertEquals(result, lectureList);
        verify(lectureRepository, times(1)).getAll();
    }

    @Test
    void getById_Should_CallRepository_GetLecture() {
        Lecture lecture = createMockLecture();
        int id = lecture.getId();

        when(lectureRepository.getById(id)).thenReturn(lecture);

        Lecture result = lectureService.getById(id);
        assertEquals(result, lecture);

        verify(lectureRepository, times(1)).getById(id);
    }

    @Test
    void getLecture_Should_CallRepository_When_GetByCourseId() {
        Course course = createMockCourse();
        int courseId = course.getId();
        List<Lecture> lectureList = new ArrayList<>();
        lectureList.add(createMockLecture());
        lectureList.add(createMockLecture());

        when(lectureRepository.lecturesByCourseId(courseId)).thenReturn(lectureList);

        List<Lecture> result = lectureService.getByCourseId(courseId);

        assertEquals(result, lectureList);
        verify(lectureRepository, times(1)).lecturesByCourseId(courseId);
    }

    @Test
    void getLecture_Should_ThrowException_When_GetByCourseId() {
        Course course = createMockCourse();
        int courseId = course.getId();

        when(lectureRepository.lecturesByCourseId(courseId)).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class, () -> lectureService.getByCourseId(courseId));
        verify(lectureRepository, times(1)).lecturesByCourseId(courseId);

    }

    @Test
    void create_Should_CallRepository_When_CreateLecture() {
        LectureDto lectureDto = createLectureDto();
        User creator = createMockAdmin();
        Lecture lecture = createMockLecture();
        lecture.setTeacher(creator);

        when(mapper.fromDto(lectureDto, creator)).thenReturn(lecture);
        when(lectureRepository.create(lecture)).thenReturn(lecture);

       lectureService.create(lectureDto, creator);


    }


    @Test
    public void deleteAllLecturesByUser_Should_CallRepositoryDeleteAllLecturesByUser() {
        // Arrange
        int userId = 1;

        // Act
        lectureService.deleteAllLecturesByUser(userId);

        // Assert
        // Verify that the deleteAllLecturesByUser method is called on the repository
        verify(lectureRepository, times(1)).deleteAllLecturesByUser(userId);

        // Make sure there are no other interactions with the lectureRepository
        verifyNoMoreInteractions(lectureRepository);
    }

    @Test
    public void deleteAllLecturesFromCourse_Should_CallRepositoryDeleteAllLecturesFromCourse() {
        // Arrange
        int courseId = 1;

        // Act
        lectureService.deleteAllLecturesFromCourse(courseId);

        // Assert
        // Verify that the deleteAllLecturesFromCourse method is called on the repository
        verify(lectureRepository, times(1)).deleteAllLecturesFromCourse(courseId);

        // Make sure there are no other interactions with the lectureRepository
        verifyNoMoreInteractions(lectureRepository);
    }
    @Test
    void deleteAllLecturesByUser_Should_CallRepository_When_DeleteAllLecturesByUser() {
        // Arrange
        int userId = 1;

        // Act
        lectureService.deleteAllLecturesByUser(userId);

        // Verify interactions with dependencies
        verify(lectureRepository).deleteAllLecturesByUser(userId);
    }


    @Test
    void update_Should_ThrowException_WhenDontHavePermission() {
        LectureDto lectureDto = createLectureDto();
        User user = createMockUser();
        int lectureId = 1;

        User nonOwnerUser = createMockUser();
        nonOwnerUser.setId(5);

        Lecture lecture = createMockLecture();
        lecture.setTeacher(createMockTeacher());

        when(lectureRepository.getById(lectureId)).thenReturn(lecture);

        Assertions.assertThrows(AuthorizationException.class,
                () -> lectureService.update(lectureDto, user, lectureId));

    }


    @Test
    void deleteAllLecturesFromCourse_Should_CallRepository_When_DeleteAllLecturesFromCourse() {
        // Arrange
        int courseId = 1;

        // Act
        lectureService.deleteAllLecturesFromCourse(courseId);

        // Verify interactions with dependencies
        verify(lectureRepository).deleteAllLecturesFromCourse(courseId);
    }


}
