package com.example.finalprojectvirtualteacher.controllers.mvc;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.helpers.AuthenticationHelper;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.services.contacts.CommentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comments")
public class CommentMvcController {
    private final AuthenticationHelper authenticationHelper;
    private final CommentService commentService;

    public CommentMvcController(AuthenticationHelper authenticationHelper, CommentService commentService) {
        this.authenticationHelper = authenticationHelper;
        this.commentService = commentService;
    }

    @GetMapping("/course/{courseId}")
    public String showCourseComments(HttpSession httpSession, Model model, @PathVariable int courseId) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            model.addAttribute("comments", commentService.getByCourseId(courseId));
            return "UserEnrolledCoursesView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }
}
