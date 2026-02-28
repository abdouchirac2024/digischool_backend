package com.digiSchool.digiSchool.storage.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informations d'un fichier stocké dans MongoDB GridFS")
public class FileInfoDto {

    @Schema(description = "ID MongoDB du fichier (ObjectId)", example = "65a1b2c3d4e5f6a7b8c9d0e1")
    private String fileId;

    @Schema(description = "Nom original du fichier", example = "photo_eleve.jpg")
    private String filename;

    @Schema(description = "Type MIME du fichier", example = "image/jpeg")
    private String contentType;

    @Schema(description = "Taille en octets", example = "204800")
    private Long size;

    @Schema(description = "Type de document", example = "PHOTO_ELEVE")
    private String typeDocument;

    @Schema(description = "ID de l'inscription liée (MySQL)", example = "42")
    private Long inscriptionId;

    @Schema(description = "ID de l'élève lié (MySQL)", example = "7")
    private Long eleveId;

    @Schema(description = "Tenant de l'école", example = "CM-CENTRE-ECOLE-001")
    private String tenant;

    @Schema(description = "URL de téléchargement", example = "/api/files/65a1b2c3d4e5f6a7b8c9d0e1")
    private String downloadUrl;

    @Schema(description = "Date d'upload")
    private LocalDateTime uploadedAt;

    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public Long getSize() { return size; }
    public void setSize(Long size) { this.size = size; }

    public String getTypeDocument() { return typeDocument; }
    public void setTypeDocument(String typeDocument) { this.typeDocument = typeDocument; }

    public Long getInscriptionId() { return inscriptionId; }
    public void setInscriptionId(Long inscriptionId) { this.inscriptionId = inscriptionId; }

    public Long getEleveId() { return eleveId; }
    public void setEleveId(Long eleveId) { this.eleveId = eleveId; }

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }

    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}
