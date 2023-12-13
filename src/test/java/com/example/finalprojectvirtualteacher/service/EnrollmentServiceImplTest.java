package com.example.finalprojectvirtualteacher.service;

import com.example.finalprojectvirtualteacher.Helpers;
import com.example.finalprojectvirtualteacher.models.Enrollment;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.repositories.contracts.EnrollmentRepository;
import com.example.finalprojectvirtualteacher.services.EnrollmentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceImplTest {

    @Mock
    EnrollmentRepository enrollmentRepository;
    @InjectMocks
    EnrollmentServiceImpl enrollmentService;

    @Test
    void getAllFinished_Should_CallRepository(){
        User user = Helpers.createMockUser();
        List<Enrollment> list = new ArrayList<>();
        list.add(new Enrollment());
        list.add(new Enrollment());

        Mockito.when(enrollmentRepository.getAllFinished(user.getId())).thenReturn(list);

        List<Enrollment> result = enrollmentService.getAllFinished(user.getId());

        Assertions.assertEquals(list,result);
    }

}
