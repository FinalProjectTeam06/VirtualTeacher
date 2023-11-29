package com.example.finalprojectvirtualteacher.controllers.mvc;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.helpers.AssignmentsHelper;
import com.example.finalprojectvirtualteacher.helpers.AuthenticationHelper;
import com.example.finalprojectvirtualteacher.helpers.LectureMapper;
import com.example.finalprojectvirtualteacher.models.*;
import com.example.finalprojectvirtualteacher.models.dto.CourseDto;
import com.example.finalprojectvirtualteacher.models.dto.FilterOptionsDto;
import com.example.finalprojectvirtualteacher.models.dto.LectureDto;
import com.example.finalprojectvirtualteacher.services.contacts.CourseService;
import com.example.finalprojectvirtualteacher.services.contacts.LectureService;
import com.example.finalprojectvirtualteacher.services.contacts.TopicService;
import com.example.finalprojectvirtualteacher.services.contacts.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseMvcController {
    private final CourseService courseService;
    private final UserService userService;
    private final TopicService topicService;
    private final AuthenticationHelper authenticationHelper;
    private final LectureService lectureService;
    private final LectureMapper lectureMapper;
    private final AssignmentsHelper assignmentsHelper;

    public CourseMvcController(CourseService courseService, UserService userService, TopicService topicService, AuthenticationHelper authenticationHelper, LectureService lectureService, LectureMapper lectureMapper, AssignmentsHelper assignmentsHelper) {
        this.courseService = courseService;
        this.userService = userService;
        this.topicService = topicService;
        this.authenticationHelper = authenticationHelper;
        this.lectureService = lectureService;
        this.lectureMapper = lectureMapper;
        this.assignmentsHelper = assignmentsHelper;
    }

    @ModelAttribute("topics")
    public List<Topic> populateCategories() {
        return topicService.getAll();
    }

    @ModelAttribute("isTeacher")
    public boolean populateIsTeacher(HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            return (user.getRole().getId() == 2 || user.getRole().getId() == 3);
        } catch (AuthorizationException e) {
            return false;
        }
    }

    @GetMapping("/topic/{topicId}")
    public String getAllCoursesFromTopic(@ModelAttribute("filter") FilterOptionsDto filterOptionsDto, Model model, @PathVariable int topicId) {
        FilterOptions filterOptions = new FilterOptions(
                filterOptionsDto.getTitle(),
                topicId,
                filterOptionsDto.getTeacherId(),
                filterOptionsDto.getRating(),
                filterOptionsDto.getSortBy(),
                filterOptionsDto.getSortOrder());
        List<User> teachers = userService.getAllTeachers();
        List<Course> courses = courseService.getAll(filterOptions);
        model.addAttribute("topicName", topicService.getById(topicId).getName());
        model.addAttribute("topicId", topicId);
        model.addAttribute("courses", courses);
        model.addAttribute("teachers", teachers);
        model.addAttribute("filterOptionsDto", filterOptionsDto);
        return "ViewCourseCategories";
    }

    @GetMapping("/enrolled")
    public String showEnrolledCourses(HttpSession httpSession, Model model) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            model.addAttribute("enrolledCourses", user.getCourses());
            model.addAttribute("loggedIn", user);
            model.addAttribute("enrolledCourses", courseService.getAllByUserNotCompleted(user.getId()));
            model.addAttribute("completedCourses", courseService.getAllByUserCompleted(user.getId()));
            return "UserEnrolledCoursesView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/create")
    public String createCourseView(HttpSession httpSession, Model model) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            model.addAttribute("loggedIn", user);
            model.addAttribute("courseDto", new CourseDto());
            return "CreateCourseView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/create")
    public String createCourse(@ModelAttribute("courseDto") CourseDto courseDto, HttpSession httpSession, Model model) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            Course course = courseService.create(courseDto, user);
            model.addAttribute("loggedIn", user);
            return "redirect:/courses/" + course.getId();
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{courseId}")
    public String viewCourseDetails(HttpSession httpSession, Model model, @PathVariable int courseId) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            Course course = courseService.getById(courseId);
            FilterOptionsDto filterOptionsDto=new FilterOptionsDto();
            filterOptionsDto.setTeacherId(course.getCreator().getId());
            FilterOptions filterOptions=new FilterOptions(filterOptionsDto.getTitle(),
                    filterOptionsDto.getTopicId(),
                    filterOptionsDto.getTeacherId(),
                    filterOptionsDto.getRating(),
                    filterOptionsDto.getSortBy(),
                    filterOptionsDto.getSortBy());
            List<Course> teacherCourses=courseService.getAll(filterOptions);
            model.addAttribute("teacherCourses", teacherCourses);
            model.addAttribute("course", course);
            model.addAttribute("loggedIn", user);
            model.addAttribute("lectures", course.getLectures());
            model.addAttribute("lectureDto", new LectureDto());
            return "CourseDetailsView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/{courseId}/lecture")
    public String addLectureToCourse(@Valid @ModelAttribute("lectureDto") LectureDto lectureDto,
                                     BindingResult bindingResult,
                                     @PathVariable int courseId,
                                     HttpSession httpSession, @RequestParam MultipartFile file) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            String url = assignmentsHelper.uploadAssignment(file);
            lectureDto.setCourseId(courseId);
            lectureDto.setAssignmentUrl(url);
            lectureService.create(lectureDto, user);
            return "redirect:/courses/" + courseId;
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (IOException e) {
            return "Error404";
        }
    }

    @GetMapping("/{courseId}/lecture/{lectureId}")
    public String showLectureView(HttpSession httpSession, Model model, @PathVariable int courseId, @PathVariable int lectureId) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            Course course = courseService.getById(courseId);
            Lecture lecture = lectureService.getById(lectureId);

            model.addAttribute("videoUrl", lecture.getVideoUrl());
            model.addAttribute("course", course);
            model.addAttribute("lecture", lecture);
            return "LectureView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }
}
