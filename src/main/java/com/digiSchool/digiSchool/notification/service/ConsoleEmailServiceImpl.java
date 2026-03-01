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

    @Override
    public void sendSchoolValidationEmail(String to, String schoolName, String slug) {
        String schoolLink = "http://localhost:3000/ecoles/" + slug;
        System.out.println("========================================");
        System.out.println("SENDING EMAIL TO: " + to);
        System.out.println("SUBJECT: Votre ecole a ete validee");
        System.out.println("MESSAGE: Felicitations ! Votre ecole \"" + schoolName + "\" a ete validee. Consultez-la ici : " + schoolLink);
        System.out.println("========================================");
    }

    @Override
    public void sendSchoolRejectionEmail(String to, String schoolName, String motifRejet) {
        System.out.println("========================================");
        System.out.println("SENDING EMAIL TO: " + to);
        System.out.println("SUBJECT: Votre ecole a ete rejetee");
        System.out.println("MESSAGE: Votre ecole \"" + schoolName + "\" a ete rejetee. Motif : " + motifRejet);
        System.out.println("========================================");
    }

    @Override
    public void sendSchoolRegistrationConfirmation(String to, String schoolName) {
        System.out.println("========================================");
        System.out.println("SENDING EMAIL TO: " + to);
        System.out.println("SUBJECT: Confirmation d'inscription de votre ecole");
        System.out.println("MESSAGE: Votre ecole \"" + schoolName + "\" a bien ete enregistree. Elle est en attente de validation par un administrateur.");
        System.out.println("========================================");
    }
}
