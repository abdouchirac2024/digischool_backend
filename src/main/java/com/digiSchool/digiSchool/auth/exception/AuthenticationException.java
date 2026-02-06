package com.digiSchool.digiSchool.auth.exception;

/**
 * Exception personnalisée pour les erreurs d'authentification.
 * Permet de distinguer les différents types d'erreurs d'authentification
 * et de retourner des codes HTTP appropriés.
 */
public class AuthenticationException extends RuntimeException {

    private final int statusCode;

    /**
     * Crée une exception d'authentification avec statut 401 (Unauthorized).
     *
     * @param message Message d'erreur
     */
    public AuthenticationException(String message) {
        super(message);
        this.statusCode = 401;
    }

    /**
     * Crée une exception d'authentification avec un statut HTTP personnalisé.
     *
     * @param message    Message d'erreur
     * @param statusCode Code HTTP (401, 403, 404, etc.)
     */
    public AuthenticationException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * Crée une exception d'authentification avec cause.
     *
     * @param message Message d'erreur
     * @param cause   Exception originale
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 401;
    }

    /**
     * Retourne le code HTTP associé à cette exception.
     */
    public int getStatusCode() {
        return statusCode;
    }
}