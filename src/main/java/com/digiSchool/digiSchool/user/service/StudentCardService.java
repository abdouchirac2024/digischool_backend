package com.digiSchool.digiSchool.user.service;

public interface StudentCardService {
    byte[] generateStudentCard(Long eleveId) throws Exception;

    byte[] generateQRCodeImage(String text, int width, int height) throws Exception;
}
