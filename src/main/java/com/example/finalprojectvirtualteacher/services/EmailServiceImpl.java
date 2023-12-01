package com.example.finalprojectvirtualteacher.services;

import com.example.finalprojectvirtualteacher.models.User;
import com.example.finalprojectvirtualteacher.services.contacts.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService {

    public static final String ERROR_MAIL = "some.email@mail.com";

    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private String userCreationTemplate(int code, User user) {
        return String.format("Hello, %s , \n Welcome to HiStudy! \n \n To activate your account use this code\n %d",
                user.getEmail(), code);
    }

    @Async
    public void sendMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("amin.histudy@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Override
    public void sendUserCreationVerificationCode(User user, int code) {
        String msg = userCreationTemplate(code, user);
        sendMessage(user.getEmail(), "HiStudy account activation", msg);

    }

}
