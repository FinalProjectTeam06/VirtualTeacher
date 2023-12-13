package com.example.finalprojectvirtualteacher.controllers.mvc;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.helpers.AssignmentsHelper;
import com.example.finalprojectvirtualteacher.helpers.AuthenticationHelper;
import com.example.finalprojectvirtualteacher.models.*;
import com.example.finalprojectvirtualteacher.models.dto.*;
import com.example.finalprojectvirtualteacher.services.contacts.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private final AssignmentsHelper assignmentsHelper;
    private final AssignmentService assignmentService;
    private final EnrollmentService enrollmentService;
    private final WikiPageService wikiPageService;

    public CourseMvcController(CourseService courseService, UserService userService, TopicService topicService, AuthenticationHelper authenticationHelper, LectureService lectureService, AssignmentsHelper assignmentsHelper, AssignmentService assignmentService, EnrollmentService enrollmentService, WikiPageService wikiPageService) {
        this.courseService = courseService;
        this.userService = userService;
        this.topicService = topicService;
        this.authenticationHelper = authenticationHelper;
        this.lectureService = lectureService;
        this.assignmentsHelper = assignmentsHelper;
        this.assignmentService = assignmentService;
        this.enrollmentService = enrollmentService;
        this.wikiPageService = wikiPageService;
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

    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin(HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            return (user.getRole().getId() == 3);
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
            model.addAttribute("finishedCourses", enrollmentService.getAllFinished(user.getId()));
            model.addAttribute("activeCourses", courseService.getAllActiveCoursesNotEnrolled(user));
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
            return "redirect:/courses/prepare/" + course.getId();
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/prepare/{courseId}")
    public String viewCoursePrepare(HttpSession httpSession, Model model, @PathVariable int courseId) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            Course course = courseService.getById(courseId);

            model.addAttribute("course", course);
            model.addAttribute("loggedIn", user);
            model.addAttribute("lectures", course.getLectures());
            model.addAttribute("lectureDto", new LectureDto());
            return "CoursePrepareView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/prepare/{courseId}")
    public String prepareCourseAddLectures(@Valid @ModelAttribute("lectureDto") LectureDto lectureDto,
                                           @RequestParam("file") MultipartFile file,
                                           HttpSession httpSession, @PathVariable int courseId) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            String url = assignmentsHelper.uploadAssignment(file);
            lectureDto.setCourseId(courseId);
            lectureDto.setAssignmentUrl(url);
            lectureService.create(lectureDto, user);
            return "redirect:/courses/prepare/" + courseId;
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (IOException e) {
            return "Error404";
        }
    }

    @PostMapping("/{courseId}/publish")
    public String publishCourse(HttpSession httpSession, @PathVariable int courseId) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            courseService.publishCourse(courseId, user);
            return "redirect:/courses/" + courseId;
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{courseId}")
    public String viewCourseDetails(HttpSession httpSession, Model model, @PathVariable int courseId) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            Course course = courseService.getById(courseId);
            FilterOptionsDto filterOptionsDto = new FilterOptionsDto();
            filterOptionsDto.setTeacherId(course.getCreator().getId());
            FilterOptions filterOptions = new FilterOptions(filterOptionsDto.getTitle(),
                    filterOptionsDto.getTopicId(),
                    filterOptionsDto.getTeacherId(),
                    filterOptionsDto.getRating(),
                    filterOptionsDto.getSortBy(),
                    filterOptionsDto.getSortBy());
            List<Course> teacherCourses = courseService.getAll(filterOptions);
            model.addAttribute("isGraduated", courseService.getAllByUserGraduated(user.getId()).contains(course));
            model.addAttribute("assignments", assignmentService.getByUserSubmittedToCourse(user.getId(), courseId));
            model.addAttribute("averageGrade", assignmentService.getGradeForCourse(user.getId(), courseId));
            model.addAttribute("teacherCourses", teacherCourses);
            model.addAttribute("course", course);
            model.addAttribute("loggedIn", user);
            model.addAttribute("lectures", course.getLectures());
            model.addAttribute("lectureDto", new LectureDto());
            model.addAttribute("rates", course.getRates());
            model.addAttribute("rateDto", new RateDto());
            return "CourseDetailsView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/{courseId}/rate")
    public String rateCourse(@Valid @ModelAttribute("rateDto") RateDto rateDto, HttpSession httpSession, Model model, @PathVariable int courseId) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            courseService.rateCourse(courseId, user, rateDto);
            model.addAttribute("loggedIn", user);
            return "redirect:/courses/" + courseId;
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
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
            model.addAttribute("noteDto", new NoteDto());
            model.addAttribute("isSearch", false);
            return "LectureView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/{courseId}/lecture/{lectureId}")
    public String showSearchView(HttpSession httpSession,
                                 Model model,
                                 @PathVariable int courseId,
                                 @PathVariable int lectureId,
                                 @RequestParam String search) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            Course course = courseService.getById(courseId);
            Lecture lecture = lectureService.getById(lectureId);
            List<WikiPage> searchResults = wikiPageService.searchWikiPages(search);
            model.addAttribute("videoUrl", lecture.getVideoUrl());
            model.addAttribute("course", course);
            model.addAttribute("lecture", lecture);
            model.addAttribute("noteDto", new NoteDto());
            model.addAttribute("isSearch", true);
            model.addAttribute("results", searchResults);
            return "LectureView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{courseId}/lecture/{lectureId}/assignment")
    public String showSubmitAssignmentView(HttpSession httpSession, Model model, @PathVariable int courseId, @PathVariable int lectureId) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            Course course = courseService.getById(courseId);
            Lecture lecture = lectureService.getById(lectureId);

            model.addAttribute("course", course);
            model.addAttribute("lecture", lecture);
            model.addAttribute("noteDto", new NoteDto());
            return "LectureAssignmentSubmit";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{courseId}/lecture/{lectureId}/assignment/show")
    public String showAssignmentDemoConditionView(HttpSession httpSession, Model model, @PathVariable int courseId, @PathVariable int lectureId) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            Course course = courseService.getById(courseId);
            Lecture lecture = lectureService.getById(lectureId);
            model.addAttribute("assignmentUrl", lecture.getAssignmentUrl());
            model.addAttribute("course", course);
            model.addAttribute("lecture", lecture);
            model.addAttribute("noteDto", new NoteDto());
            return "AssignmentDemoConditionView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/assignments")
    public String assignmentsToCheck(HttpSession httpSession, Model model) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);

            model.addAttribute("loggedIn", user);
            model.addAttribute("assignments", assignmentService.getByTeacherForGrade(user.getId()));
            return "InstructorAssignmentsToCheck";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/assignments/user/{userId}")
    public String showUserAssignmentsSubmission(Model model, HttpSession httpSession, @PathVariable int userId) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            model.addAttribute("loggedIn", user);
            model.addAttribute("assignments", assignmentService.getByUserSubmitted(user.getId()));
            return "UserSubmittedAssignmentsView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/assignments/{assignmentId}/grade")
    public String showAssignmentEvaluation(HttpSession httpSession, Model model, @PathVariable int assignmentId) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            Assignment assignment = assignmentService.getById(assignmentId);
            model.addAttribute("assignment", assignment);
            model.addAttribute("loggedIn", user);
            model.addAttribute("gradeDto", new GradeDto());
            return "AssignmentEvaluation";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/assignments/{assignmentId}/grade")
    public String gradeAssignment(@Valid @ModelAttribute("gradeDto") GradeDto gradeDto, HttpSession httpSession, Model model, @PathVariable int assignmentId) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            Assignment assignment = assignmentService.getById(assignmentId);
            assignmentService.grade(assignmentId, gradeDto.getGradeId(), assignment.getLecture().getCourse().getId(), assignment.getUser().getId());
            model.addAttribute("assignment", assignment);
            model.addAttribute("loggedIn", user);
            return "redirect:/courses/assignments";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/{courseId}/lecture/{lectureId}/assignment")
    public String submitAssignment(@RequestParam("file") MultipartFile file, HttpSession httpSession, Model model, @PathVariable int courseId, @PathVariable int lectureId) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            Course course = courseService.getById(courseId);
            Lecture lecture = lectureService.getById(lectureId);
            assignmentService.submitAssignment(user, lectureId, file);
            model.addAttribute("course", course);
            model.addAttribute("lecture", lecture);
            return "redirect:/courses/" + course.getId();
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/user/{userId}")
    public String showTeacherCourses(HttpSession httpSession, Model model, @PathVariable int userId) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            model.addAttribute("loggedIn", user);
            model.addAttribute("publishedCourses", courseService.getAllPublishedCoursesFromTeacher(user));
            model.addAttribute("notPublishedCourses", courseService.getAllNotPublishedCoursesFromTeacher(user));
            return "TeacherCourses";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/{courseId}/delete")
    public String deleteCourse(HttpSession session, @PathVariable int courseId) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            courseService.delete(courseId, user);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        return "redirect:/users/allCourses";
    }

}

