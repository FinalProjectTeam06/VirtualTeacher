package com.example.finalprojectvirtualteacher.controllers.mvc;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.helpers.AuthenticationHelper;
import com.example.finalprojectvirtualteacher.helpers.mappers.ImageHelper;
import com.example.finalprojectvirtualteacher.helpers.mappers.UserMapper;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.dto.UserDtoUpdate;
import com.example.finalprojectvirtualteacher.services.contacts.AssignmentService;
import com.example.finalprojectvirtualteacher.services.contacts.CourseService;
import com.example.finalprojectvirtualteacher.services.contacts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserMvcController {
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;
    private final UserService userService;
    private final ImageHelper imageHelper;
    private final CourseService courseService;
    private final AssignmentService assignmentService;

    public UserMvcController(AuthenticationHelper authenticationHelper, UserMapper userMapper, UserService userService, ImageHelper imageHelper, CourseService courseService, AssignmentService assignmentService) {
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
        this.userService = userService;
        this.imageHelper = imageHelper;
        this.courseService = courseService;
        this.assignmentService = assignmentService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
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
        try{
            User user = authenticationHelper.tryGetCurrentUser(session);
            return (user.getRole().getId()==3);
        } catch (AuthorizationException e) {
            return false;
        }
    }

    @GetMapping
    public String showUserPage(HttpSession httpSession, Model model) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            if (user.getRole().getId() == 1) {
                model.addAttribute("loggedIn", user);
                return "StudentDashboard";
            } else {
                model.addAttribute("loggedIn", user);
                return "InstructorDashboard";
            }
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/profile")
    public String showProfileInformation(HttpSession httpSession, Model model) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            model.addAttribute("loggedIn", user);
            return "ProfileInformation";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/update")
    public String showUpdateProfilePage(Model model, HttpSession httpSession) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            model.addAttribute("userToUpdate", user);
            model.addAttribute("updateDto", userMapper.toDto(user));
            model.addAttribute("loggedIn", user);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        return "ProfileSettings";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute("updateDto") UserDtoUpdate userDtoUpdate, Model model, HttpSession httpSession, BindingResult bindingResult) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            model.addAttribute("userToUpdate", user);
            model.addAttribute("user", user);
            model.addAttribute("loggedIn", user);
            userService.updateUser(user, user, userDtoUpdate);
            return "ProfileSettings";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/picture")
    public String addProfilePhoto(@RequestParam("file") MultipartFile file, HttpSession httpSession) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            String url = imageHelper.uploadImage(file);
            userService.addProfilePhoto(user, url);
            return "redirect:/users/update";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (IOException e) {
            return "Error404";
        }
    }

    @PostMapping("/course/{courseId}/enroll")
    public String enrollCourse(HttpSession httpSession, Model model, @PathVariable int courseId) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            userService.enrollCourse(user, courseId);
            model.addAttribute("loggedIn", user);
            model.addAttribute("enrolledCourses", courseService.getAllByUserNotCompleted(user.getId()));
            model.addAttribute("completedCourses", courseService.getAllByUserCompleted(user.getId()));
            model.addAttribute("activeCourses", courseService.getAllActiveCoursesNotEnrolled(user));
            return "UserEnrolledCoursesView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/all")
    public String showAllUsers(HttpSession session, Model model) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            List<User> users = userService.getAll();
            model.addAttribute("users",users);
            model.addAttribute("loggedIn",user);
            return "Users";
        } catch (AuthorizationException e){
            return "redirect:/auth/login";
        }
    }
    @PostMapping("/{userId}")
    public String deleteUser(@PathVariable int userId,HttpSession session){
        try{
            User user= authenticationHelper.tryGetCurrentUser(session);
            userService.deleteUser(userId,user);
        } catch (AuthorizationException e){
            return "redirect:/auth/login";
        }
        return "redirect:/all";
    }
    @GetMapping("/allCourses")
    public String showAllCourses(HttpSession session,Model model) {
        try{
            User user = authenticationHelper.tryGetCurrentUser(session);
            List<Course> courses = courseService.getAll();
            model.addAttribute("user",user);
            model.addAttribute("courses",courses);
            model.addAttribute("loggedIn",user);
            return "AllCourses";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }
}
