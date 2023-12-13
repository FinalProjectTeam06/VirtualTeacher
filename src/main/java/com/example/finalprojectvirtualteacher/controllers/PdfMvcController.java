package com.example.finalprojectvirtualteacher.controllers;

import com.example.finalprojectvirtualteacher.exceptions.AuthorizationException;
import com.example.finalprojectvirtualteacher.helpers.AuthenticationHelper;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.services.contacts.AssignmentService;
import com.example.finalprojectvirtualteacher.services.contacts.CourseService;
import com.example.finalprojectvirtualteacher.services.contacts.PdfService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Controller
@RequestMapping("/pdf")
public class PdfMvcController {
    private final PdfService pdfService;
    private final AuthenticationHelper authenticationHelper;
    private final AssignmentService assignmentService;
    private final CourseService courseService;

    public PdfMvcController(PdfService pdfService, AuthenticationHelper authenticationHelper, AssignmentService assignmentService, CourseService courseService) {
        this.pdfService = pdfService;
        this.authenticationHelper = authenticationHelper;
        this.assignmentService = assignmentService;
        this.courseService = courseService;
    }

    @GetMapping("/download/{courseId}")
    public ResponseEntity<byte[]> downloadPdf(HttpSession httpSession, @PathVariable int courseId) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            Course course = courseService.getById(courseId);
            double grade = assignmentService.getGradeForCourse(user.getId(), courseId);

            Resource exportResponse = pdfService.downloadCertificate(null, course, user, grade);

            byte[] pdfBytes = exportResponse.getContentAsByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=filled_template.pdf");
//
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfBytes.length)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (AuthorizationException | IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
