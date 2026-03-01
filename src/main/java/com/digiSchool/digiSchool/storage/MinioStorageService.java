package com.digiSchool.digiSchool.storage;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Service de stockage d'objets via MinIO (S3-compatible).
 * Compatible avec plusieurs instances backend — les fichiers sont centralisés.
 */
@Service
public class MinioStorageService {

    private static final Logger log = LoggerFactory.getLogger(MinioStorageService.class);

    // Types MIME autorisés pour les avatars
    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp");

    // Types MIME autorisés pour les documents
    private static final Set<String> ALLOWED_DOCUMENT_TYPES = Set.of(
            "application/pdf",
            "image/jpeg", "image/png", "image/gif", "image/webp",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

    private static final long MAX_AVATAR_SIZE = 5 * 1024 * 1024L; // 5 Mo
    private static final long MAX_DOCUMENT_SIZE = 20 * 1024 * 1024L; // 20 Mo

    private final MinioClient minioClient;

    @Value("${minio.bucket.avatars:avatars}")
    private String avatarBucket;

    @Value("${minio.bucket.documents:documents}")
    private String documentBucket;

    @Value("${minio.public-url}")
    private String publicUrl;

    public MinioStorageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    // -------------------------------------------------------
    // Avatars / Photos de profil
    // -------------------------------------------------------

    /**
     * Upload une image de profil (avatar) vers MinIO.
     *
     * @param userId ID de l'utilisateur
     * @param file   Fichier multipart à uploader
     * @return URL publique de l'image uploadée
     */
    public String uploadAvatar(Long userId, MultipartFile file) throws IOException {
        validateFile(file, ALLOWED_IMAGE_TYPES, MAX_AVATAR_SIZE,
                "Format non supporté. Formats acceptés : JPG, PNG, GIF, WebP");

        String objectName = buildObjectName(userId, file.getOriginalFilename());

        uploadToMinio(avatarBucket, objectName, file);

        String url = buildPublicUrl(avatarBucket, objectName);
        log.info("Avatar uploadé → MinIO bucket={} object={} url={}", avatarBucket, objectName, url);
        return url;
    }

    /**
     * Supprime un avatar de MinIO à partir de son URL publique.
     *
     * @param avatarUrl URL publique précédemment retournée par uploadAvatar
     */
    public void deleteAvatar(String avatarUrl) {
        deleteByUrl(avatarUrl, avatarBucket);
    }

    // -------------------------------------------------------
    // Documents génériques (PDF, images scolaires, etc.)
    // -------------------------------------------------------

    /**
     * Upload un document (PDF, image, Word) vers MinIO dans le bucket documents.
     *
     * @param ownerId ID du propriétaire (élève, enseignant…)
     * @param folder  Sous-dossier logique (ex: "cours", "bulletins")
     * @param file    Fichier à uploader
     * @return URL publique du document
     */
    public String uploadDocument(Long ownerId, String folder, MultipartFile file) throws IOException {
        validateFile(file, ALLOWED_DOCUMENT_TYPES, MAX_DOCUMENT_SIZE,
                "Format non supporté. Formats acceptés : PDF, JPG, PNG, Word");

        String prefix = (folder != null && !folder.isBlank()) ? folder + "/" : "";
        String objectName = prefix + buildObjectName(ownerId, file.getOriginalFilename());

        uploadToMinio(documentBucket, objectName, file);

        String url = buildPublicUrl(documentBucket, objectName);
        log.info("Document uploadé → MinIO bucket={} object={}", documentBucket, objectName);
        return url;
    }

    /**
     * Supprime un document de MinIO à partir de son URL publique.
     */
    public void deleteDocument(String documentUrl) {
        deleteByUrl(documentUrl, documentBucket);
    }

    /**
     * Génère une URL pré-signée temporaire pour accéder à un document privé.
     *
     * @param bucket        Nom du bucket
     * @param objectName    Nom de l'objet
     * @param expiryMinutes Durée de validité en minutes
     * @return URL signée temporaire
     */
    public String getPresignedUrl(String bucket, String objectName, int expiryMinutes) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(objectName)
                            .expiry(expiryMinutes, TimeUnit.MINUTES)
                            .build());
        } catch (Exception e) {
            log.error("Impossible de générer l'URL pré-signée pour {}/{}: {}", bucket, objectName, e.getMessage());
            throw new StorageException("Erreur lors de la génération de l'URL sécurisée", e);
        }
    }

    // -------------------------------------------------------
    // Helpers privés
    // -------------------------------------------------------

    private void uploadToMinio(String bucket, String objectName, MultipartFile file) throws IOException {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
        } catch (Exception e) {
            throw new StorageException("Erreur upload MinIO vers " + bucket + "/" + objectName, e);
        }
    }

    private void deleteByUrl(String url, String bucket) {
        if (url == null || url.isBlank())
            return;
        try {
            String objectName = extractObjectName(url, bucket);
            if (objectName == null || objectName.isBlank())
                return;

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build());
            log.debug("Fichier supprimé de MinIO: bucket={} object={}", bucket, objectName);
        } catch (Exception e) {
            // Ne pas faire échouer la transaction — la BDD reste prioritaire
            log.warn("Impossible de supprimer le fichier MinIO '{}': {}", url, e.getMessage());
        }
    }

    private void validateFile(MultipartFile file, Set<String> allowedTypes, long maxSize, String typeErrMsg) {
        if (file == null || file.isEmpty()) {
            throw new StorageException("Le fichier est vide");
        }
        if (file.getSize() > maxSize) {
            throw new StorageException("La taille du fichier dépasse la limite autorisée ("
                    + (maxSize / (1024 * 1024)) + " Mo)");
        }
        String contentType = file.getContentType();
        if (contentType == null || !allowedTypes.contains(contentType.toLowerCase())) {
            throw new StorageException(typeErrMsg);
        }
    }

    private String buildObjectName(Long ownerId, String originalFilename) {
        String ext = extractExtension(originalFilename);
        return ownerId + "_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12) + ext;
    }

    private String buildPublicUrl(String bucket, String objectName) {
        String base = publicUrl.endsWith("/") ? publicUrl.substring(0, publicUrl.length() - 1) : publicUrl;
        return base + "/" + bucket + "/" + objectName;
    }

    private String extractObjectName(String url, String bucket) {
        if (url == null)
            return null;
        String prefix = "/" + bucket + "/";
        int idx = url.lastIndexOf(prefix);
        if (idx >= 0) {
            return url.substring(idx + prefix.length());
        }
        // Fallback : dernier segment
        String[] parts = url.split("/");
        return parts.length > 0 ? parts[parts.length - 1] : null;
    }

    private String extractExtension(String filename) {
        if (filename == null)
            return ".bin";
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot).toLowerCase() : ".bin";
    }
}