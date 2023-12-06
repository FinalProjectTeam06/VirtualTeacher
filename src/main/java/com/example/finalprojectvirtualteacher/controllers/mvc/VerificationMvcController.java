package com.example.finalprojectvirtualteacher.controllers.mvc;

import com.example.finalprojectvirtualteacher.exceptions.ForbiddenOperationException;
import com.example.finalprojectvirtualteacher.exceptions.WrongActivationCodeException;
import com.example.finalprojectvirtualteacher.models.dto.CodeDto;
import com.example.finalprojectvirtualteacher.services.contacts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequestMapping("/verification")
public class VerificationMvcController {

    private final UserService userService;
    @Autowired
    public VerificationMvcController(UserService service) {
        this.userService = service;
    }

    @GetMapping("/{email}")
    public String sendEmailView(@PathVariable String email, Model model){

        model.addAttribute("code", new CodeDto());
        model.addAttribute("isSuccess", true);

        return "SendVerificationMailPage";
    }

    @PostMapping("/{email}")
    public String validateUser(@ModelAttribute("code") String code, @PathVariable String email){
        try{
            userService.activateAccount(Integer.parseInt(code));
            return "redirect:/auth/login";
        }catch (WrongActivationCodeException e){
            return "redirect:/verification/" + email;
        }
    }

    @PostMapping("/new-code/{email}")
    public String sendNewActivationCode(@PathVariable String email) {
        try {
            userService.resendActivationCode(email);
            return "redirect:/verification/" + email;
        } catch (ForbiddenOperationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

}

