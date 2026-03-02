package com.digiSchool.digiSchool.user.serviceimp;

import com.digiSchool.digiSchool.user.service.SmsService;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    @Override
    public void sendSms(String phoneNumber, String message) {
        // Simulation d'envoi de SMS
        System.out.println("[SMS] Destinataire: " + phoneNumber + " | Message: " + message);

        // Dans une implémentation réelle, on appellerait ici une API comme Twilio ou un
        // service local
        System.out.println("--------------------------------------------------");
        System.out.println("SMS ENVOYÉ À: " + phoneNumber);
        System.out.println("CONTENU: " + message);
        System.out.println("--------------------------------------------------");
    }
}
