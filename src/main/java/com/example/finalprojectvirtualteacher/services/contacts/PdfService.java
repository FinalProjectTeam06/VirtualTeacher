package com.example.finalprojectvirtualteacher.services.contacts;

import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface PdfService {
    Resource downloadCertificate(HttpServletResponse httpServletResponse, Course course, User user, double grade);
}
