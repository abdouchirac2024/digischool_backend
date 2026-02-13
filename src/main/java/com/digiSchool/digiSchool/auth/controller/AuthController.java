package com.digiSchool.digiSchool.auth.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digiSchool.digiSchool.auth.dto.AuthResponse;
import com.digiSchool.digiSchool.auth.dto.ForgotPasswordRequest;
import com.digiSchool.digiSchool.auth.dto.LoginRequest;
import com.digiSchool.digiSchool.auth.dto.RefreshTokenRequest;
import com.digiSchool.digiSchool.auth.dto.ResetPasswordRequest;
import com.digiSchool.digiSchool.auth.model.User;
import com.digiSchool.digiSchool.auth.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentification")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Connexion", description = "Authentification par email ou telephone avec mot de passe. Retourne un token JWT (access + refresh).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Connexion reussie"),
        @ApiResponse(responseCode = "401", description = "Identifiants invalides"),
        @ApiResponse(responseCode = "423", description = "Compte verrouille"),
        @ApiResponse(responseCode = "429", description = "Trop de tentatives")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {

        String ipAddress = getClientIpAddress(httpRequest);
        AuthResponse response = authService.login(request, ipAddress);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Rafraichir le token", description = "Echange un refresh token valide contre une nouvelle paire access/refresh token.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Token rafraichi"),
        @ApiResponse(responseCode = "401", description = "Refresh token invalide ou expire")
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {

        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Deconnexion", description = "Invalide le refresh token de la session courante.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Deconnexion reussie")
    })
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(
            @RequestBody RefreshTokenRequest request) {

        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok(Map.of("message", "Déconnexion réussie"));
    }

    @Operation(summary = "Deconnexion globale", description = "Invalide tous les refresh tokens de l'utilisateur (toutes les sessions).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Toutes les sessions fermees"),
        @ApiResponse(responseCode = "401", description = "Token invalide")
    })
    @PostMapping("/logout-all")
    public ResponseEntity<Map<String, String>> logoutAll(
            @Parameter(description = "Bearer token JWT", required = true)
            @RequestHeader("Authorization") String authHeader) {

        String token = extractToken(authHeader);
        User user = authService.getCurrentUser(token);
        authService.logoutAll(user.getId());
        return ResponseEntity.ok(Map.of("message", "Déconnexion de toutes les sessions réussie"));
    }

    @Operation(summary = "Mot de passe oublie", description = "Envoie un email de reinitialisation si l'adresse existe.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Email envoye (si le compte existe)")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        authService.forgotPassword(request);
        return ResponseEntity.ok(Map.of(
            "message", "Si cet email existe, un lien de réinitialisation a été envoyé"
        ));
    }

    @Operation(summary = "Reinitialiser le mot de passe", description = "Reinitialise le mot de passe avec le token recu par email.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mot de passe reinitialise"),
        @ApiResponse(responseCode = "400", description = "Token invalide ou expire")
    })
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        authService.resetPassword(request);
        return ResponseEntity.ok(Map.of("message", "Mot de passe réinitialisé avec succès"));
    }

    @Operation(summary = "Profil utilisateur courant", description = "Retourne les informations de l'utilisateur connecte (id, nom, role, ecole, etc.).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profil utilisateur"),
        @ApiResponse(responseCode = "401", description = "Non authentifie")
    })
    @GetMapping("/me")
    public ResponseEntity<AuthResponse.UserInfo> getCurrentUser(
            @Parameter(description = "Bearer token JWT", required = true)
            @RequestHeader("Authorization") String authHeader) {

        String token = extractToken(authHeader);
        User user = authService.getCurrentUser(token);

        AuthResponse.UserInfo userInfo = authService.buildUserInfo(user);

        return ResponseEntity.ok(userInfo);
    }

    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new IllegalArgumentException("Token invalide");
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
