package com.digiSchool.digiSchool.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Requete de changement de mot de passe")
public class ChangePasswordRequest {

    @NotBlank(message = "Le mot de passe actuel est obligatoire")
    @Schema(description = "Mot de passe actuel")
    private String currentPassword;

    @NotBlank(message = "Le nouveau mot de passe est obligatoire")
    @Size(min = 8, message = "Le nouveau mot de passe doit contenir au moins 8 caracteres")
    @Schema(description = "Nouveau mot de passe (min 8 caracteres)")
    private String newPassword;

    @NotBlank(message = "La confirmation du mot de passe est obligatoire")
    @Schema(description = "Confirmation du nouveau mot de passe")
    private String confirmPassword;

    public ChangePasswordRequest() {}

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
