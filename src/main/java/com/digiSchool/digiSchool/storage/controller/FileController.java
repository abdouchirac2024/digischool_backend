package com.digiSchool.digiSchool.storage.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.digiSchool.digiSchool.Exceptionconfig.service.TenantContext;
import com.digiSchool.digiSchool.auth.model.RoleType;
import com.digiSchool.digiSchool.auth.model.User;
import com.digiSchool.digiSchool.auth.security.RequireRole;
import com.digiSchool.digiSchool.storage.dto.FileInfoDto;
import com.digiSchool.digiSchool.storage.service.MongoFileStorageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/files")
@CrossOrigin
@Tag(name = "Fichiers (MongoDB GridFS)", description = "Upload et téléchargement d'images et PDFs pour les inscriptions")
public class FileController {

    private final MongoFileStorageService fileStorageService;

    public FileController(MongoFileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @Operation(
        summary = "Uploader un fichier (image ou PDF) pour une inscription d'élève",
        description = "Types de documents acceptés : PHOTO_ELEVE, ACTE_NAISSANCE, CERTIFICAT_MEDICAL, BULLETIN_ANCIEN, PIECE_IDENTITE_PARENT, AUTRE\n" +
                      "Formats acceptés : image/jpeg, image/png, application/pdf\n" +
                      "Taille max : 20 MB"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Fichier uploadé avec succès"),
        @ApiResponse(responseCode = "400", description = "Type de fichier non autorisé ou données invalides"),
        @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RequireRole({RoleType.ADMIN_ECOLE, RoleType.SECRETAIRE, RoleType.ENSEIGNANT})
    public ResponseEntity<FileInfoDto> upload(
            @Parameter(description = "Fichier à uploader (image JPG/PNG ou PDF)")
            @RequestParam("file") MultipartFile file,

            @Parameter(description = "Type : PHOTO_ELEVE | ACTE_NAISSANCE | CERTIFICAT_MEDICAL | BULLETIN_ANCIEN | PIECE_IDENTITE_PARENT | AUTRE")
            @RequestParam("typeDocument") String typeDocument,

            @Parameter(description = "ID de l'inscription MySQL (optionnel)")
            @RequestParam(value = "inscriptionId", required = false) Long inscriptionId,

            @Parameter(description = "ID de l'élève MySQL (optionnel)")
            @RequestParam(value = "eleveId", required = false) Long eleveId,

            @AuthenticationPrincipal User currentUser
    ) {
        String tenant = TenantContext.getTenant();
        FileInfoDto result = fileStorageService.stocker(file, typeDocument, inscriptionId, eleveId, tenant);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Obtenir les informations d'un fichier (sans télécharger le contenu)")
    @GetMapping("/{fileId}/info")
    public ResponseEntity<FileInfoDto> getInfo(@PathVariable String fileId) {
        return ResponseEntity.ok(fileStorageService.getInfo(fileId));
    }

    @Operation(summary = "Télécharger un fichier par son ID MongoDB")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Fichier retourné en binaire"),
        @ApiResponse(responseCode = "404", description = "Fichier introuvable")
    })
    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> download(@PathVariable String fileId) {
        FileInfoDto info = fileStorageService.getInfo(fileId);
        byte[] content = fileStorageService.telecharger(fileId);

        String contentType = info.getContentType() != null
                ? info.getContentType() : "application/octet-stream";

        String disposition = contentType.startsWith("image/")
                ? "inline; filename=\"" + info.getFilename() + "\""
                : "attachment; filename=\"" + info.getFilename() + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition)
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(content.length))
                .body(content);
    }

    @Operation(summary = "Lister les fichiers d'une inscription")
    @GetMapping("/inscription/{inscriptionId}")
    @RequireRole({RoleType.ADMIN_ECOLE, RoleType.SECRETAIRE, RoleType.ENSEIGNANT})
    public ResponseEntity<List<FileInfoDto>> getByInscription(@PathVariable Long inscriptionId) {
        String tenant = TenantContext.getTenant();
        return ResponseEntity.ok(fileStorageService.getByInscription(inscriptionId, tenant));
    }

    @Operation(summary = "Lister les fichiers d'un élève")
    @GetMapping("/eleve/{eleveId}")
    @RequireRole({RoleType.ADMIN_ECOLE, RoleType.SECRETAIRE, RoleType.ENSEIGNANT})
    public ResponseEntity<List<FileInfoDto>> getByEleve(@PathVariable Long eleveId) {
        String tenant = TenantContext.getTenant();
        return ResponseEntity.ok(fileStorageService.getByEleve(eleveId, tenant));
    }

    @Operation(summary = "Supprimer un fichier")
    @DeleteMapping("/{fileId}")
    @RequireRole({RoleType.ADMIN_ECOLE, RoleType.SECRETAIRE})
    public ResponseEntity<Void> delete(@PathVariable String fileId) {
        fileStorageService.supprimer(fileId);
        return ResponseEntity.noContent().build();
    }
}
