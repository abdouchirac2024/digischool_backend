package com.digiSchool.digiSchool.user.controller;

import com.digiSchool.digiSchool.user.service.BulkImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/students/import")
public class BulkImportController {

    private final BulkImportService bulkImportService;

    public BulkImportController(BulkImportService bulkImportService) {
        this.bulkImportService = bulkImportService;
    }

    @PostMapping
    public ResponseEntity<List<Map<String, String>>> importStudents(@RequestParam("file") MultipartFile file) {
        try {
            List<Map<String, String>> results = bulkImportService.importStudents(file);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
