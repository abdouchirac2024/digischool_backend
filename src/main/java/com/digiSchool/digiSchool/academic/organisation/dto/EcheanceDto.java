package com.digiSchool.digiSchool.academic.organisation.dto;

import java.time.LocalDate;

public class EcheanceDto {
    private Long idEcheance;
    private Integer numero;
    private String libelle;
    private Double montant;
    private LocalDate dateEcheance;
    private LocalDate datePaiement;
    private String statut;

    public Long getIdEcheance() {
        return idEcheance;
    }

    public void setIdEcheance(Long idEcheance) {
        this.idEcheance = idEcheance;
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

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
