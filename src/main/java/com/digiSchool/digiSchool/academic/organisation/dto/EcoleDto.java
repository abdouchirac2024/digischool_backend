package com.digiSchool.digiSchool.academic.organisation.dto;

import java.time.LocalDateTime;
import com.digiSchool.digiSchool.academic.organisation.model.StatutEcole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ecole / Etablissement scolaire")
public class EcoleDto {

    @Schema(description = "ID unique de l'ecole", example = "1")
    private Long id;

    @Schema(description = "Code unique de l'ecole", example = "ECB-001")
    private String codeEcole;

    @Schema(description = "Nom de l'ecole", example = "Ecole Bilingue La Victoire")
    private String nom;

    @Schema(description = "Adresse de l'ecole")
    private String adresse;

    @Schema(description = "Telephone", example = "+237 677 123 456")
    private String telephone;

    @Schema(description = "Email de l'ecole", example = "contact@lavictoire.cm")
    private String email;

    @Schema(description = "Statut de l'ecole")
    private StatutEcole statutEcole;

    @Schema(description = "Slug pour URL personnalisee", example = "ecole-bilingue-la-victoire")
    private String slug;

    @Schema(description = "Motif de rejet si rejetee")
    private String motifRejet;

    @Schema(description = "Date de validation")
    private LocalDateTime dateValidation;

    @Schema(description = "Nombre d'eleves")
    private Integer nombreEleves;

    @Schema(description = "URL du logo")
    private String logoUrl;

    @Schema(description = "Couleur primaire", example = "#2302B3")
    private String couleurPrimaire;

    @Schema(description = "Couleur secondaire", example = "#4318FF")
    private String couleurSecondaire;

    @Schema(description = "Type de secteur")
    private String typeSecteur;

    @Schema(description = "Type d'etablissement")
    private String typeEtablissement;

    @Schema(description = "Sous-systeme")
    private String sousSysteme;

    @Schema(description = "Boite postale")
    private String boitePostale;

    @Schema(description = "Site web")
    private String siteWeb;

    @Schema(description = "Devise/slogan de l'ecole")
    private String devise;

    @Schema(description = "Annee de fondation")
    private Integer anneeFondation;

    @Schema(description = "Numero d'autorisation ministerielle")
    private String numeroAutorisation;

    // Relations
    @Schema(description = "ID du quartier")
    private Long quartierId;

    @Schema(description = "Nom du quartier", accessMode = Schema.AccessMode.READ_ONLY)
    private String quartierNom;

    @Schema(description = "Nom de la ville", accessMode = Schema.AccessMode.READ_ONLY)
    private String villeNom;

    @Schema(description = "Nom du departement", accessMode = Schema.AccessMode.READ_ONLY)
    private String departementNom;

    @Schema(description = "Nom de la region", accessMode = Schema.AccessMode.READ_ONLY)
    private String regionNom;

    @Schema(description = "Nom du directeur", accessMode = Schema.AccessMode.READ_ONLY)
    private String directeurNom;

    @Schema(description = "Email du directeur", accessMode = Schema.AccessMode.READ_ONLY)
    private String directeurEmail;

    @Schema(description = "Nombre de classes", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer nombreClasses;

    @Schema(description = "Date de creation")
    private LocalDateTime createdAt;

    @Schema(description = "Date de mise a jour")
    private LocalDateTime updatedAt;

    // ===== Getters & Setters =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeEcole() {
        return codeEcole;
    }

    public void setCodeEcole(String codeEcole) {
        this.codeEcole = codeEcole;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
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

    public StatutEcole getStatutEcole() {
        return statutEcole;
    }

    public void setStatutEcole(StatutEcole statutEcole) {
        this.statutEcole = statutEcole;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getMotifRejet() {
        return motifRejet;
    }

    public void setMotifRejet(String motifRejet) {
        this.motifRejet = motifRejet;
    }

    public LocalDateTime getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(LocalDateTime dateValidation) {
        this.dateValidation = dateValidation;
    }

    public Integer getNombreEleves() {
        return nombreEleves;
    }

    public void setNombreEleves(Integer nombreEleves) {
        this.nombreEleves = nombreEleves;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getCouleurPrimaire() {
        return couleurPrimaire;
    }

    public void setCouleurPrimaire(String couleurPrimaire) {
        this.couleurPrimaire = couleurPrimaire;
    }

    public String getCouleurSecondaire() {
        return couleurSecondaire;
    }

    public void setCouleurSecondaire(String couleurSecondaire) {
        this.couleurSecondaire = couleurSecondaire;
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

    public Long getQuartierId() {
        return quartierId;
    }

    public void setQuartierId(Long quartierId) {
        this.quartierId = quartierId;
    }

    public String getQuartierNom() {
        return quartierNom;
    }

    public void setQuartierNom(String quartierNom) {
        this.quartierNom = quartierNom;
    }

    public String getVilleNom() {
        return villeNom;
    }

    public void setVilleNom(String villeNom) {
        this.villeNom = villeNom;
    }

    public String getDepartementNom() {
        return departementNom;
    }

    public void setDepartementNom(String departementNom) {
        this.departementNom = departementNom;
    }

    public String getRegionNom() {
        return regionNom;
    }

    public void setRegionNom(String regionNom) {
        this.regionNom = regionNom;
    }

    public String getDirecteurNom() {
        return directeurNom;
    }

    public void setDirecteurNom(String directeurNom) {
        this.directeurNom = directeurNom;
    }

    public String getDirecteurEmail() {
        return directeurEmail;
    }

    public void setDirecteurEmail(String directeurEmail) {
        this.directeurEmail = directeurEmail;
    }

    public Integer getNombreClasses() {
        return nombreClasses;
    }

    public void setNombreClasses(Integer nombreClasses) {
        this.nombreClasses = nombreClasses;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
