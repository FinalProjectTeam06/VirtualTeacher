package com.example.finalprojectvirtualteacher.controllers.mvc;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.exceptions.EntityDuplicateException;
import com.example.finalprojectvirtualteacher.exceptions.InvalidRecaptchaException;
import com.example.finalprojectvirtualteacher.helpers.AuthenticationHelper;
import com.example.finalprojectvirtualteacher.helpers.mappers.UserMapper;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.models.dto.LoginDto;
import com.example.finalprojectvirtualteacher.models.dto.RegisterMvcDto;
import com.example.finalprojectvirtualteacher.services.contacts.RecaptchaService;
import com.example.finalprojectvirtualteacher.services.contacts.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthenticationMvcController {

    public static final String G_RECAPTCHA_RESPONSE = "g-recaptcha-response";

    public static final String REGISTER_COMPANY = "/register/company";
    private static final String CREATE_EMPLOYEE_DTO = "createUserDto";


    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;
    private final UserService userService;
    private final RecaptchaService recaptchaService;


@Autowired
    public AuthenticationMvcController(AuthenticationHelper authenticationHelper, UserMapper userMapper, UserService userService, RecaptchaService captchaService, RecaptchaService recaptchaService) {
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
        this.userService = userService;
        this.recaptchaService = recaptchaService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginDto());
        return "LoginView";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto loginDto,
                              BindingResult bindingResult,
                              @RequestParam(value = G_RECAPTCHA_RESPONSE,required = false) String recaptchaResponse,
                              HttpSession httpSession,
                              Model model
                              ) {
        if (bindingResult.hasErrors()) {
            return "LoginView";
        }
        try {
            recaptchaService.validateRecaptcha(recaptchaResponse);
        } catch (InvalidRecaptchaException e) {
            bindingResult.rejectValue("recaptchaResponse", "recaptcha_error", e.getMessage());
            return "LoginView";
        }
        try {
            User user= authenticationHelper.verifyAuthentication(loginDto.getEmail(), loginDto.getPassword());
            if (!user.isActivated()){
                userService.sendActivationEmail(user);
                return "redirect:/verification/"+ loginDto.getEmail();
            }
            httpSession.setAttribute("currentUser", loginDto.getEmail());
            return "redirect:/users";
        } catch (AuthorizationException e) {
            bindingResult.rejectValue("password", "auth_error", e.getMessage());
            return "LoginView";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute("currentUser");
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new RegisterMvcDto());
        return "RegisterView";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") RegisterMvcDto registerMvcDto,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "RegisterView";
        }

        if (!registerMvcDto.getPassword().equals(registerMvcDto.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "password_error", "Password confirmation should match password.");
            return "RegisterView";
        }
        try {
            User user = userMapper.fromRegisterMvcDto(registerMvcDto);
            userService.create(user);
            if (!user.isActivated()){
                return "redirect:/verification/"+ user.getEmail();
            }
            return "redirect:/auth/login";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("email", "email_error", e.getMessage());
            return "RegisterView";
        }
    }


}
