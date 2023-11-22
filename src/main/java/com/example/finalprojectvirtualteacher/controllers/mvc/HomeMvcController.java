package com.example.finalprojectvirtualteacher.controllers.mvc;

import com.example.finalprojectvirtualteacher.models.dto.FilterOptionsDto;
import com.example.finalprojectvirtualteacher.services.contacts.CourseService;
import com.example.finalprojectvirtualteacher.services.contacts.LectureService;
import com.example.finalprojectvirtualteacher.services.contacts.TopicService;
import com.example.finalprojectvirtualteacher.services.contacts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeMvcController {
    private final TopicService topicService;
    private final UserService userService;
    private final CourseService courseService;
    private final LectureService lectureService;

    public HomeMvcController(TopicService topicService, UserService userService, CourseService courseService, LectureService lectureService) {
        this.topicService = topicService;
        this.userService = userService;
        this.courseService = courseService;
        this.lectureService = lectureService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
    @GetMapping
    public String showHomePage(Model model){
        model.addAttribute("enrollments", courseService.getAllEnrollments());
        model.addAttribute("lectures", lectureService.getAll());
        model.addAttribute("courses", courseService.getAll());
        model.addAttribute("users", userService.getAll());
        model.addAttribute("filter", new FilterOptionsDto());
        model.addAttribute("topics", topicService.getAll());
        return "HomeView";
    }
}
