package com.example.finalprojectvirtualteacher;

import com.example.finalprojectvirtualteacher.models.*;
import com.example.finalprojectvirtualteacher.models.dto.LectureDto;
import com.example.finalprojectvirtualteacher.models.dto.RateDto;
import com.example.finalprojectvirtualteacher.models.dto.UserDtoUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Helpers {

    public static User createMockAdmin() {
        User mockUser = createMockUser();
        mockUser.setId(2);
        mockUser.setRole(createMockAdminRole());
        return mockUser;
    }

    public static User createMockUserWithId(int id) {
        User user = createMockUser();
        user.setId(id);
        return user;
    }

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

    public static Comment createMockComment() {
        Comment mockComment = new Comment();
        mockComment.setId(1);
        mockComment.setContent("mockContent");
        return mockComment;
    }

    public static Role createMockAdminRole() {
        Role adminRole = new Role();
        adminRole.setId(1);
        adminRole.setName("admin");
        return adminRole;
    }



    public static Role createMockUserRole() {
        Role userRole = new Role();
        userRole.setId(2);
        userRole.setName("user");
        return userRole;
    }

    public static UserDtoUpdate createMockUserDtoUpdate() {
        UserDtoUpdate dto = new UserDtoUpdate();
        dto.setFirstName("UpdatedFirstName");
        dto.setLastName("UpdatedLastName");
        dto.setPassword("UpdatedPassword");
        return dto;
    }

    public static Course createMockCourse() {
        Course course = new Course();
        course.setId(1);
        course.setTitle("MockCourseTitle");
        course.setDescription("MockCourseDescription");
        course.setCreator(createMockUser());
        return course;
    }

    public static Lecture createMockLecture() {
        Lecture lecture = new Lecture();
        lecture.setId(1);
        lecture.setTitle("MockLectureTitle");
        lecture.setDescription("MockLectureDescription");
        lecture.setTeacher(createMockUser());
        lecture.setCourse(createMockCourse());
        return lecture;
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

    public static List<Role> createMockRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(createMockRole(1, "Role1"));
        roles.add(createMockRole(2, "Role2"));
        roles.add(createMockRole(3, "Role3"));
        return roles;
    }

    public static Role createMockRole(int id, String name) {
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        return role;
    }


    public static Lecture createMockLecture(int id, String title, String description, User teacher, Course course) {
        Lecture lecture = new Lecture();
        lecture.setId(id);
        lecture.setTitle(title);
        lecture.setDescription(description);
        lecture.setTeacher(teacher);
        lecture.setCourse(course);
        return lecture;
    }
    public static LectureDto createMockLectureDto(String title, String description, String videoUrl, String assignmentUrl, int courseId) {
        LectureDto dto = new LectureDto();
        dto.setTitle(title);
        dto.setDescription(description);
        dto.setVideoUrl(videoUrl);
        dto.setAssignmentUrl(assignmentUrl);
        dto.setCourseId(courseId);
        return dto;
    }

    public static UserDtoUpdate createMockUserDtoUpdate(String firstName, String lastName, String password) {
        UserDtoUpdate dto = new UserDtoUpdate();
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setPassword(password);
        return dto;
    }



    public static Course createMockCourse(int id, String title, String description, User creator) {
        Course course = new Course();
        course.setId(id);
        course.setTitle(title);
        course.setDescription(description);
        course.setCreator(creator);
        return course;
    }








}