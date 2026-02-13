package com.digiSchool.digiSchool.academic.organisation.dto;

import com.digiSchool.digiSchool.academic.organisation.model.Niveau;
import com.digiSchool.digiSchool.academic.organisation.model.SousSysteme;
import com.digiSchool.digiSchool.academic.organisation.model.StatutClasse;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Classe scolaire")
public class ClasseDto {

    @Schema(description = "ID unique de la classe", example = "1")
    private Long id;

    @Schema(description = "Nom de la classe", example = "CM2-A")
    private String nomClasse;

    @Schema(description = "Niveau scolaire", example = "CM2")
    private Niveau niveau;

    @Schema(description = "Sous-systeme educatif", example = "FRANCOPHONE")
    private SousSysteme sousSysteme;

    @Schema(description = "Section (A, B, C...)", example = "A")
    private String section;

    @Schema(description = "Capacite maximale", example = "45")
    private Integer capacite;

    @Schema(description = "Statut de la classe", example = "ACTIVE")
    private StatutClasse statut;

    @Schema(description = "Nombre d'eleves actuellement inscrits", example = "38")
    private Integer effectifActuel;

    @Schema(description = "Frais de scolarite annuels en FCFA", example = "75000.0")
    private Double fraisScolarite;

    @Schema(description = "Description de la classe")
    private String description;

    // Relations (IDs + noms pour l'affichage)
    @Schema(description = "ID de l'ecole (auto-defini depuis le JWT)")
    private Long ecoleId;

    @Schema(description = "Nom de l'ecole", accessMode = Schema.AccessMode.READ_ONLY)
    private String ecoleNom;

    @Schema(description = "ID de l'annee scolaire", example = "1")
    private Long anneeScolaireId;

    @Schema(description = "Libelle de l'annee scolaire", example = "2025-2026", accessMode = Schema.AccessMode.READ_ONLY)
    private String anneeScolaireLibelle;

    @Schema(description = "ID de l'enseignant titulaire")
    private Long titulaireId;

    @Schema(description = "Nom de l'enseignant titulaire", accessMode = Schema.AccessMode.READ_ONLY)
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
