package com.example.finalprojectvirtualteacher.services.contacts;

import com.example.finalprojectvirtualteacher.models.Enrollment;

import java.util.List;

public interface EnrollmentService {
    List<Enrollment> getAllFinished(int userId);
}
