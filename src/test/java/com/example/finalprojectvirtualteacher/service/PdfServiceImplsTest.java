package com.example.finalprojectvirtualteacher.service;

import com.example.finalprojectvirtualteacher.exceptions.FileUploadException;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.services.PdfServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PdfServiceImplsTest {

    @InjectMocks
    private PdfServiceImpl pdfService;

    @Mock
    private MockHttpServletResponse httpServletResponse;

    @Mock
    private Course course;

    @Mock
    private User user;



    @Test
    void testDownloadCertificateWithException() throws IOException {
        // Set up test data
        when(course.getTitle()).thenReturn("Test Course");
        when(user.getFirstName()).thenReturn("John");
        when(user.getLastName()).thenReturn("Doe");

        // Mock the behavior to throw an exception
        when(httpServletResponse.getOutputStream()).thenThrow(new IOException("Mocked IOException"));

        // Assertions
        assertThrows(FileUploadException.class, () -> pdfService.downloadCertificate(httpServletResponse, course, user, 4.5));
    }
}