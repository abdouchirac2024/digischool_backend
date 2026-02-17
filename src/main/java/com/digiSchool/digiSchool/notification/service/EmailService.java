package com.digiSchool.digiSchool.notification.service;

public interface EmailService {
    void sendPasswordResetEmail(String to, String token);
}
