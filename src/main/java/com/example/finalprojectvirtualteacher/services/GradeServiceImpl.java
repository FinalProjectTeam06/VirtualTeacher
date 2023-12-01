package com.example.finalprojectvirtualteacher.services;

import com.example.finalprojectvirtualteacher.models.Grade;
import com.example.finalprojectvirtualteacher.repositories.contracts.GradeRepository;
import com.example.finalprojectvirtualteacher.services.contacts.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradeServiceImpl implements GradeService {
    private final GradeRepository gradeRepository;

    @Autowired
    public GradeServiceImpl(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    @Override
    public Grade getById(int id) {
        return gradeRepository.getById(id);
    }
}
