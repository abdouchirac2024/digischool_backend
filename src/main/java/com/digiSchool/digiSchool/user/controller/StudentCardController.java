package com.digiSchool.digiSchool.user.controller;

import com.digiSchool.digiSchool.user.service.StudentCardService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/students")
public class StudentCardController {

    private final StudentCardService studentCardService;

    public StudentCardController(StudentCardService studentCardService) {
        this.studentCardService = studentCardService;
    }

    @GetMapping("/{id}/card")
    public ResponseEntity<byte[]> getStudentCard(@PathVariable Long id) {
        try {
            byte[] pdfContents = studentCardService.generateStudentCard(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String filename = "carte_scolaire_" + id + ".pdf";
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
