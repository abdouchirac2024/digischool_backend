package com.digiSchool.digiSchool.config.file;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class FileStorageService {

    private final Cloudinary cloudinary;

    public FileStorageService(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true));
    }

    /**
     * Upload un fichier sur Cloudinary et retourne l'URL publique
     */
    @SuppressWarnings("unchecked")
    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Le fichier est vide");
        }

        // Valider le type de fichier (images et PDF)
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.startsWith("image/") && !contentType.equals("application/pdf"))) {
            throw new RuntimeException("Seules les images et les documents PDF sont acceptés (JPG, PNG, WebP, PDF)");
        }

        // Valider la taille (max 10MB pour accommoder les PDF)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new RuntimeException("Le fichier ne doit pas dépasser 10MB");
        }

        try {
            Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "digischool/documents",
                    "resource_type", "auto"));
            return (String) result.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload sur Cloudinary", e);
        }
    }

    /**
     * Supprime un fichier de Cloudinary par son public_id
     */
    public void delete(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la suppression sur Cloudinary", e);
        }
    }
}
