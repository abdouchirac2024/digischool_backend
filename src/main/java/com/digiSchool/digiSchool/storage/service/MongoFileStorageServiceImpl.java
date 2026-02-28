package com.digiSchool.digiSchool.storage.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.digiSchool.digiSchool.storage.dto.FileInfoDto;
import com.mongodb.client.gridfs.model.GridFSFile;

@Service
public class MongoFileStorageServiceImpl implements MongoFileStorageService {

    // Types de documents acceptés
    private static final Set<String> TYPES_VALIDES = Set.of(
            "PHOTO_ELEVE",
            "ACTE_NAISSANCE",
            "CERTIFICAT_MEDICAL",
            "BULLETIN_ANCIEN",
            "PIECE_IDENTITE_PARENT",
            "AUTRE"
    );

    // Types MIME autorisés (images + PDF uniquement)
    private static final Set<String> MIME_AUTORISES = Set.of(
            "image/jpeg",
            "image/png",
            "image/jpg",
            "application/pdf"
    );

    private final GridFsTemplate gridFsTemplate;

    public MongoFileStorageServiceImpl(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    @Override
    public FileInfoDto stocker(MultipartFile file, String typeDocument, Long inscriptionId, Long eleveId, String tenant) {
        // Validation du type de document
        String type = typeDocument != null ? typeDocument.toUpperCase() : "AUTRE";
        if (!TYPES_VALIDES.contains(type)) {
            throw new RuntimeException("Type de document invalide : " + typeDocument
                    + ". Valeurs acceptées : " + TYPES_VALIDES);
        }

        // Validation du type MIME
        String contentType = file.getContentType();
        if (contentType == null || !MIME_AUTORISES.contains(contentType)) {
            throw new RuntimeException("Type de fichier non autorisé : " + contentType
                    + ". Seuls les images (JPEG, PNG) et PDFs sont acceptés.");
        }

        // Validation taille (max 20 MB)
        if (file.getSize() > 20 * 1024 * 1024) {
            throw new RuntimeException("Fichier trop volumineux. Taille maximum : 20 MB.");
        }

        // Construction des métadonnées MongoDB
        Document metadata = new Document();
        metadata.put("typeDocument", type);
        metadata.put("tenant", tenant);
        metadata.put("uploadedAt", LocalDateTime.now().toString());
        if (inscriptionId != null) metadata.put("inscriptionId", inscriptionId);
        if (eleveId != null) metadata.put("eleveId", eleveId);

        // Nom de fichier sécurisé
        String filename = StringUtils.cleanPath(file.getOriginalFilename() != null
                ? file.getOriginalFilename() : "fichier_inconnu");

        // Stockage dans GridFS
        ObjectId fileId;
        try {
            fileId = gridFsTemplate.store(
                    file.getInputStream(),
                    filename,
                    contentType,
                    metadata
            );
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du stockage du fichier : " + e.getMessage());
        }

        // Construire la réponse
        FileInfoDto dto = new FileInfoDto();
        dto.setFileId(fileId.toHexString());
        dto.setFilename(filename);
        dto.setContentType(contentType);
        dto.setSize(file.getSize());
        dto.setTypeDocument(type);
        dto.setInscriptionId(inscriptionId);
        dto.setEleveId(eleveId);
        dto.setTenant(tenant);
        dto.setDownloadUrl("/api/files/" + fileId.toHexString());
        dto.setUploadedAt(LocalDateTime.now());

        return dto;
    }

    @Override
    public FileInfoDto getInfo(String fileId) {
        GridFSFile file = gridFsTemplate.findOne(
                new Query(Criteria.where("_id").is(new ObjectId(fileId)))
        );

        if (file == null) {
            throw new RuntimeException("Fichier introuvable avec l'id : " + fileId);
        }

        return gridFsFileToDto(file);
    }

    @Override
    public List<FileInfoDto> getByInscription(Long inscriptionId, String tenant) {
        List<FileInfoDto> result = new ArrayList<>();
        gridFsTemplate.find(
                new Query(Criteria.where("metadata.inscriptionId").is(inscriptionId)
                        .and("metadata.tenant").is(tenant))
        ).forEach(file -> result.add(gridFsFileToDto(file)));
        return result;
    }

    @Override
    public List<FileInfoDto> getByEleve(Long eleveId, String tenant) {
        List<FileInfoDto> result = new ArrayList<>();
        gridFsTemplate.find(
                new Query(Criteria.where("metadata.eleveId").is(eleveId)
                        .and("metadata.tenant").is(tenant))
        ).forEach(file -> result.add(gridFsFileToDto(file)));
        return result;
    }

    @Override
    public byte[] telecharger(String fileId) {
        GridFSFile file = gridFsTemplate.findOne(
                new Query(Criteria.where("_id").is(new ObjectId(fileId)))
        );

        if (file == null) {
            throw new RuntimeException("Fichier introuvable avec l'id : " + fileId);
        }

        try {
            GridFsResource resource = gridFsTemplate.getResource(file);
            return resource.getInputStream().readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du téléchargement du fichier : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(String fileId) {
        gridFsTemplate.delete(
                new Query(Criteria.where("_id").is(new ObjectId(fileId)))
        );
    }

    private FileInfoDto gridFsFileToDto(GridFSFile file) {
        FileInfoDto dto = new FileInfoDto();
        dto.setFileId(file.getObjectId().toHexString());
        dto.setFilename(file.getFilename());
        dto.setSize(file.getLength());
        dto.setDownloadUrl("/api/files/" + file.getObjectId().toHexString());

        if (file.getMetadata() != null) {
            Document meta = file.getMetadata();
            dto.setContentType(meta.getString("_contentType"));
            dto.setTypeDocument(meta.getString("typeDocument"));
            dto.setTenant(meta.getString("tenant"));

            Object inscriptionIdObj = meta.get("inscriptionId");
            if (inscriptionIdObj instanceof Number) {
                dto.setInscriptionId(((Number) inscriptionIdObj).longValue());
            }

            Object eleveIdObj = meta.get("eleveId");
            if (eleveIdObj instanceof Number) {
                dto.setEleveId(((Number) eleveIdObj).longValue());
            }

            String uploadedAt = meta.getString("uploadedAt");
            if (uploadedAt != null) {
                dto.setUploadedAt(LocalDateTime.parse(uploadedAt));
            }
        }

        return dto;
    }
}
