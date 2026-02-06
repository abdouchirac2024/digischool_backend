package com.digiSchool.digiSchool.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digiSchool.digiSchool.auth.dto.LoginRequest;
import com.digiSchool.digiSchool.auth.dto.LoginResponse;
import com.digiSchool.digiSchool.auth.dto.UserDto;
import com.digiSchool.digiSchool.auth.exception.AuthenticationException;
import com.digiSchool.digiSchool.auth.service.AuthService;

/**
 * Contrôleur d'authentification.
 * Gère les endpoints de connexion, déconnexion et récupération des infos utilisateur.
 *
 * Endpoints:
 * - POST /api/auth/login - Connexion
 * - POST /api/auth/logout - Déconnexion
 * - POST /api/auth/refresh - Rafraîchir le token
 * - GET /api/auth/me - Obtenir l'utilisateur courant
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Connexion utilisateur.
     * Vérifie email/mot de passe et retourne un token JWT.
     *
     * @param request Email et mot de passe
     * @return Token JWT et informations utilisateur
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity
                .status(e.getStatusCode())
                .body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Récupérer les informations de l'utilisateur connecté.
     *
     * @param authHeader Header Authorization contenant le token JWT
     * @return Informations de l'utilisateur
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity
                .status(401)
                .body(new ErrorResponse("Token manquant"));
        }

        String token = authHeader.substring(7);

        try {
            UserDto user = authService.getUserFromToken(token);
            return ResponseEntity.ok(user);
        } catch (AuthenticationException e) {
            return ResponseEntity
                .status(e.getStatusCode())
                .body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Rafraîchir le token JWT.
     *
     * @param request Corps contenant le refresh token
     * @return Nouveaux tokens
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            LoginResponse response = authService.refreshToken(request.getRefreshToken());
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity
                .status(e.getStatusCode())
                .body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Déconnexion (côté client, on supprime juste le token).
     * Côté serveur, le token reste valide jusqu'à expiration (stateless).
     *
     * @return Message de confirmation
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(new SuccessResponse("Déconnexion réussie"));
    }

    // ==================== Classes internes pour les réponses ====================

    /**
     * Requête de rafraîchissement de token.
     */
    public static class RefreshTokenRequest {
        private String refreshToken;

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }

    /**
     * Réponse d'erreur.
     */
    public static class ErrorResponse {
        private String message;
        private boolean success = false;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public boolean isSuccess() {
            return success;
        }
    }

    /**
     * Réponse de succès.
     */
    public static class SuccessResponse {
        private String message;
        private boolean success = true;

        public SuccessResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public boolean isSuccess() {
            return success;
        }
    }
}