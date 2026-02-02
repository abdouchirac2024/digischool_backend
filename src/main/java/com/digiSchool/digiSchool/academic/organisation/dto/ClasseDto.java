package com.digiSchool.digiSchool.academic.organisation.dto;

import com.digiSchool.digiSchool.academic.organisation.model.Niveau;
import com.digiSchool.digiSchool.academic.organisation.model.SousSysteme;
import com.digiSchool.digiSchool.academic.organisation.model.StatutClasse;

public class ClasseDto {

    private Long id;
    private String nomClasse;
    private Niveau niveau;
    private SousSysteme sousSysteme;
    private String section;
    private Integer capacite;
    private StatutClasse statut;
    private Integer effectifActuel;
    private Double fraisScolarite;
    private String description;

    // Relations (IDs + noms pour l'affichage)
    private Long ecoleId;
    private String ecoleNom;
    private Long anneeScolaireId;
    private String anneeScolaireLibelle;
    private Long titulaireId;
    private String titulaireNom;

    // ===== Getters & Setters =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getEffectifActuel() {
        return effectifActuel;
    }

    public void setEffectifActuel(Integer effectifActuel) {
        this.effectifActuel = effectifActuel;
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

    public Long getEcoleId() {
        return ecoleId;
    }

    public void setEcoleId(Long ecoleId) {
        this.ecoleId = ecoleId;
    }

    public String getEcoleNom() {
        return ecoleNom;
    }

    public void setEcoleNom(String ecoleNom) {
        this.ecoleNom = ecoleNom;
    }

    public Long getAnneeScolaireId() {
        return anneeScolaireId;
    }

    public void setAnneeScolaireId(Long anneeScolaireId) {
        this.anneeScolaireId = anneeScolaireId;
    }

    public String getAnneeScolaireLibelle() {
        return anneeScolaireLibelle;
    }

    public void setAnneeScolaireLibelle(String anneeScolaireLibelle) {
        this.anneeScolaireLibelle = anneeScolaireLibelle;
    }

    public Long getTitulaireId() {
        return titulaireId;
    }

    public void setTitulaireId(Long titulaireId) {
        this.titulaireId = titulaireId;
    }

    public String getTitulaireNom() {
        return titulaireNom;
    }

    public void setTitulaireNom(String titulaireNom) {
        this.titulaireNom = titulaireNom;
    }
}
