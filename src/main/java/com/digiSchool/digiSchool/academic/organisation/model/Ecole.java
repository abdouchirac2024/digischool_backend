package com.digiSchool.digiSchool.academic.organisation.model;

import java.util.List;

import com.digiSchool.digiSchool.Exceptionconfig.model.Quartier;
import com.digiSchool.digiSchool.Exceptionconfig.model.TenantEntity;
import com.digiSchool.digiSchool.user.model.Utilisateur;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "ecole", uniqueConstraints = {
		@UniqueConstraint(columnNames = "code_ecole")
}, indexes = {
		@Index(name = "idx_ecole_code", columnList = "code_ecole")
})
public class Ecole extends TenantEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEcole;
	@Column(name = "code_ecole", nullable = false, unique = true, updatable = false)
	private String codeEcole;

	private String nom;
	private String adresse;
	private String telephone;
	private String email;
	private Boolean statut;

	@OneToMany(mappedBy = "ecole")
	private List<Utilisateur> utilisateurs;

	@OneToMany(mappedBy = "ecole")
	private List<Classe> classes;

	@ManyToOne
	private Quartier quartier;

	public Long getIdEcole() {
		return idEcole;
	}

	public void setIdEcole(Long idEcole) {
		this.idEcole = idEcole;
	}

	public String getCodeEcole() {
		return codeEcole;
	}

	public void setCodeEcole(String codeEcole) {
		this.codeEcole = codeEcole;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getStatut() {
		return statut;
	}

	public void setStatut(Boolean statut) {
		this.statut = statut;
	}

	public List<Utilisateur> getUtilisateurs() {
		return utilisateurs;
	}

	public void setUtilisateurs(List<Utilisateur> utilisateurs) {
		this.utilisateurs = utilisateurs;
	}

	public List<Classe> getClasses() {
		return classes;
	}

	public void setClasses(List<Classe> classes) {
		this.classes = classes;
	}

	public Quartier getQuartier() {
		return quartier;
	}

	public void setQuartier(Quartier quartier) {
		this.quartier = quartier;
	}
}
