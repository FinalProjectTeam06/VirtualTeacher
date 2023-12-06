package com.example.finalprojectvirtualteacher.service;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.helpers.AssignmentsHelper;
import com.example.finalprojectvirtualteacher.helpers.mappers.LectureMapper;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.Lecture;
import com.example.finalprojectvirtualteacher.models.Note;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.dto.LectureDto;
import com.example.finalprojectvirtualteacher.repositories.contracts.LectureRepository;
import com.example.finalprojectvirtualteacher.services.LectureServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.finalprojectvirtualteacher.Helpers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LectureServiceImplTests {

    @Mock
    LectureRepository lectureRepository;

    @InjectMocks
    LectureServiceImpl lectureService;
    @Mock
    LectureMapper mapper;
    @Mock
    AssignmentsHelper assignmentsHelper;


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
        List<Lecture> lectureList = new ArrayList<>();

        when(lectureRepository.lecturesByCourseId(courseId)).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class, () -> lectureService.getByCourseId(courseId));
        verify(lectureRepository, times(1)).lecturesByCourseId(courseId);

    }

    @Test
    void create_Should_CallRepository_When_CreateLecture() {
        LectureDto lectureDto = createLectureDto();
        User creator = createMockTeacher();
        Lecture lecture = createMockLecture();
        lecture.setTeacher(creator);

        when(mapper.fromDto(lectureDto, creator)).thenReturn(lecture);
        when(lectureRepository.create(lecture)).thenReturn(lecture);

        Lecture result = lectureService.create(lectureDto, creator);

        verify(mapper, times(1)).fromDto(lectureDto, creator);
        verify(lectureRepository, times(1)).create(lecture);

        assertEquals(result, lecture);
    }

    //    //todo- da se napravi test za greshka
//
//    @Test
//    void update_Should_CallRepository_WhenCreatorUpdate() {
//        LectureDto lectureDto = createLectureDto();
//        User creator = createMockTeacher();
//        Lecture lecture = createMockLecture();
//        lecture.setTeacher(creator);
//
//        when(mapper.fromDtoUpdate(lectureDto, lecture)).thenReturn(lecture);
//
//        lectureService.update(lectureDto, creator, lecture.getId());
//
//        verify(mapper, times(1)).fromDtoUpdate(lectureDto, lecture);
//        verify(lectureRepository, times(1)).update(lecture);
//    }
//
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
    void delete_Should_CallRepository_WhenDeleteLecture() {
        Lecture lecture = createMockLecture();
        lecture.setTeacher(createMockTeacher());
        int lectureId = lecture.getId();
        User admin = createMockAdmin();

        doNothing().when(lectureRepository).delete(lectureId);

        lectureService.delete(lectureId, admin);
        verify(lectureRepository, times(1)).delete(lectureId);
    }

//    @Test
//    void getNote_Should_CallRepository_When_GetNote() {
//        Lecture lecture = createMockLecture();
//        Note note = createNote();
//        User user = createMockUser();
//        when(lectureRepository.getNote(lecture.getId(), user.getId())).thenReturn(note);
//
//        lectureService.getNote(lecture.getId(), user.getId());
//
//        verify(lectureRepository, times(1)).getNote(lecture.getId(), user.getId());
//    }
//@Test
//    v   Note existingNote = createNote();
//
//    when(lectureRepository.getById(lectureId)).thenReturn(createMockLecture());
//    when(lectureRepository.getNote(lectureId, user.getId()))
//            .thenReturn(existingNote);
//
//        lectureService.createNote(lectureId,user,"UpdatedText");
//
//    verify(lectureRepository,times(1)).updateNote(existingNote);
//
//}oid createNote_Should_CallRepository_WhenUserIsEnrolledAndExistingNoteExists(){
//        int lectureId = 1;
//        User user =createMockUser();
//
//
//    @Test
//    void createNote_When_UserIsNotEnrolled(){
//        int lectureId = 1;
//        User user = createMockUser();
//        user.setCourses(Collections.emptySet());
//
//        assertThrows(EntityNotFoundException.class,()->lectureService.createNote(lectureId,user,"Text"));
//    }

    //todo
    @Test
    void submitAssignment_Should_CallRepository_When_IsSuccessfully(){
        int lectureId =1;
        User user =createMockUser();
        Lecture lecture = createMockLecture();
        String assignment = "stringAssignment";



        when(lectureRepository.getById(lectureId)).thenReturn(lecture);


    }

}
