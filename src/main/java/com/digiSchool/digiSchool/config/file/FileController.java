package com.digiSchool.digiSchool.config.file;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/files")
@Tag(name = "Files", description = "Gestion des fichiers (upload photos via Cloudinary)")
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    @Operation(summary = "Upload une photo sur Cloudinary")
    public ResponseEntity<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        // store() retourne directement l'URL Cloudinary (secure_url)
        String cloudinaryUrl = fileStorageService.store(file);

        return ResponseEntity.ok(Map.of(
                "url", cloudinaryUrl,
                "fileName", file.getOriginalFilename() != null ? file.getOriginalFilename() : "photo"
        ));
    }

    @DeleteMapping("/{publicId}")
    @Operation(summary = "Supprimer une photo de Cloudinary")
    public ResponseEntity<Void> delete(@PathVariable String publicId) {
        fileStorageService.delete(publicId);
        return ResponseEntity.noContent().build();
    }
}
