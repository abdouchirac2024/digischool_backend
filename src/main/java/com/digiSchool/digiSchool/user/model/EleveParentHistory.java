package com.digiSchool.digiSchool.user.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

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

@Entity
@Table(
    name = "eleve_parent_history",
    indexes = {
        @Index(name = "idx_ep_history_eleve_parent", columnList = "eleve_parent_id"),
        @Index(name = "idx_ep_history_eleve", columnList = "eleve_id"),
        @Index(name = "idx_ep_history_parent", columnList = "parent_id"),
        @Index(name = "idx_ep_history_action", columnList = "action"),
        @Index(name = "idx_ep_history_tenant", columnList = "tenant")
    }
)
public class EleveParentHistory extends TenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistory;

    @ManyToOne
    @JoinColumn(name = "eleve_parent_id", nullable = true)
    private EleveParent eleveParent;

    @Column(name = "eleve_id", nullable = false)
    private Long eleveId;

    @Column(name = "parent_id", nullable = false)
    private Long parentId;

    @Column(name = "ancienne_relation")
    @Enumerated(EnumType.STRING)
    private TypeRelation ancienneRelation;

    @Column(name = "nouvelle_relation")
    @Enumerated(EnumType.STRING)
    private TypeRelation nouvelleRelation;

    @Column(name = "ancien_est_principal")
    private Boolean ancienEstPrincipal;

    @Column(name = "nouveau_est_principal")
    private Boolean nouveauEstPrincipal;

    @Column(nullable = false, length = 50)
    private String action; // CREATE, UPDATE, DELETE

    @Column(columnDefinition = "TEXT")
    private String motif;

    @Column(name = "modifie_par", nullable = false)
    private Long modifiePar;

    @CreationTimestamp
    @Column(name = "date_modification", updatable = false)
    private LocalDateTime dateModification;

    // Getters and Setters
    public Long getIdHistory() {
        return idHistory;
    }

    public void setIdHistory(Long idHistory) {
        this.idHistory = idHistory;
    }

    public EleveParent getEleveParent() {
        return eleveParent;
    }

    public void setEleveParent(EleveParent eleveParent) {
        this.eleveParent = eleveParent;
    }

    public Long getEleveId() {
        return eleveId;
    }

    public void setEleveId(Long eleveId) {
        this.eleveId = eleveId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public TypeRelation getAncienneRelation() {
        return ancienneRelation;
    }

    public void setAncienneRelation(TypeRelation ancienneRelation) {
        this.ancienneRelation = ancienneRelation;
    }

    public TypeRelation getNouvelleRelation() {
        return nouvelleRelation;
    }

    public void setNouvelleRelation(TypeRelation nouvelleRelation) {
        this.nouvelleRelation = nouvelleRelation;
    }

    public Boolean getAncienEstPrincipal() {
        return ancienEstPrincipal;
    }

    public void setAncienEstPrincipal(Boolean ancienEstPrincipal) {
        this.ancienEstPrincipal = ancienEstPrincipal;
    }

    public Boolean getNouveauEstPrincipal() {
        return nouveauEstPrincipal;
    }

    public void setNouveauEstPrincipal(Boolean nouveauEstPrincipal) {
        this.nouveauEstPrincipal = nouveauEstPrincipal;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Long getModifiePar() {
        return modifiePar;
    }

    public void setModifiePar(Long modifiePar) {
        this.modifiePar = modifiePar;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }
}
