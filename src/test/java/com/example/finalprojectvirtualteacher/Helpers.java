package com.example.finalprojectvirtualteacher;

import com.example.finalprojectvirtualteacher.models.*;
import com.example.finalprojectvirtualteacher.models.dto.CourseDto;
import com.example.finalprojectvirtualteacher.models.dto.LectureDto;
import com.example.finalprojectvirtualteacher.models.dto.RateDto;
import com.example.finalprojectvirtualteacher.models.dto.UserDtoUpdate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Helpers {
    public static User createMockUser() {
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setFirstName("MockFirstName");
        mockUser.setLastName("MockLastName");
        mockUser.setEmail("mock@user.com");
        mockUser.setPassword("MockPassword");
        mockUser.setRole(createMockUserRole());
        mockUser.setCourses(new HashSet<>());
        return mockUser;
    }

    public static User createMockTeacher() {
        User mockUser = new User();
        mockUser.setId(2);
        mockUser.setFirstName("MockFirstName");
        mockUser.setLastName("MockLastName");
        mockUser.setEmail("mock@user.com");
        mockUser.setPassword("MockPassword");
        mockUser.setRole(createMockTeacherRole());
        mockUser.setCourses(new HashSet<>());
        return mockUser;
    }

    public static User createMockAdmin() {
        User mockUser = createMockUser();
        mockUser.setRole(createMockAdminRole());
        return mockUser;
    }

    private static Role createMockAdminRole() {
        Role adminRole = new Role();
        adminRole.setId(1);
        adminRole.setName("admin");
        return adminRole;
    }

    private static Role createMockTeacherRole() {
        Role teacher = new Role();
        teacher.setId(2);
        teacher.setName("teacher");
        return teacher;
    }

    public static User createMockUserWithId(int id) {
        User user = createMockUser();
        user.setId(id);
        return user;
    }

    public static Role createMockUserRole() {
        Role userRole = new Role();
        userRole.setId(1);
        userRole.setName("student");
        return userRole;
    }

    public static Course createMockCourse() {
        Course course = new Course();
        course.setId(1);
        course.setTitle("MockCourseTitle");
        course.setDescription("MockCourseDescription");
        course.setCreator(createMockTeacher());
        course.setStudents(new HashSet<>());
        return course;
    }

    public static CourseDto createCourseDto() {
        CourseDto dto = new CourseDto();
        dto.setTitle("Title");
        dto.setDescription("Description");
        dto.setMinGrade(1);
        dto.setStartDate(LocalDate.now());
        dto.setTopicId(1);
        return dto;
    }

    public static Comment createMockComment() {
        Comment mockComment = new Comment();
        mockComment.setId(1);
        mockComment.setContent("mockContent");
        return mockComment;
    }

    public static Lecture createMockLecture() {
        Lecture lecture = new Lecture();
        lecture.setId(1);
        lecture.setTitle("MockLectureTitle");
        lecture.setDescription("MockLectureDescription");
        lecture.setTeacher(createMockTeacher());
        lecture.setCourse(createMockCourse());
        return lecture;
    }
    public static LectureDto createLectureDto(){
        LectureDto lectureDto = new LectureDto();
        lectureDto.setDescription("MockDescription");
        lectureDto.setTitle("MockTitle");
        lectureDto.setAssignmentUrl("assignment");
        lectureDto.setCourseId(1);
        lectureDto.setVideoUrl("videoUrl");
        return lectureDto;
    }

    public static Rate createMockRate() {
        Rate rate = new Rate();
        rate.setComment("comment");
        rate.setRateValue(1);
        rate.setCourse(createMockCourse());
        rate.setUser(createMockUser());
        return rate;
    }

    public static RateDto createRateDto() {
        RateDto rateDto = new RateDto();
        rateDto.setId(1);
        rateDto.setRateValue(1);
        rateDto.setComment("comment");
        return rateDto;
    }

    public static List<Topic> createMockTopics() {
        List<Topic> topics = new ArrayList<>();
        return topics;
    }

    public static Topic createMockTopic(int id) {
        Topic topic = new Topic();
        topic.setId(id);
        return topic;
    }

    public static Role createMockRole(int id, String name) {
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        return role;
    }

    public static List<Role> createMockRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(createMockRole(1, "Role1"));
        roles.add(createMockRole(2, "Role2"));
        roles.add(createMockRole(3, "Role3"));
        return roles;
    }

    public static UserFilterOptions createUserFilterOptions(){
      return new UserFilterOptions(
                 "mockUsername",
                 "mockEmail",
                 "mockFirstname");
    }
    public static FilterOptions createMockFilterOptions() {
        return new FilterOptions(
                "mockCreatedBy",
                1,
                1,
                2.0,
                "",
                "");
    }

    public static UserDtoUpdate createMockUserDtoUpdate() {
        UserDtoUpdate dto = new UserDtoUpdate();
        dto.setFirstName("UpdatedFirstName");
        dto.setLastName("UpdatedLastName");
        dto.setPassword("UpdatedPassword");
        return dto;
    }
    public static Note createNote(){
        Note note = new Note();
        note.setLecture(createMockLecture());
        note.setText("MockTest");
        note.setNoteId(1);
        note.setUser(createMockUser());
        return note;
    }

}