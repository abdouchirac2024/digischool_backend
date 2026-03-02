package com.digiSchool.digiSchool.academic.organisation.model;

import java.time.LocalDateTime;
import java.util.List;

import com.digiSchool.digiSchool.Exceptionconfig.model.Quartier;
import com.digiSchool.digiSchool.Exceptionconfig.model.TenantEntity;
import com.digiSchool.digiSchool.auth.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

	@Deprecated
	private Boolean statut;

	@Enumerated(EnumType.STRING)
	@Column(name = "statut_ecole", nullable = false)
	private StatutEcole statutEcole = StatutEcole.EN_ATTENTE;

	@Column(name = "slug", unique = true)
	private String slug;

	@Column(name = "motif_rejet", columnDefinition = "TEXT")
	private String motifRejet;

	@Column(name = "date_validation")
	private LocalDateTime dateValidation;

	@Column(name = "nombre_eleves")
	private Integer nombreEleves;

	@Column(name = "logo_url")
	private String logoUrl;

	@Column(name = "couleur_primaire")
	private String couleurPrimaire;

	@Column(name = "couleur_secondaire")
	private String couleurSecondaire;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@OneToMany(mappedBy = "ecole")
	private List<User> users;

	@OneToMany(mappedBy = "ecole")
	private List<Classe> classes;

	@Enumerated(EnumType.STRING)
	@Column(name = "type_secteur")
	private TypeSecteur typeSecteur;

	@Enumerated(EnumType.STRING)
	@Column(name = "type_etablissement")
	private TypeEtablissement typeEtablissement;

	@Enumerated(EnumType.STRING)
	@Column(name = "sous_systeme")
	private SousSysteme sousSysteme;

	@Column(name = "boite_postale")
	private String boitePostale;

	@Column(name = "site_web")
	private String siteWeb;

	@Column(name = "devise")
	private String devise;

	@Column(name = "annee_fondation")
	private Integer anneeFondation;

	@Column(name = "numero_autorisation")
	private String numeroAutorisation;

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

	@Deprecated
	public Boolean getStatut() {
		return statut;
	}

	@Deprecated
	public void setStatut(Boolean statut) {
		this.statut = statut;
	}

	public StatutEcole getStatutEcole() {
		return statutEcole;
	}

	public void setStatutEcole(StatutEcole statutEcole) {
		this.statutEcole = statutEcole;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getMotifRejet() {
		return motifRejet;
	}

	public void setMotifRejet(String motifRejet) {
		this.motifRejet = motifRejet;
	}

	public LocalDateTime getDateValidation() {
		return dateValidation;
	}

	public void setDateValidation(LocalDateTime dateValidation) {
		this.dateValidation = dateValidation;
	}

	public Integer getNombreEleves() {
		return nombreEleves;
	}

	public void setNombreEleves(Integer nombreEleves) {
		this.nombreEleves = nombreEleves;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getCouleurPrimaire() {
		return couleurPrimaire;
	}

	public void setCouleurPrimaire(String couleurPrimaire) {
		this.couleurPrimaire = couleurPrimaire;
	}

	public String getCouleurSecondaire() {
		return couleurSecondaire;
	}

	public void setCouleurSecondaire(String couleurSecondaire) {
		this.couleurSecondaire = couleurSecondaire;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Classe> getClasses() {
		return classes;
	}

	public void setClasses(List<Classe> classes) {
		this.classes = classes;
	}

	public TypeSecteur getTypeSecteur() {
		return typeSecteur;
	}

	public void setTypeSecteur(TypeSecteur typeSecteur) {
		this.typeSecteur = typeSecteur;
	}

	public TypeEtablissement getTypeEtablissement() {
		return typeEtablissement;
	}

	public void setTypeEtablissement(TypeEtablissement typeEtablissement) {
		this.typeEtablissement = typeEtablissement;
	}

	public SousSysteme getSousSysteme() {
		return sousSysteme;
	}

	public void setSousSysteme(SousSysteme sousSysteme) {
		this.sousSysteme = sousSysteme;
	}

	public String getBoitePostale() {
		return boitePostale;
	}

	public void setBoitePostale(String boitePostale) {
		this.boitePostale = boitePostale;
	}

	public String getSiteWeb() {
		return siteWeb;
	}

	public void setSiteWeb(String siteWeb) {
		this.siteWeb = siteWeb;
	}

	public String getDevise() {
		return devise;
	}

	public void setDevise(String devise) {
		this.devise = devise;
	}

	public Integer getAnneeFondation() {
		return anneeFondation;
	}

	public void setAnneeFondation(Integer anneeFondation) {
		this.anneeFondation = anneeFondation;
	}

	public String getNumeroAutorisation() {
		return numeroAutorisation;
	}

	public void setNumeroAutorisation(String numeroAutorisation) {
		this.numeroAutorisation = numeroAutorisation;
	}

	public Quartier getQuartier() {
		return quartier;
	}

	public void setQuartier(Quartier quartier) {
		this.quartier = quartier;
	}
}
