package com.example.finalprojectvirtualteacher.service;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.helpers.mappers.CourseMapper;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.FilterOptions;
import com.example.finalprojectvirtualteacher.models.Rate;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.dto.CourseDto;
import com.example.finalprojectvirtualteacher.models.dto.RateDto;
import com.example.finalprojectvirtualteacher.repositories.contracts.CourseRepository;
import com.example.finalprojectvirtualteacher.services.CourseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.example.finalprojectvirtualteacher.Helpers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceImplTests {

    @Mock
    private CourseRepository courseRepository;
    @InjectMocks
    private CourseServiceImpl courseService;
    @Mock
    private CourseMapper mapper;


    @Test
    void getAll_Should_CallRepository_With_FilterOptions() {
        FilterOptions filterOptions = createMockFilterOptions();
        when(courseRepository.getAll(filterOptions))
                .thenReturn(null);

        courseService.getAll(filterOptions);

        Mockito.verify(courseRepository, Mockito.times(1))
                .getAll(filterOptions);
    }

    @Test
    void getAll_Should_CallRepository() {
        Course course = createMockCourse();
        Course course1 = createMockCourse();

        List<Course> getAll = new ArrayList<>();
        getAll.add(course);
        getAll.add(course1);

        when(courseRepository.getAll()).thenReturn(getAll);

        List<Course> result = courseService.getAll();

        assertEquals(result, getAll);
        verify(courseRepository,times(1)).getAll();
    }
    @Test
    void getAllByUserCompleted_Should_CallRepository(){
        User user = createMockUser();
        List<Course> courseList = new ArrayList<>();
        courseList.add(createMockCourse());
        courseList.add(createMockCourse());


        when(courseRepository.getAllByUserCompleted(user.getId())).thenReturn(courseList);

        List<Course>result = courseService.getAllByUserCompleted(user.getId());

        assertEquals(result,courseList);
        verify(courseRepository,times(1)).getAllByUserCompleted(user.getId());
    }
    @Test
    void getAllByUserNotCompleted_Should_CallRepository(){
        User user = createMockUser();
        List<Course> courseList = new ArrayList<>();
        courseList.add(createMockCourse());
        courseList.add(createMockCourse());


        when(courseRepository.getAllByUserNotCompleted(user.getId())).thenReturn(courseList);

        List<Course>result = courseService.getAllByUserNotCompleted(user.getId());

        assertEquals(result,courseList);
        verify(courseRepository,times(1)).getAllByUserNotCompleted(user.getId());
    }

    @Test
    void getById_Should_Return_Comment() {
        Course course = createMockCourse();

        when(courseRepository.getById(course.getId()))
                .thenReturn(course);

       Course result = courseService.getById(course.getId());

    assertEquals(result,course);
        verify(courseRepository, Mockito.times(1)).getById(course.getId());
    }


    @Test
    void GetAllEnrollments_WithNoCourses() {
        List<Course> courseList = new ArrayList<>();

        Mockito.when(courseService.getAll()).thenReturn(courseList);

        int enrollmentCount = courseService.getAllEnrollments();

        assertEquals(0, enrollmentCount);
        verify(courseRepository,times(1)).getAll();
    }

    @Test
    void testGetAllEnrollments_WithCoursesAndStudents() {
        Course course = createMockCourse();
        Course course1 = createMockCourse();
        User user = createMockUser();
        User user1 = createMockUser();

        course.addStudents(user);
        course1.addStudents(user1);

        List<Course> courses = new ArrayList<>();
        courses.add(course);
        courses.add(course1);

        Mockito.when(courseService.getAll()).thenReturn(courses);

        int enrollmentCount = courseService.getAllEnrollments();
        int expectedCount = 2; // Sum of students in each course

        assertEquals(expectedCount, enrollmentCount);
    }

    @Test
    void create_Should_CallRepository() {
        CourseDto courseDto = createCourseDto();
        User creator = createMockTeacher();
        Course course1 = createMockCourse();
        when(mapper.fromDtoIn(courseDto, creator)).thenReturn(course1);

        courseService.create(courseDto, creator);

        verify(mapper, times(1)).fromDtoIn(courseDto, creator);
        verify(courseRepository, times(1)).create(course1);
    }
    @Test
    void update_Should_CallRepository_When_UserIsAdmin() {
        CourseDto courseDto = createCourseDto();
        User user = createMockAdmin();
        int courseId = 123;
        Course existingCourse = createMockCourse();
        existingCourse.setId(courseId);
        existingCourse.setCreator(user);
        Course updatedCourse =createMockCourse();
        updatedCourse.setId(courseId);

        when(courseRepository.getById(courseId)).thenReturn(existingCourse);
        when(mapper.fromDtoUpdate(courseDto, existingCourse)).thenReturn(updatedCourse);
        when(courseRepository.update(updatedCourse)).thenReturn(updatedCourse);
        courseService.update(courseDto,user, courseId);

        verify(courseRepository,times(1)).update(updatedCourse);
        verify(mapper, times(1)).fromDtoUpdate(courseDto, existingCourse);

    }

    @Test
    void delete_Should_CallRepository_WhenHavePermission() {
        int courseId = 1;
        User admin = createMockAdmin();
        Course courseToDelete = createMockCourse();
        courseToDelete.setCreator(admin);

        when(courseRepository.getById(courseId)).thenReturn(courseToDelete);

        courseService.delete(courseId,admin);
        verify(courseRepository,times(1)).delete(courseToDelete);

    }
    @Test
    void delete_Should_ThrowException_WhenDontHavePermission() {
        User user = createMockUser();
        Course course = createMockCourse();


        when(courseRepository.getById(course.getId())).thenReturn(course);

      assertThrows(AuthorizationException.class,() -> courseService.delete(course.getId(),user));

      verify(courseRepository,never()).delete(course);
    }

    @Test
    void testCheckPermission_WithAdminRole_Success() {
        User adminUser = createMockAdmin();
        Course course = createMockCourse();
        course.setCreator(adminUser);


        assertDoesNotThrow(() -> courseService.checkPermission(course, adminUser));
    }

    @Test
    void rateCourse_Should_CallRepository_When_NewRate_Success() {
        User user = createMockUser();
        RateDto rateDto = createRateDto();

        Course course =createMockCourse();

        when(courseRepository.getById(course.getId())).thenReturn(course);
        when(courseRepository.getRating(course.getId(), user.getId())).thenThrow(EntityNotFoundException.class);
        when(courseRepository.rateCourse(any(Rate.class))).thenReturn(course);

        courseService.rateCourse(course.getId(),user,rateDto);

        verify(courseRepository,times(1)).update(course);
        verify(courseRepository,times(1)).rateCourse(any(Rate.class));
    }

    @Test
    void rateCourse_Should_CallRepository_When_ExcitingRatingUpdate_Success(){
        User user =createMockUser();
        RateDto rateDto =createRateDto();
        Course course =createMockCourse();
        Rate excistingRate =createMockRate();

        when(courseRepository.getById(course.getId())).thenReturn(course);
        when(courseRepository.getRating(course.getId(), user.getId())).thenReturn(excistingRate);
        when(courseRepository.updateRating(any(Rate.class))).thenReturn(course);

        courseService.rateCourse(course.getId(),user,rateDto);

        verify(courseRepository,times(1)).update(course);
        verify(courseRepository,times(1)).updateRating(any(Rate.class));

    }

}
