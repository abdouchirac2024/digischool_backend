package com.digiSchool.digiSchool.storage.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.digiSchool.digiSchool.storage.dto.FileInfoDto;

public interface MongoFileStorageService {

    /**
     * Stocke un fichier dans MongoDB GridFS avec métadonnées
     *
     * @param file        Fichier à stocker (image ou PDF)
     * @param typeDocument Type : PHOTO_ELEVE, ACTE_NAISSANCE, CERTIFICAT_MEDICAL, BULLETIN_ANCIEN
     * @param inscriptionId ID de l'inscription dans MySQL (optionnel)
     * @param eleveId     ID de l'élève dans MySQL (optionnel)
     * @param tenant      Tenant de l'école
     * @return Informations du fichier stocké (avec fileId)
     */
    FileInfoDto stocker(MultipartFile file, String typeDocument, Long inscriptionId, Long eleveId, String tenant);

    /**
     * Récupère les métadonnées d'un fichier
     */
    FileInfoDto getInfo(String fileId);

    /**
     * Récupère les fichiers d'une inscription
     */
    List<FileInfoDto> getByInscription(Long inscriptionId, String tenant);

    /**
     * Récupère les fichiers d'un élève
     */
    List<FileInfoDto> getByEleve(Long eleveId, String tenant);

    /**
     * Télécharge le contenu binaire d'un fichier
     *
     * @return tableau d'octets du fichier
     */
    byte[] telecharger(String fileId);

    /**
     * Supprime un fichier de GridFS
     */
    void supprimer(String fileId);
}
