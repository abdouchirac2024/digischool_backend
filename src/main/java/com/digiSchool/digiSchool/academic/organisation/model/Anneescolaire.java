package com.digiSchool.digiSchool.academic.organisation.model;

import java.time.LocalDate;
import java.util.List;

import com.digiSchool.digiSchool.Exceptionconfig.model.TenantEntity;
import com.digiSchool.digiSchool.academic.evaluation.model.Evaluation;
import com.digiSchool.digiSchool.academic.pedagogique.model.Discipline;

import jakarta.persistence.CascadeType;
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
    
    @OneToMany(mappedBy = "anneeScolaire", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Periode> periodes;

    @OneToMany(mappedBy = "anneeScolaire", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Examen> examens;

    @OneToMany(mappedBy = "anneeScolaire", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vacance> vacances;

    @OneToMany(mappedBy = "anneeScolaire", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JourFerie> joursFeries;

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

	public Long getIdAnnee() {
		return idAnnee;
	}

	public void setIdAnnee(Long idAnnee) {
		this.idAnnee = idAnnee;
	}

	public List<Inscription> getInscriptions() {
		return inscriptions;
	}

	public void setInscriptions(List<Inscription> inscriptions) {
		this.inscriptions = inscriptions;
	}

	public List<Evaluation> getEvaluations() {
		return evaluations;
	}

	public void setEvaluations(List<Evaluation> evaluations) {
		this.evaluations = evaluations;
	}

	public List<EmploiDuTemps> getEmploisDuTemps() {
		return emploisDuTemps;
	}

	public void setEmploisDuTemps(List<EmploiDuTemps> emploisDuTemps) {
		this.emploisDuTemps = emploisDuTemps;
	}

	public List<Discipline> getDisciplines() {
		return disciplines;
	}

	public void setDisciplines(List<Discipline> disciplines) {
		this.disciplines = disciplines;
	}

	public List<Periode> getPeriodes() {
		return periodes;
	}

	public void setPeriodes(List<Periode> periodes) {
		this.periodes = periodes;
	}

	public List<Examen> getExamens() {
		return examens;
	}

	public void setExamens(List<Examen> examens) {
		this.examens = examens;
	}

	public List<Vacance> getVacances() {
		return vacances;
	}

	public void setVacances(List<Vacance> vacances) {
		this.vacances = vacances;
	}

	public List<JourFerie> getJoursFeries() {
		return joursFeries;
	}

	public void setJoursFeries(List<JourFerie> joursFeries) {
		this.joursFeries = joursFeries;
	}
	
}

