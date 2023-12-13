package com.example.finalprojectvirtualteacher.service;

import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.services.PdfServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PdfServiceImplsTest {

    @Test
     void testDownloadCertificate() {
        // Arrange
        PdfServiceImpl pdfService = new PdfServiceImpl(); // Replace with the actual class name
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        Course mockCourse = new Course(); // Replace with the actual Course class
        mockCourse.setTitle("Sample Course");
        User mockUser = new User(); // Replace with the actual User class
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        double mockGrade = 4.5; // Replace with the actual grade

        // Act
        Resource resultResource = pdfService.downloadCertificate
                (mockResponse, mockCourse, mockUser, mockGrade);

        // Assert
        assertNotNull(resultResource);
        if (mockResponse != null) {
            try {
                verify(mockResponse.getOutputStream(), times(1))
                        .write(any(byte[].class));
            } catch (IOException e) {
                e.printStackTrace(); // Handle or log the exception based on your needs
            }
        }
        // Additional assertions based on the expected content of the certificate
        // You might want to check specific fields in the PDF, depending on your implementation
    }
}


