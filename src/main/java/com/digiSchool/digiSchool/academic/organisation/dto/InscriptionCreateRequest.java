package com.digiSchool.digiSchool.academic.organisation.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotNull;

public class InscriptionCreateRequest {

    @NotNull(message = "L'identifiant de l'eleve est obligatoire")
    private Long eleveId;

    @NotNull(message = "L'identifiant de la classe est obligatoire")
    private Long classeId;

    private Long anneeScolaireId;

    private LocalDate dateInscription;

    /** Numeros des tranches a marquer comme payees (ex: [1], [1,2], [1,2,3]) */
    private List<Integer> tranchesPayees;

    // Nouveaux champs financiers
    private Double remise;
    private Double fraisTransport;
    private Double fraisCantine;
    private Double fraisAssurance;

    public Long getEleveId() {
        return eleveId;
    }

    public void setEleveId(Long eleveId) {
        this.eleveId = eleveId;
    }

    public Long getClasseId() {
        return classeId;
    }

    public void setClasseId(Long classeId) {
        this.classeId = classeId;
    }

    public Long getAnneeScolaireId() {
        return anneeScolaireId;
    }

    public void setAnneeScolaireId(Long anneeScolaireId) {
        this.anneeScolaireId = anneeScolaireId;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    public List<Integer> getTranchesPayees() {
        return tranchesPayees;
    }

    public void setTranchesPayees(List<Integer> tranchesPayees) {
        this.tranchesPayees = tranchesPayees;
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
