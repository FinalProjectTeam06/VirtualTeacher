package com.example.finalprojectvirtualteacher.controllers.mvc;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.helpers.AuthenticationHelper;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.FilterOptions;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.dto.FilterOptionsDto;
import com.example.finalprojectvirtualteacher.services.contacts.CourseService;
import com.example.finalprojectvirtualteacher.services.contacts.TopicService;
import com.example.finalprojectvirtualteacher.services.contacts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseMvcController {
    private final CourseService courseService;
    private final UserService userService;
    private final TopicService topicService;
    private final AuthenticationHelper authenticationHelper;

    public CourseMvcController(CourseService courseService, UserService userService, TopicService topicService, AuthenticationHelper authenticationHelper) {
        this.courseService = courseService;
        this.userService = userService;
        this.topicService = topicService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isTeacher")
    public boolean populateIsTeacher(HttpSession session) {
        try {
            User user=authenticationHelper.tryGetCurrentUser(session);
            return (user.getRole().getId()==2 || user.getRole().getId()==3);
        }catch (AuthorizationException e){
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
        List<User> teachers=userService.getAllTeachers();
        List<Course> courses = courseService.getAll(filterOptions);
        model.addAttribute("topicName", topicService.getById(topicId).getName());
        model.addAttribute("topicId", topicId);
        model.addAttribute("courses", courses);
        model.addAttribute("teachers", teachers);
        model.addAttribute("filterOptionsDto", filterOptionsDto);
        return "ViewCourseCategories";
    }

    @GetMapping("/enrolled")
    public String showEnrolledCourses(HttpSession httpSession, Model model){
        try {
            User user=authenticationHelper.tryGetCurrentUser(httpSession);
            model.addAttribute("enrolledCourses", user.getCourses());
            model.addAttribute("loggedIn", user);
            return "UserEnrolledCoursesView";
        }catch (AuthorizationException e){
            return "redirect:/auth/login";
        }
    }
}
