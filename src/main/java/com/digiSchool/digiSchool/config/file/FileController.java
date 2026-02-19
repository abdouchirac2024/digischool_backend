package com.digiSchool.digiSchool.config.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/files")
@Tag(name = "Files", description = "Gestion des fichiers (upload/download photos)")
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    @Operation(summary = "Upload un fichier (photo)")
    public ResponseEntity<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        String storedFilename = fileStorageService.store(file);

        // Construire l'URL d'accès au fichier
        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/")
                .path(storedFilename)
                .path("/download")
                .toUriString();

        return ResponseEntity.ok(Map.of(
                "url", fileUrl,
                "fileName", storedFilename
        ));
    }

    @GetMapping("/{fileName}/download")
    @Operation(summary = "Télécharger un fichier")
    public ResponseEntity<Resource> download(@PathVariable String fileName) {
        try {
            Path filePath = fileStorageService.load(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{fileName}")
    @Operation(summary = "Supprimer un fichier")
    public ResponseEntity<Void> delete(@PathVariable String fileName) {
        fileStorageService.delete(fileName);
        return ResponseEntity.noContent().build();
    }
}
