package com.example.finalprojectvirtualteacher.service;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.helpers.LectureMapper;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.Lecture;
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
import java.util.List;

import static com.example.finalprojectvirtualteacher.Helpers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LectureServiceImplTests {

    @Mock
    LectureRepository lectureRepository;

    @InjectMocks
    LectureServiceImpl lectureService;
    @Mock
    LectureMapper mapper;


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

//    @Test
//    void create_Should_CallRepository_When_CreateLecture() {
//        LectureDto lectureDto = createLectureDto();
//        User creator = createMockUser();
//        Lecture lecture = createMockLecture();
//
//        when(mapper.fromDto(lectureDto, creator)).thenReturn(lecture);
//        when(lectureRepository.create(lecture)).thenReturn(lecture);
//
//        lectureService.create(lectureDto,creator);
//        verify(mapper, times(1)).fromDto(lectureDto, creator);
//        verify(lectureRepository, times(1)).create(lecture);
//
//    }
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
//    @Test
//    void update_Should_ThrowException_WhenDontHavePermission() {
//        LectureDto lectureDto = createLectureDto();
//        User user = createMockUser();
//        user.setId(5);
//        User user1 = createMockTeacher();
//        Lecture lecture = createMockLecture();
//        lecture.setTeacher(user1);
//
//        Assertions.assertThrows(AuthorizationException.class, () -> lectureService.update(lectureDto, user, lecture.getId()));
//    }
//
//    @Test
//    void delete_Should_CallRepository_WhenDeleteLecture() {
//        int lectureId = 1;
//        User admin = createMockAdmin();
//        Lecture lecture = createMockLecture();
//
//        when(lectureRepository.getById(lectureId)).thenReturn(lecture);
//
//        lectureService.delete(lectureId, admin);
//        verify(lectureRepository, times(1)).delete(lectureId);
//
//
//    }


}
