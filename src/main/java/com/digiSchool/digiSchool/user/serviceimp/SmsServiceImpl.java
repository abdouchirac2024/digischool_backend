package com.digiSchool.digiSchool.user.serviceimp;

import com.digiSchool.digiSchool.user.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    @Override
    public void sendSms(String phoneNumber, String message) {
        // Simulation d'envoi de SMS
        log.info("SIMULATION SMS | Destinataire: {} | Message: {}", phoneNumber, message);

        // Dans une implémentation réelle, on appellerait ici une API comme Twilio ou un
        // service local
        System.out.println("--------------------------------------------------");
        System.out.println("SMS ENVOYÉ À: " + phoneNumber);
        System.out.println("CONTENU: " + message);
        System.out.println("--------------------------------------------------");
    }
}
