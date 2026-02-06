package com.digiSchool.digiSchool.auth.dto;

/**
 * DTO pour la requête de connexion.
 * Accepte email OU téléphone comme identifiant.
 */
public class LoginRequest {

    /**
     * Identifiant de connexion : peut être un email ou un numéro de téléphone
     */
    private String identifier;

    private String password;

    public LoginRequest() {}

    public LoginRequest(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Alias pour getIdentifier() - compatibilité
     */
    public String getEmail() {
        return identifier;
    }

    /**
     * Alias pour setIdentifier() - compatibilité
     */
    public void setEmail(String email) {
        this.identifier = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
