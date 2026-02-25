package com.digiSchool.digiSchool.user.dto;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Eleve inscrit dans une ecole")
public class EleveDto {

    @Schema(description = "ID unique de l'eleve", example = "1")
    private Long idEleve;

    @Schema(description = "Matricule genere automatiquement", example = "ECB-2025-0001")
    private String matricule;

    @Schema(description = "Nom de famille", example = "Talla")
    private String nom;

    @Schema(description = "Prenom", example = "Amina")
    private String prenom;

    @Schema(description = "Date de naissance", example = "2012-05-15")
    private LocalDate dateNaissance;

    @Schema(description = "Tenant ID de l'ecole", example = "CM-CENTRE-ECOLE-001")
    private String tenant;

    @Schema(description = "Lieu de naissance", example = "Yaounde")
    private String lieuNaissance;

    @Schema(description = "Sexe (M ou F)", example = "F")
    private String sexe;

    @Schema(description = "Nationalite de l'eleve", example = "Camerounaise")
    private String nationalite;

    @Schema(description = "ID du quartier de residence")
    private Long quartierId;

    @Schema(description = "Nom du quartier de residence")
    private String quartierNom;

    @Schema(description = "URL de la photo de l'eleve")
    private String photoUrl;

    @Schema(description = "URL de l'acte de naissance numérisé")
    private String acteNaissanceUrl;

    @Schema(description = "URL du certificat médical numérisé")
    private String certificatMedicalUrl;

    @Schema(description = "URL du dernier bulletin numérisé")
    private String bulletinUrl;

    // Getters and Setters
    @JsonProperty("id")
    public Long getIdEleve() {
        return idEleve;
    }

    public void setIdEleve(Long idEleve) {
        this.idEleve = idEleve;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getQuartierNom() {
        return quartierNom;
    }

    public void setQuartierNom(String quartierNom) {
        this.quartierNom = quartierNom;
    }

    public String getLieuNaissance() {
        return lieuNaissance;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public Long getQuartierId() {
        return quartierId;
    }

    public void setQuartierId(Long quartierId) {
        this.quartierId = quartierId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getActeNaissanceUrl() {
        return acteNaissanceUrl;
    }

    public void setActeNaissanceUrl(String acteNaissanceUrl) {
        this.acteNaissanceUrl = acteNaissanceUrl;
    }

    public String getCertificatMedicalUrl() {
        return certificatMedicalUrl;
    }

    public void setCertificatMedicalUrl(String certificatMedicalUrl) {
        this.certificatMedicalUrl = certificatMedicalUrl;
    }

    public String getBulletinUrl() {
        return bulletinUrl;
    }

    public void setBulletinUrl(String bulletinUrl) {
        this.bulletinUrl = bulletinUrl;
    }
}
