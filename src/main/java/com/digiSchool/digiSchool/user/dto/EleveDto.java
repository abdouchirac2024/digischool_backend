package com.digiSchool.digiSchool.user.dto;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EleveDto {
    private Long idEleve;
    private String matricule;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String tenant;
    private String lieuNaissance;
    private String sexe;
    private String quartierNom;

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
}
