package com.digiSchool.digiSchool.notification.service;

public interface EmailService {
    void sendPasswordResetEmail(String to, String token);
    void sendSchoolValidationEmail(String to, String schoolName, String slug);
    void sendSchoolRejectionEmail(String to, String schoolName, String motifRejet);
    void sendSchoolRegistrationConfirmation(String to, String schoolName);
}
