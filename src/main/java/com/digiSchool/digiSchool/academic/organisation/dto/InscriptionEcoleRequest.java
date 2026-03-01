package com.digiSchool.digiSchool.academic.organisation.dto;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Requete d'inscription d'une ecole")
public class InscriptionEcoleRequest {

    @NotBlank(message = "Le nom de l'ecole est obligatoire")
    @Size(min = 3, max = 200)
    @Schema(description = "Nom de l'ecole", example = "Ecole Bilingue La Victoire")
    private String schoolName;

    @NotNull(message = "Le quartier est obligatoire")
    @Schema(description = "ID du quartier (hierarchie geographique)", example = "1")
    private Long quartierId;

    @NotBlank(message = "Le type de secteur est obligatoire")
    @Schema(description = "Type de secteur: PUBLIC, PRIVE_LAIC, PRIVE_CONFESSIONNEL, PRIVE_COMMUNAUTAIRE", example = "PRIVE_LAIC")
    private String typeSecteur;

    @Schema(description = "Type d'etablissement: MATERNELLE, PRIMAIRE, SECONDAIRE_GENERAL, SECONDAIRE_TECHNIQUE, BILINGUE, COMPLEXE_SCOLAIRE", example = "COMPLEXE_SCOLAIRE")
    private String typeEtablissement;

    @Schema(description = "Sous-systeme: FRANCOPHONE, ANGLOPHONE, BILINGUE", example = "BILINGUE")
    private String sousSysteme;

    @NotBlank(message = "L'adresse est obligatoire")
    @Schema(description = "Adresse de l'ecole")
    private String adresse;

    @Schema(description = "Telephone de l'ecole")
    private String telephone;

    @NotBlank(message = "L'email de l'ecole est obligatoire")
    @Email(message = "Email invalide")
    @Schema(description = "Email de l'ecole", example = "contact@ecole.cm")
    private String email;

    @Schema(description = "Nombre d'eleves", example = "300")
    private Integer nombreEleves;

    @Schema(description = "Boite postale", example = "BP 1234")
    private String boitePostale;

    @Schema(description = "Site web de l'ecole", example = "https://www.ecole.cm")
    private String siteWeb;

    @Schema(description = "Devise/slogan de l'ecole", example = "L'excellence pour tous")
    private String devise;

    @Schema(description = "Annee de fondation", example = "1995")
    private Integer anneeFondation;

    @Schema(description = "Numero d'autorisation ministerielle", example = "AUTH-2024-001")
    private String numeroAutorisation;

    @NotBlank(message = "Le prenom du directeur est obligatoire")
    @Schema(description = "Prenom du directeur")
    private String adminPrenom;

    @NotBlank(message = "Le nom du directeur est obligatoire")
    @Schema(description = "Nom du directeur")
    private String adminNom;

    @NotBlank(message = "L'email du directeur est obligatoire")
    @Email(message = "Email invalide")
    @Schema(description = "Email du directeur")
    private String adminEmail;

    @Schema(description = "Telephone du directeur")
    private String adminTelephone;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit faire au moins 6 caracteres")
    @Schema(description = "Mot de passe")
    private String password;

    // ===== Getters & Setters =====

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Long getQuartierId() {
        return quartierId;
    }

    public void setQuartierId(Long quartierId) {
        this.quartierId = quartierId;
    }

    public String getTypeSecteur() {
        return typeSecteur;
    }

    public void setTypeSecteur(String typeSecteur) {
        this.typeSecteur = typeSecteur;
    }

    public String getTypeEtablissement() {
        return typeEtablissement;
    }

    public void setTypeEtablissement(String typeEtablissement) {
        this.typeEtablissement = typeEtablissement;
    }

    public String getSousSysteme() {
        return sousSysteme;
    }

    public void setSousSysteme(String sousSysteme) {
        this.sousSysteme = sousSysteme;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getNombreEleves() {
        return nombreEleves;
    }

    public void setNombreEleves(Integer nombreEleves) {
        this.nombreEleves = nombreEleves;
    }

    public String getBoitePostale() {
        return boitePostale;
    }

    public void setBoitePostale(String boitePostale) {
        this.boitePostale = boitePostale;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public Integer getAnneeFondation() {
        return anneeFondation;
    }

    public void setAnneeFondation(Integer anneeFondation) {
        this.anneeFondation = anneeFondation;
    }

    public String getNumeroAutorisation() {
        return numeroAutorisation;
    }

    public void setNumeroAutorisation(String numeroAutorisation) {
        this.numeroAutorisation = numeroAutorisation;
    }

    public String getAdminPrenom() {
        return adminPrenom;
    }

    public void setAdminPrenom(String adminPrenom) {
        this.adminPrenom = adminPrenom;
    }

    public String getAdminNom() {
        return adminNom;
    }

    public void setAdminNom(String adminNom) {
        this.adminNom = adminNom;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getAdminTelephone() {
        return adminTelephone;
    }

    public void setAdminTelephone(String adminTelephone) {
        this.adminTelephone = adminTelephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
