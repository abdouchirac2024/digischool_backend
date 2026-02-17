package com.digiSchool.digiSchool.academic.pedagogique.model;

import java.time.LocalDate;

import com.digiSchool.digiSchool.Exceptionconfig.model.TenantEntity;
import com.digiSchool.digiSchool.academic.organisation.model.Anneescolaire;
import com.digiSchool.digiSchool.user.model.Eleve;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Discipline extends TenantEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDiscipline;

    private String type; // Ex: Avertissement, Blâme, Exclusion
    private String motif;
    private LocalDate dateDiscipline;

    @ManyToOne
    @JoinColumn(name = "eleve_id")
    private Eleve eleve;

    @ManyToOne
    @JoinColumn(name = "annee_id")
    private Anneescolaire anneeScolaire;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMotif() {
		return motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public LocalDate getDateDiscipline() {
		return dateDiscipline;
	}

	public void setDateDiscipline(LocalDate dateDiscipline) {
		this.dateDiscipline = dateDiscipline;
	}
}
