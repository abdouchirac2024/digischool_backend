package com.digiSchool.digiSchool.config.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final Path uploadDir;

    public FileStorageService(@Value("${file.upload-dir:uploads}") String uploadDir) {
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer le répertoire d'upload: " + uploadDir, e);
        }
    }

    /**
     * Stocke un fichier et retourne le nom généré
     */
    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Le fichier est vide");
        }

        // Valider le type de fichier (images seulement)
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Seules les images sont acceptées (JPG, PNG, WebP)");
        }

        // Valider la taille (max 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("Le fichier ne doit pas dépasser 5MB");
        }

        // Générer un nom unique
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String storedFilename = UUID.randomUUID().toString() + extension;

        try {
            Path targetLocation = this.uploadDir.resolve(storedFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return storedFilename;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du stockage du fichier", e);
        }
    }

    /**
     * Charge un fichier par son nom
     */
    public Path load(String filename) {
        return uploadDir.resolve(filename).normalize();
    }

    /**
     * Supprime un fichier
     */
    public void delete(String filename) {
        try {
            Path filePath = uploadDir.resolve(filename).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la suppression du fichier", e);
        }
    }
}
