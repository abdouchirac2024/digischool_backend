package com.digiSchool.digiSchool.academic.organisation.model;

import java.time.LocalDate;
import java.util.List;

import com.digiSchool.digiSchool.Exceptionconfig.model.TenantEntity;
import com.digiSchool.digiSchool.academic.evaluation.model.Evaluation;
import com.digiSchool.digiSchool.academic.pedagogique.model.Discipline;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;


@Entity
public class Anneescolaire extends TenantEntity{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAnnee;

    private String libelle;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Boolean statut;

    @OneToMany(mappedBy = "anneeScolaire")
    private List<Inscription> inscriptions;

    @OneToMany(mappedBy = "anneeScolaire")
    private List<Evaluation> evaluations;

    @OneToMany(mappedBy = "anneeScolaire")
    private List<EmploiDuTemps> emploisDuTemps;

    @OneToMany(mappedBy = "anneeScolaire")
    private List<Discipline> disciplines;

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public LocalDate getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(LocalDate dateDebut) {
		this.dateDebut = dateDebut;
	}

	public LocalDate getDateFin() {
		return dateFin;
	}

	public void setDateFin(LocalDate dateFin) {
		this.dateFin = dateFin;
	}

	public Boolean getStatut() {
		return statut;
	}

	public void setStatut(Boolean statut) {
		this.statut = statut;
	}
}

