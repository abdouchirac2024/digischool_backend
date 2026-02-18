package com.digiSchool.digiSchool.academic.organisation.model;

import java.time.LocalDate;

import com.digiSchool.digiSchool.Exceptionconfig.model.TenantEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Echeance extends TenantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEcheance;

    @ManyToOne
    @JoinColumn(name = "inscription_id")
    private Inscription inscription;

    private Integer numero;
    private String libelle;
    private Double montant;
    private LocalDate dateEcheance;
    private LocalDate datePaiement;

    @Enumerated(EnumType.STRING)
    private StatutEcheance statut;

    // Getters & Setters

    public Long getIdEcheance() {
        return idEcheance;
    }

    public void setIdEcheance(Long idEcheance) {
        this.idEcheance = idEcheance;
    }

    public Inscription getInscription() {
        return inscription;
    }

    public void setInscription(Inscription inscription) {
        this.inscription = inscription;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public LocalDate getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public StatutEcheance getStatut() {
        return statut;
    }

    public void setStatut(StatutEcheance statut) {
        this.statut = statut;
    }
}
