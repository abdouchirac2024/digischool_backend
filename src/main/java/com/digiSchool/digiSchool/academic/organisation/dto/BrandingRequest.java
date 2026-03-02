package com.digiSchool.digiSchool.academic.organisation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Requete de personnalisation du branding")
public class BrandingRequest {

    @Schema(description = "URL du logo")
    private String logoUrl;

    @Schema(description = "Couleur primaire", example = "#2302B3")
    private String couleurPrimaire;

    @Schema(description = "Couleur secondaire", example = "#4318FF")
    private String couleurSecondaire;

    // ===== Getters & Setters =====

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
}
