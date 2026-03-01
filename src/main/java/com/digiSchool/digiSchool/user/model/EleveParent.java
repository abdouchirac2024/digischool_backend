package com.digiSchool.digiSchool.user.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.digiSchool.digiSchool.Exceptionconfig.model.TenantEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "eleve_parent",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"eleve_id", "parent_id"})
    },
    indexes = {
        @Index(name = "idx_eleve_parent_eleve", columnList = "eleve_id"),
        @Index(name = "idx_eleve_parent_parent", columnList = "parent_id"),
        @Index(name = "idx_eleve_parent_principal", columnList = "est_principal"),
        @Index(name = "idx_eleve_parent_tenant", columnList = "tenant")
    }
)
public class EleveParent extends TenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEleveParent;

    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinColumn(name = "eleve_id")
    private Eleve eleve;

    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_relation", nullable = false)
    private TypeRelation typeRelation;

    @Column(name = "est_principal", nullable = false)
    private Boolean estPrincipal = false;

    @Column(name = "autorise_prise_en_charge", nullable = false)
    private Boolean autorisePriseEnCharge = true;

    @Column(name = "autorise_urgence", nullable = false)
    private Boolean autoriseUrgence = true;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    // Getters and Setters
    public Long getIdEleveParent() {
        return idEleveParent;
    }

    public void setIdEleveParent(Long idEleveParent) {
        this.idEleveParent = idEleveParent;
    }

    public Eleve getEleve() {
        return eleve;
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
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

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }
}
