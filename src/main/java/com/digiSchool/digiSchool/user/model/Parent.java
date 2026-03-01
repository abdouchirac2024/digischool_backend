package com.digiSchool.digiSchool.user.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.UpdateTimestamp;

import com.digiSchool.digiSchool.Exceptionconfig.model.Quartier;
import com.digiSchool.digiSchool.Exceptionconfig.model.TenantEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Filter(name = "tenantFilter", condition = "tenant = :tenant")
@Table(
    name = "parent",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "matricule_parent"),
        @UniqueConstraint(columnNames = {"email", "tenant"}),
        @UniqueConstraint(columnNames = {"telephone", "tenant"})
    },
    indexes = {
        @Index(name = "idx_parent_matricule", columnList = "matricule_parent"),
        @Index(name = "idx_parent_email", columnList = "email"),
        @Index(name = "idx_parent_telephone", columnList = "telephone"),
        @Index(name = "idx_parent_quartier", columnList = "quartier_id"),
        @Index(name = "idx_parent_tenant", columnList = "tenant")
    }
)
public class Parent extends TenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idParent;

    @Column(name = "matricule_parent", nullable = false, unique = true, updatable = false, length = 50)
    private String matriculeParent;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, unique = true, length = 20)
    private String telephone;

    @Column(name = "telephone_secondaire", length = 20)
    private String telephoneSecondaire;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String adresse;

//    @ManyToOne
//    @JoinColumn(name = "quartier_id", nullable = false)
//    private Quartier quartier;

    @Column(length = 100)
    private String profession;

    @Column(length = 255)
    private String employeur;

    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    @Column(name = "piece_identite_type", length = 50)
    private String pieceIdentiteType;

    @Column(name = "piece_identite_numero", length = 50)
    private String pieceIdentiteNumero;

    @Column(nullable = false)
    private Boolean actif = true;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<EleveParent> eleveParents;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // Getters and Setters
    public Long getIdParent() {
        return idParent;
    }

    public void setIdParent(Long idParent) {
        this.idParent = idParent;
    }

    public String getMatriculeParent() {
        return matriculeParent;
    }

    public void setMatriculeParent(String matriculeParent) {
        this.matriculeParent = matriculeParent;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTelephoneSecondaire() {
        return telephoneSecondaire;
    }

    public void setTelephoneSecondaire(String telephoneSecondaire) {
        this.telephoneSecondaire = telephoneSecondaire;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

//    public Quartier getQuartier() {
//        return quartier;
//    }
//
//    public void setQuartier(Quartier quartier) {
//        this.quartier = quartier;
//    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getEmployeur() {
        return employeur;
    }

    public void setEmployeur(String employeur) {
        this.employeur = employeur;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPieceIdentiteType() {
        return pieceIdentiteType;
    }

    public void setPieceIdentiteType(String pieceIdentiteType) {
        this.pieceIdentiteType = pieceIdentiteType;
    }

    public String getPieceIdentiteNumero() {
        return pieceIdentiteNumero;
    }

    public void setPieceIdentiteNumero(String pieceIdentiteNumero) {
        this.pieceIdentiteNumero = pieceIdentiteNumero;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public List<EleveParent> getEleveParents() {
        return eleveParents;
    }

    public void setEleveParents(List<EleveParent> eleveParents) {
        this.eleveParents = eleveParents;
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

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }
}
