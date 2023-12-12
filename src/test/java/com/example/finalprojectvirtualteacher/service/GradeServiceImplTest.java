package com.example.finalprojectvirtualteacher.service;

import com.example.finalprojectvirtualteacher.Helpers;
import com.example.finalprojectvirtualteacher.models.Grade;
import com.example.finalprojectvirtualteacher.repositories.contracts.GradeRepository;
import com.example.finalprojectvirtualteacher.services.GradeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GradeServiceImplTest {

    @InjectMocks
    GradeServiceImpl gradeService;
    @Mock
    GradeRepository gradeRepository;

    @Test
    void getById_Should_callRepository_And_Return_Grade(){
        Grade grade = Helpers.createMockGrade();

        Mockito.when(gradeRepository.getById(grade.getId())).thenReturn(grade);

        Grade result = gradeService.getById(grade.getId());

        Assertions.assertEquals(result,grade);

        Mockito.verify(gradeRepository,Mockito.times(1)).getById(grade.getId());
    }
}
