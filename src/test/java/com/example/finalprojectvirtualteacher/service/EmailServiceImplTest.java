package com.example.finalprojectvirtualteacher.service;

import com.example.finalprojectvirtualteacher.Helpers;
import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.services.EmailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static com.example.finalprojectvirtualteacher.Helpers.createMockUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class EmailServiceImplTest {

@InjectMocks
    EmailServiceImpl emailService;

@Mock
    JavaMailSender mailSender;

@Test
    void send_UserCreation_Verification_Code(){
    User user = createMockUser();
    int verificationCode = 123;

    emailService.sendUserCreationVerificationCode(user,verificationCode);

    ArgumentCaptor<SimpleMailMessage> argumentCaptor =
            ArgumentCaptor.forClass(SimpleMailMessage.class);
    Mockito.verify(mailSender).send(argumentCaptor.capture());

    SimpleMailMessage capturedMessage = argumentCaptor.getValue();
    assertNotNull(capturedMessage);
    assertEquals("amin.histudy@gmail.com", capturedMessage.getFrom());
    assertEquals(user.getEmail(), capturedMessage.getTo()[0]);
    assertEquals("HiStudy account activation", capturedMessage.getSubject());
    assertEquals("Hello, " + user.getEmail() +
                    " , \n Welcome to HiStudy! \n \n To activate your account use this code\n 123",
            capturedMessage.getText());
}


}
