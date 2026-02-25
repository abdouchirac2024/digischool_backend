package com.digiSchool.digiSchool.user.service;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

public interface BulkImportService {
    List<Map<String, String>> importStudents(MultipartFile file) throws Exception;
}
