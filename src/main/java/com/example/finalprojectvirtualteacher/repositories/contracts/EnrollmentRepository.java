package com.example.finalprojectvirtualteacher.repositories.contracts;

import com.example.finalprojectvirtualteacher.models.Enrollment;

import java.util.List;

public interface EnrollmentRepository {
    List<Enrollment> getAllFinished(int userId);
}
