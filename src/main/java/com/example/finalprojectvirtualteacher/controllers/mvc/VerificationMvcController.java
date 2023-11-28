package com.example.finalprojectvirtualteacher.controllers.mvc;

import com.example.finalprojectvirtualteacher.exceptions.WrongActivationCodeException;
import com.example.finalprojectvirtualteacher.models.dto.CodeDto;
import com.example.finalprojectvirtualteacher.services.contacts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/verification")
public class VerificationMvcController {

    private final UserService service;
    @Autowired
    public VerificationMvcController(UserService service) {
        this.service = service;
    }

    @GetMapping("/{email}")
    public String sendEmailView(@PathVariable String email, Model model){

        model.addAttribute("code", new CodeDto());
        model.addAttribute("isSuccess", true);
        model.addAttribute("action", "/verification/"+ email);

        return "SendVerificationMailPage";
    }

    @PostMapping("/{email}")
    public String validateUser(@ModelAttribute("code") String code, @PathVariable String email){
        try{
            service.activateAccount(Integer.parseInt(code));
            return "redirect:/";
        }catch (WrongActivationCodeException e){
            return "redirect:/verification/" + email;
        }
    }


}

