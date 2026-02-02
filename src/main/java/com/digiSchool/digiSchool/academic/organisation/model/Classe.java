package com.digiSchool.digiSchool.academic.organisation.model;

import java.util.List;

import com.digiSchool.digiSchool.Exceptionconfig.model.TenantEntity;
import com.digiSchool.digiSchool.academic.evaluation.model.Evaluation;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Classe extends TenantEntity{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClasse;

    private String nomClasse;
    private String niveau;

    @ManyToOne
    @JoinColumn(name = "ecole_id")
    private Ecole ecole;

    @OneToMany(mappedBy = "classe")
    private List<Inscription> inscriptions;

    @OneToMany(mappedBy = "classe")
    private List<Evaluation> evaluations;

    @OneToMany(mappedBy = "classe")
    private List<EmploiDuTemps> emploisDuTemps;
    

	public Long getIdClasse() {
		return idClasse;
	}

	public void setIdClasse(Long idClasse) {
		this.idClasse = idClasse;
	}

	public String getNomClasse() {
		return nomClasse;
	}

	public void setNomClasse(String nomClasse) {
		this.nomClasse = nomClasse;
	}

	public String getNiveau() {
		return niveau;
	}

	public void setNiveau(String niveau) {
		this.niveau = niveau;
	}

	public Ecole getEcole() {
		return ecole;
	}

	public void setEcole(Ecole ecole) {
		this.ecole = ecole;
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
    
    
}

