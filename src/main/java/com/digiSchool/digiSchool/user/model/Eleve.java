package com.digiSchool.digiSchool.user.model;

import java.time.LocalDate;
import java.util.List;

import com.digiSchool.digiSchool.Exceptionconfig.model.Quartier;
import com.digiSchool.digiSchool.academic.evaluation.model.Note;
import com.digiSchool.digiSchool.academic.organisation.model.Inscription;
import com.digiSchool.digiSchool.academic.pedagogique.model.Discipline;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "eleve",uniqueConstraints = {@UniqueConstraint(columnNames = "matricule")})
public class Eleve {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEleve;
    
    @Column(nullable = false, unique = true)
    private String matricule;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    
    @Enumerated(EnumType.STRING)
    private StatutEleve statut;

    @OneToMany(mappedBy = "eleve")
    private List<Inscription> inscriptions;

    @OneToMany(mappedBy = "eleve")
    private List<Note> notes;

    @OneToMany(mappedBy = "eleve")
    private List<Discipline> disciplines;
    
    @ManyToOne
    private Quartier quartier;

	public String getMatricule() {
		return matricule;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public LocalDate getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(LocalDate dateNaissance) {
		this.dateNaissance = dateNaissance;
	}
	
}


