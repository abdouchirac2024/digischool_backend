package com.digiSchool.digiSchool.academic.organisation.dto;

import java.time.LocalDate;
import java.util.List;

public class AnneeScolaireResponseDTO {

    private Long id;
    private String libelle;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Boolean statut;

    private List<ExamenDTO> exams;
    private List<VacanceDTO> holidays;
    private List<JourFerieDTO> publicHolidays;
    private List<PeriodeDTO> periods;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public List<ExamenDTO> getExams() {
		return exams;
	}
	public void setExams(List<ExamenDTO> exams) {
		this.exams = exams;
	}
	public List<VacanceDTO> getHolidays() {
		return holidays;
	}
	public void setHolidays(List<VacanceDTO> holidays) {
		this.holidays = holidays;
	}
	public List<JourFerieDTO> getPublicHolidays() {
		return publicHolidays;
	}
	public void setPublicHolidays(List<JourFerieDTO> publicHolidays) {
		this.publicHolidays = publicHolidays;
	}
	public List<PeriodeDTO> getPeriods() {
		return periods;
	}
	public void setPeriods(List<PeriodeDTO> periods) {
		this.periods = periods;
	}

    // getters & setters
    
}

