package com.digiSchool.digiSchool.academic.organisation.model;

import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class EmploiDuTemps {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmploi;

    private String jour;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private String matiere;

    @ManyToOne
    @JoinColumn(name = "classe_id")
    private Classe classe;

    @ManyToOne
    @JoinColumn(name = "annee_id")
    private Anneescolaire anneeScolaire;

	public Long getIdEmploi() {
		return idEmploi;
	}

	public void setIdEmploi(Long idEmploi) {
		this.idEmploi = idEmploi;
	}

	public String getJour() {
		return jour;
	}

	public void setJour(String jour) {
		this.jour = jour;
	}

	public LocalTime getHeureDebut() {
		return heureDebut;
	}

	public void setHeureDebut(LocalTime heureDebut) {
		this.heureDebut = heureDebut;
	}

	public LocalTime getHeureFin() {
		return heureFin;
	}

	public void setHeureFin(LocalTime heureFin) {
		this.heureFin = heureFin;
	}

	public String getMatiere() {
		return matiere;
	}

	public void setMatiere(String matiere) {
		this.matiere = matiere;
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
    
    
}
