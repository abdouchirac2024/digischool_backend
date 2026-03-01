package com.digiSchool.digiSchool.academic.organisation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Requete de validation/rejet d'une ecole")
public class ValidationEcoleRequest {

    @Schema(description = "true = approuver, false = rejeter")
    private boolean approuve;

    @Schema(description = "Motif de rejet (requis si approuve = false)")
    private String motifRejet;

    // ===== Getters & Setters =====

    public boolean isApprouve() {
        return approuve;
    }

    public void setApprouve(boolean approuve) {
        this.approuve = approuve;
    }

    public String getMotifRejet() {
        return motifRejet;
    }

    public void setMotifRejet(String motifRejet) {
        this.motifRejet = motifRejet;
    }
}
