package com.example.finalprojectvirtualteacher.services;

import com.example.finalprojectvirtualteacher.exceptions.FileUploadException;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.services.contacts.PdfService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

@Service
public class PdfServiceImpl implements PdfService {
    public Resource downloadCertificate(HttpServletResponse httpServletResponse, Course course, User user, double grade) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            PdfReader pdfReader = new PdfReader("src/main/resources/PdfTemplate/certificate.pdf");

            PdfStamper pdfStamper = new PdfStamper(pdfReader, outputStream);

            AcroFields formFields = pdfStamper.getAcroFields();
            formFields.setField("courseTitle", course.getTitle());
            formFields.setField("studentName", String.format("%s %s", user.getFirstName(), user.getLastName()));
            formFields.setField("courseGrade", String.format("  Grade: %.2f/6.00", grade));
            formFields.setField("date", LocalDate.now().toString());

            pdfStamper.setFormFlattening(true);

            pdfStamper.close();
            pdfReader.close();
            byte[] pdfBytes = outputStream.toByteArray();

            if(httpServletResponse != null) {
                httpServletResponse.getOutputStream().write(pdfBytes);
            }

            ByteArrayResource resource = new ByteArrayResource(pdfBytes);
            return resource;

        } catch (IOException | DocumentException e) {
            throw new FileUploadException(e.getMessage(), (IOException) e);
        }

    }

}
