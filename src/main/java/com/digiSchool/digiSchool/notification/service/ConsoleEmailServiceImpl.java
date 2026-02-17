package com.digiSchool.digiSchool.notification.service;

import org.springframework.stereotype.Service;

@Service
public class ConsoleEmailServiceImpl implements EmailService {

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        String resetLink = "http://localhost:3000/reset-password?token=" + token;
        System.out.println("========================================");
        System.out.println("SNDING EMAIL TO: " + to);
        System.out.println("SUBJECT: Password Reset Request");
        System.out.println("MESSAGE: Please use the following link to reset your password: " + resetLink);
        System.out.println("========================================");
    }
}
