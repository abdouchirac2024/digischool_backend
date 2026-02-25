package com.digiSchool.digiSchool.user.service;

public interface SmsService {
    void sendSms(String phoneNumber, String message);
}
