package com.digiSchool.digiSchool.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Requete de mise a jour du profil utilisateur")
public class UpdateProfileRequest {

    @NotBlank(message = "Le nom est obligatoire")
    @Schema(description = "Nom de famille", example = "Dupont")
    private String nom;

    @NotBlank(message = "Le prenom est obligatoire")
    @Schema(description = "Prenom", example = "Jean")
    private String prenom;

    @Schema(description = "Numero de telephone", example = "+237677123456")
    private String telephone;

    public UpdateProfileRequest() {}

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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
