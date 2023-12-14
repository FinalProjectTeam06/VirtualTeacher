package com.example.finalprojectvirtualteacher.controllers.mvc;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Get the status code from the request
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");

        // Check if it's a 404 error
        if(statusCode != null && statusCode == 404) {
            return "ErrorPage";
        }
        
        return "ErrorPage";
    }
}
