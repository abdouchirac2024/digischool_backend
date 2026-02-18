package com.digiSchool.digiSchool.academic.organisation.dto;

import jakarta.validation.constraints.NotBlank;

public class AnnulationRequest {

    @NotBlank(message = "Le motif d'annulation est obligatoire")
    private String motif;

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }
}
