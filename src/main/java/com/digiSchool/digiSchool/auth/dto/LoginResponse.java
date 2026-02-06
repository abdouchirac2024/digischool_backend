package com.digiSchool.digiSchool.auth.dto;

/**
 * DTO pour la réponse de connexion.
 * Contient le token JWT, les informations utilisateur et les métadonnées.
 */
public class LoginResponse {

    private String token;
    private String refreshToken;
    private UserDto user;
    private String message;
    private boolean success = true;
    private long expiresIn;

    public LoginResponse() {}

    // Token d'accès JWT
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // Refresh token pour renouveler l'accès
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // Informations utilisateur
    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    // Message de retour
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Indicateur de succès
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    // Durée de validité du token en millisecondes
    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
}