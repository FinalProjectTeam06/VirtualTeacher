package com.example.finalprojectvirtualteacher.controllers.mvc;

import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.FilterOptions;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.dto.FilterOptionsDto;
import com.example.finalprojectvirtualteacher.services.contacts.CourseService;
import com.example.finalprojectvirtualteacher.services.contacts.UserService;
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

    public CourseMvcController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
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
        model.addAttribute("topicId", topicId);
        model.addAttribute("courses", courses);
        model.addAttribute("teachers", teachers);
        model.addAttribute("filterOptionsDto", filterOptionsDto);
        return "ViewCourseCategories";
    }
}
