package com.digiSchool.digiSchool.academic.organisation.model;

import java.time.LocalDate;

import com.digiSchool.digiSchool.Exceptionconfig.model.TenantEntity;
import com.digiSchool.digiSchool.user.model.Eleve;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Inscription extends TenantEntity{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInscription;

    private LocalDate dateInscription;
    private Boolean statut;

    @ManyToOne
    @JoinColumn(name = "eleve_id")
    private Eleve eleve;

    @ManyToOne
    @JoinColumn(name = "classe_id")
    private Classe classe;

    @ManyToOne
    @JoinColumn(name = "annee_id")
    private Anneescolaire anneeScolaire;

	public LocalDate getDateInscription() {
		return dateInscription;
	}

	public void setDateInscription(LocalDate dateInscription) {
		this.dateInscription = dateInscription;
	}

	public Boolean getStatut() {
		return statut;
	}

	public void setStatut(Boolean statut) {
		this.statut = statut;
	}

	public Classe getClasse() {
		return classe;
	}

	public void setClasse(Classe classe) {
		this.classe = classe;
	}

	public Eleve getEleve() {
		return eleve;
	}

	public void setEleve(Eleve eleve) {
		this.eleve = eleve;
	}
}

