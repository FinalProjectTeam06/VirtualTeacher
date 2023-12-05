package com.example.finalprojectvirtualteacher.services;

import com.example.finalprojectvirtualteacher.models.Enrollment;
import com.example.finalprojectvirtualteacher.repositories.contracts.EnrollmentRepository;
import com.example.finalprojectvirtualteacher.services.contacts.EnrollmentService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public List<Enrollment> getAllFinished(int userId) {
        return enrollmentRepository.getAllFinished(userId);
    }
}
