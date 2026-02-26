package com.digiSchool.digiSchool.academic.organisation.dto;

import java.time.LocalDate;
import java.util.List;

public class InscriptionDto {
    private Long idInscription;
    private String numeroInscription;
    private LocalDate dateInscription;
    private Double montantTotal;
    private String statutInscription;
    private String motifAnnulation;

    // Finances
    private Double remise;
    private Double fraisTransport;
    private Double fraisCantine;
    private Double fraisAssurance;

    // Eleve
    private Long eleveId;
    private String eleveMatricule;
    private String eleveNom;
    private String elevePrenom;
    private String elevePhotoUrl;
    private String eleveActeNaissanceUrl;
    private String eleveCertificatMedicalUrl;
    private String eleveBulletinUrl;
    private LocalDate eleveDateNaissance;
    private String eleveLieuNaissance;
    private String eleveNationalite;
    private String eleveSexe;
    private String eleveQuartier;
    private String eleveVille;

    // Parents
    private List<ParentSummaryDto> parents;

    // Classe
    private Long classeId;
    private String classeNom;
    private String classeNiveau;
    private Double fraisScolarite;

    // Annee scolaire
    private Long anneeScolaireId;
    private String anneeScolaireLibelle;

    // Echeancier
    private List<EcheanceDto> echeances;

    // Identifiants generes (retournes uniquement lors de la creation)
    private String generatedPassword;
    private String generatedEmail;

    // Getters & Setters

    public Long getIdInscription() {
        return idInscription;
    }

    public void setIdInscription(Long idInscription) {
        this.idInscription = idInscription;
    }

    public String getNumeroInscription() {
        return numeroInscription;
    }

    public void setNumeroInscription(String numeroInscription) {
        this.numeroInscription = numeroInscription;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    public Double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(Double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public String getStatutInscription() {
        return statutInscription;
    }

    public void setStatutInscription(String statutInscription) {
        this.statutInscription = statutInscription;
    }

    public String getMotifAnnulation() {
        return motifAnnulation;
    }

    public void setMotifAnnulation(String motifAnnulation) {
        this.motifAnnulation = motifAnnulation;
    }

    public Long getEleveId() {
        return eleveId;
    }

    public void setEleveId(Long eleveId) {
        this.eleveId = eleveId;
    }

    public String getEleveMatricule() {
        return eleveMatricule;
    }

    public void setEleveMatricule(String eleveMatricule) {
        this.eleveMatricule = eleveMatricule;
    }

    public String getEleveNom() {
        return eleveNom;
    }

    public void setEleveNom(String eleveNom) {
        this.eleveNom = eleveNom;
    }

    public String getElevePrenom() {
        return elevePrenom;
    }

    public void setElevePrenom(String elevePrenom) {
        this.elevePrenom = elevePrenom;
    }

    public Long getClasseId() {
        return classeId;
    }

    public void setClasseId(Long classeId) {
        this.classeId = classeId;
    }

    public String getClasseNom() {
        return classeNom;
    }

    public void setClasseNom(String classeNom) {
        this.classeNom = classeNom;
    }

    public String getClasseNiveau() {
        return classeNiveau;
    }

    public void setClasseNiveau(String classeNiveau) {
        this.classeNiveau = classeNiveau;
    }

    public Double getFraisScolarite() {
        return fraisScolarite;
    }

    public void setFraisScolarite(Double fraisScolarite) {
        this.fraisScolarite = fraisScolarite;
    }

    public Long getAnneeScolaireId() {
        return anneeScolaireId;
    }

    public void setAnneeScolaireId(Long anneeScolaireId) {
        this.anneeScolaireId = anneeScolaireId;
    }

    public String getAnneeScolaireLibelle() {
        return anneeScolaireLibelle;
    }

    public void setAnneeScolaireLibelle(String anneeScolaireLibelle) {
        this.anneeScolaireLibelle = anneeScolaireLibelle;
    }

    public List<EcheanceDto> getEcheances() {
        return echeances;
    }

    public void setEcheances(List<EcheanceDto> echeances) {
        this.echeances = echeances;
    }

    public String getGeneratedPassword() {
        return generatedPassword;
    }

    public void setGeneratedPassword(String generatedPassword) {
        this.generatedPassword = generatedPassword;
    }

    public String getGeneratedEmail() {
        return generatedEmail;
    }

    public void setGeneratedEmail(String generatedEmail) {
        this.generatedEmail = generatedEmail;
    }

    public Double getRemise() {
        return remise;
    }

    public void setRemise(Double remise) {
        this.remise = remise;
    }

    public Double getFraisTransport() {
        return fraisTransport;
    }

    public void setFraisTransport(Double fraisTransport) {
        this.fraisTransport = fraisTransport;
    }

    public Double getFraisCantine() {
        return fraisCantine;
    }

    public void setFraisCantine(Double fraisCantine) {
        this.fraisCantine = fraisCantine;
    }

    public Double getFraisAssurance() {
        return fraisAssurance;
    }

    public void setFraisAssurance(Double fraisAssurance) {
        this.fraisAssurance = fraisAssurance;
    }

    public String getElevePhotoUrl() {
        return elevePhotoUrl;
    }

    public void setElevePhotoUrl(String elevePhotoUrl) {
        this.elevePhotoUrl = elevePhotoUrl;
    }

    public String getEleveActeNaissanceUrl() {
        return eleveActeNaissanceUrl;
    }

    public void setEleveActeNaissanceUrl(String eleveActeNaissanceUrl) {
        this.eleveActeNaissanceUrl = eleveActeNaissanceUrl;
    }

    public String getEleveCertificatMedicalUrl() {
        return eleveCertificatMedicalUrl;
    }

    public void setEleveCertificatMedicalUrl(String eleveCertificatMedicalUrl) {
        this.eleveCertificatMedicalUrl = eleveCertificatMedicalUrl;
    }

    public String getEleveBulletinUrl() {
        return eleveBulletinUrl;
    }

    public void setEleveBulletinUrl(String eleveBulletinUrl) {
        this.eleveBulletinUrl = eleveBulletinUrl;
    }

    public LocalDate getEleveDateNaissance() {
        return eleveDateNaissance;
    }

    public void setEleveDateNaissance(LocalDate eleveDateNaissance) {
        this.eleveDateNaissance = eleveDateNaissance;
    }

    public String getEleveLieuNaissance() {
        return eleveLieuNaissance;
    }

    public void setEleveLieuNaissance(String eleveLieuNaissance) {
        this.eleveLieuNaissance = eleveLieuNaissance;
    }

    public String getEleveNationalite() {
        return eleveNationalite;
    }

    public void setEleveNationalite(String eleveNationalite) {
        this.eleveNationalite = eleveNationalite;
    }

    public String getEleveSexe() {
        return eleveSexe;
    }

    public void setEleveSexe(String eleveSexe) {
        this.eleveSexe = eleveSexe;
    }

    public String getEleveQuartier() {
        return eleveQuartier;
    }

    public void setEleveQuartier(String eleveQuartier) {
        this.eleveQuartier = eleveQuartier;
    }

    public String getEleveVille() {
        return eleveVille;
    }

    public void setEleveVille(String eleveVille) {
        this.eleveVille = eleveVille;
    }

    public List<ParentSummaryDto> getParents() {
        return parents;
    }

    public void setParents(List<ParentSummaryDto> parents) {
        this.parents = parents;
    }
}
