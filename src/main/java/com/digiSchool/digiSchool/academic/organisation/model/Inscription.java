package com.digiSchool.digiSchool.academic.organisation.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.digiSchool.digiSchool.Exceptionconfig.model.TenantEntity;
import com.digiSchool.digiSchool.user.model.Eleve;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Inscription extends TenantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInscription;

    @Column(unique = true)
    private String numeroInscription;

    private LocalDate dateInscription;
    private Double montantTotal;

    private Double remise = 0.0;
    private Double fraisTransport = 0.0;
    private Double fraisCantine = 0.0;
    private Double fraisAssurance = 0.0;

    // Ancien champ garde pour compatibilite
    private Boolean statut;

    @Enumerated(EnumType.STRING)
    private StatutInscription statutInscription;

    private String motifAnnulation;

    @ManyToOne
    @JoinColumn(name = "eleve_id")
    private Eleve eleve;

    @ManyToOne
    @JoinColumn(name = "classe_id")
    private Classe classe;

    @ManyToOne
    @JoinColumn(name = "annee_id")
    private Anneescolaire anneeScolaire;

    @OneToMany(mappedBy = "inscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Echeance> echeances;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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

    public Boolean getStatut() {
        return statut;
    }

    public void setStatut(Boolean statut) {
        this.statut = statut;
    }

    public StatutInscription getStatutInscription() {
        return statutInscription;
    }

    public void setStatutInscription(StatutInscription statutInscription) {
        this.statutInscription = statutInscription;
    }

    public String getMotifAnnulation() {
        return motifAnnulation;
    }

    public void setMotifAnnulation(String motifAnnulation) {
        this.motifAnnulation = motifAnnulation;
    }

    public Eleve getEleve() {
        return eleve;
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public Anneescolaire getAnneeScolaire() {
        return anneeScolaire;
    }

    public void setAnneeScolaire(Anneescolaire anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
    }

    public List<Echeance> getEcheances() {
        return echeances;
    }

    public void setEcheances(List<Echeance> echeances) {
        this.echeances = echeances;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
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
}
