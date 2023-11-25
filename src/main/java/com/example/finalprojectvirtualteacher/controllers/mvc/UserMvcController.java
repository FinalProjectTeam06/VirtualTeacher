package com.example.finalprojectvirtualteacher.controllers.mvc;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.helpers.AuthenticationHelper;
import com.example.finalprojectvirtualteacher.models.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserMvcController {
    private final AuthenticationHelper authenticationHelper;

    public UserMvcController(AuthenticationHelper authenticationHelper) {
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
    @ModelAttribute("isTeacher")
    public boolean populateIsTeacher(HttpSession session) {
        try {
            User user=authenticationHelper.tryGetCurrentUser(session);
            return user.getRole().getId()==2;
        }catch (AuthorizationException e){
            return false;
        }
    }

    @GetMapping
    public String showUserPage(HttpSession httpSession, Model model){
        try {
            User user=authenticationHelper.tryGetCurrentUser(httpSession);
            if(user.getRole().getId()==1) {
                model.addAttribute("loggedIn", user);
                return "StudentDashboard";
            }else {
                model.addAttribute("loggedIn", user);
                return "InstructorDashboard";
            }
        }catch (AuthorizationException e){
            return "redirect:/auth/login";
        }
    }
}
