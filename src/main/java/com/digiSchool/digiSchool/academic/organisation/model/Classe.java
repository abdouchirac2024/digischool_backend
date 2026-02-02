package com.digiSchool.digiSchool.academic.organisation.model;

import java.util.List;

import com.digiSchool.digiSchool.Exceptionconfig.model.TenantEntity;
import com.digiSchool.digiSchool.academic.evaluation.model.Evaluation;
import com.digiSchool.digiSchool.user.model.Utilisateur;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Classe extends TenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClasse;

    @Column(nullable = false)
    private String nomClasse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Niveau niveau;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SousSysteme sousSysteme;

    private String section;

    private Integer capacite;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutClasse statut = StatutClasse.ACTIVE;

    private Double fraisScolarite;

    private String description;

    @ManyToOne
    @JoinColumn(name = "ecole_id")
    private Ecole ecole;

    @ManyToOne
    @JoinColumn(name = "annee_scolaire_id")
    private Anneescolaire anneeScolaire;

    @ManyToOne
    @JoinColumn(name = "titulaire_id")
    private Utilisateur titulaire;

    @OneToMany(mappedBy = "classe")
    private List<Inscription> inscriptions;

    @OneToMany(mappedBy = "classe")
    private List<Evaluation> evaluations;

    @OneToMany(mappedBy = "classe")
    private List<EmploiDuTemps> emploisDuTemps;


    // ===== Getters & Setters =====

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

    public Niveau getNiveau() {
        return niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public SousSysteme getSousSysteme() {
        return sousSysteme;
    }

    public void setSousSysteme(SousSysteme sousSysteme) {
        this.sousSysteme = sousSysteme;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Integer getCapacite() {
        return capacite;
    }

    public void setCapacite(Integer capacite) {
        this.capacite = capacite;
    }

    public StatutClasse getStatut() {
        return statut;
    }

    public void setStatut(StatutClasse statut) {
        this.statut = statut;
    }

    public Double getFraisScolarite() {
        return fraisScolarite;
    }

    public void setFraisScolarite(Double fraisScolarite) {
        this.fraisScolarite = fraisScolarite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Ecole getEcole() {
        return ecole;
    }

    public void setEcole(Ecole ecole) {
        this.ecole = ecole;
    }

    public Anneescolaire getAnneeScolaire() {
        return anneeScolaire;
    }

    public void setAnneeScolaire(Anneescolaire anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
    }

    public Utilisateur getTitulaire() {
        return titulaire;
    }

    public void setTitulaire(Utilisateur titulaire) {
        this.titulaire = titulaire;
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
