package com.example.finalprojectvirtualteacher.services.contacts;

import com.example.finalprojectvirtualteacher.models.User;

public interface EmailService {
    void sendMessage(String to, String subject, String text);
    void sendUserCreationVerificationCode(User user, int code) ;
}
