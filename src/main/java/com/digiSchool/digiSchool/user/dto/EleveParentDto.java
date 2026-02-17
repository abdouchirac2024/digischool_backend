package com.digiSchool.digiSchool.user.dto;

import java.time.LocalDateTime;

import com.digiSchool.digiSchool.user.model.TypeRelation;

public class EleveParentDto {

    private Long idEleveParent;
    private Long eleveId;
    private String eleveMatricule;
    private String eleveNom;
    private String elevePrenom;
    private String eleveClasse;
    private Long parentId;
    private String parentMatricule;
    private String parentNom;
    private String parentPrenom;
    private String parentTelephone;
    private String parentEmail;
    private TypeRelation typeRelation;
    private Boolean estPrincipal;
    private Boolean autorisePriseEnCharge;
    private Boolean autoriseUrgence;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;

    // Getters and Setters
    public Long getIdEleveParent() {
        return idEleveParent;
    }

    public void setIdEleveParent(Long idEleveParent) {
        this.idEleveParent = idEleveParent;
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

    public String getEleveClasse() {
        return eleveClasse;
    }

    public void setEleveClasse(String eleveClasse) {
        this.eleveClasse = eleveClasse;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentMatricule() {
        return parentMatricule;
    }

    public void setParentMatricule(String parentMatricule) {
        this.parentMatricule = parentMatricule;
    }

    public String getParentNom() {
        return parentNom;
    }

    public void setParentNom(String parentNom) {
        this.parentNom = parentNom;
    }

    public String getParentPrenom() {
        return parentPrenom;
    }

    public void setParentPrenom(String parentPrenom) {
        this.parentPrenom = parentPrenom;
    }

    public String getParentTelephone() {
        return parentTelephone;
    }

    public void setParentTelephone(String parentTelephone) {
        this.parentTelephone = parentTelephone;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
    }

    public TypeRelation getTypeRelation() {
        return typeRelation;
    }

    public void setTypeRelation(TypeRelation typeRelation) {
        this.typeRelation = typeRelation;
    }

    public Boolean getEstPrincipal() {
        return estPrincipal;
    }

    public void setEstPrincipal(Boolean estPrincipal) {
        this.estPrincipal = estPrincipal;
    }

    public Boolean getAutorisePriseEnCharge() {
        return autorisePriseEnCharge;
    }

    public void setAutorisePriseEnCharge(Boolean autorisePriseEnCharge) {
        this.autorisePriseEnCharge = autorisePriseEnCharge;
    }

    public Boolean getAutoriseUrgence() {
        return autoriseUrgence;
    }

    public void setAutoriseUrgence(Boolean autoriseUrgence) {
        this.autoriseUrgence = autoriseUrgence;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
}
