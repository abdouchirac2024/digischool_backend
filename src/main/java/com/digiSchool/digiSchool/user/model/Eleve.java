package com.digiSchool.digiSchool.user.model;

import java.time.LocalDate;
import java.util.List;
import org.hibernate.annotations.Filter;

import com.digiSchool.digiSchool.Exceptionconfig.model.Quartier;
import com.digiSchool.digiSchool.Exceptionconfig.model.TenantEntity;
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
@Filter(name = "tenantFilter", condition = "tenant = :tenant")
@Table(name = "eleve", uniqueConstraints = { @UniqueConstraint(columnNames = "matricule") })
public class Eleve extends TenantEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEleve;

	@Column(nullable = false, unique = true)
	private String matricule;
	private String nom;
	private String prenom;
	private LocalDate dateNaissance;
	private String lieuNaissance;
	private String nationalite;

	@Enumerated(EnumType.STRING)
	private Sexe sexe;

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

	@OneToMany(mappedBy = "eleve")
	private List<EleveParent> eleveParents;

	public Long getIdEleve() {
		return idEleve;
	}

	public void setIdEleve(Long idEleve) {
		this.idEleve = idEleve;
	}

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

	public String getLieuNaissance() {
		return lieuNaissance;
	}

	public void setLieuNaissance(String lieuNaissance) {
		this.lieuNaissance = lieuNaissance;
	}

	public String getNationalite() {
		return nationalite;
	}

	public void setNationalite(String nationalite) {
		this.nationalite = nationalite;
	}

	public Sexe getSexe() {
		return sexe;
	}

	public void setSexe(Sexe sexe) {
		this.sexe = sexe;
	}

	public StatutEleve getStatut() {
		return statut;
	}

	public void setStatut(StatutEleve statut) {
		this.statut = statut;
	}

	public List<Inscription> getInscriptions() {
		return inscriptions;
	}

	public void setInscriptions(List<Inscription> inscriptions) {
		this.inscriptions = inscriptions;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	public List<Discipline> getDisciplines() {
		return disciplines;
	}

	public void setDisciplines(List<Discipline> disciplines) {
		this.disciplines = disciplines;
	}

	public Quartier getQuartier() {
		return quartier;
	}

	public void setQuartier(Quartier quartier) {
		this.quartier = quartier;
	}

	public List<EleveParent> getEleveParents() {
		return eleveParents;
	}

	public void setEleveParents(List<EleveParent> eleveParents) {
		this.eleveParents = eleveParents;
	}

}
