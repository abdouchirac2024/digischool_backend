package com.digiSchool.digiSchool.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Requete de connexion par email ou telephone")
public class LoginRequest {

    @NotBlank(message = "L'identifiant est obligatoire")
    @Schema(description = "Email ou telephone de l'utilisateur", example = "admin@digischool.cm")
    private String login;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Schema(description = "Mot de passe", example = "Admin@2025")
    private String password;

    public LoginRequest() {}

    public LoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
