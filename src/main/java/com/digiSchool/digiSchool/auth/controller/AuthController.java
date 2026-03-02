package com.digiSchool.digiSchool.auth.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digiSchool.digiSchool.auth.dto.AuthResponse;
import com.digiSchool.digiSchool.auth.dto.ForgotPasswordRequest;
import com.digiSchool.digiSchool.auth.dto.LoginRequest;
import com.digiSchool.digiSchool.auth.dto.RefreshTokenRequest;
import com.digiSchool.digiSchool.auth.dto.ResetPasswordRequest;
import com.digiSchool.digiSchool.auth.model.User;
import com.digiSchool.digiSchool.auth.service.AuthService;
import com.digiSchool.digiSchool.auth.service.JwtTokenService;
import com.digiSchool.digiSchool.auth.util.CookieUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentification")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenService jwtTokenService;

    @Value("${app.cookie.secure:false}")
    private boolean cookieSecure;

    @Value("${jwt.refresh-expiration:604800000}")
    private long refreshTokenExpiration;

    @Value("${app.cookie.ws-token-expiration:300000}")
    private long wsTokenExpiration;

    public AuthController(AuthService authService, JwtTokenService jwtTokenService) {
        this.authService = authService;
        this.jwtTokenService = jwtTokenService;
    }

    @Operation(summary = "Connexion", description = "Authentification par email ou telephone avec mot de passe. Retourne un token JWT (access + refresh) et pose des cookies HttpOnly.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Connexion reussie"),
        @ApiResponse(responseCode = "401", description = "Identifiants invalides"),
        @ApiResponse(responseCode = "423", description = "Compte verrouille"),
        @ApiResponse(responseCode = "429", description = "Trop de tentatives")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse response) {

        String ipAddress = getClientIpAddress(httpRequest);
        AuthResponse authResponse = authService.login(request, ipAddress);

        // Poser les cookies HttpOnly (inaccessibles au JavaScript)
        int accessMaxAge = (int) (authResponse.getExpiresIn() / 1000);
        int refreshMaxAge = (int) (refreshTokenExpiration / 1000);
        CookieUtils.addAccessTokenCookie(response, authResponse.getAccessToken(), accessMaxAge, cookieSecure);
        CookieUtils.addRefreshTokenCookie(response, authResponse.getRefreshToken(), refreshMaxAge, cookieSecure);

        return ResponseEntity.ok(authResponse);
    }

    @Operation(summary = "Rafraichir le token", description = "Utilise le cookie refresh_token (ou le body en fallback Swagger) pour emettre une nouvelle paire de tokens.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Token rafraichi"),
        @ApiResponse(responseCode = "401", description = "Refresh token invalide ou expire")
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(
            HttpServletRequest httpRequest,
            HttpServletResponse response,
            @RequestBody(required = false) RefreshTokenRequest requestBody) {

        // Priorité au cookie HttpOnly, fallback sur le body (compatibilité Swagger)
        String refreshTokenValue = CookieUtils.extractTokenFromCookie(httpRequest, CookieUtils.REFRESH_TOKEN_COOKIE);
        if (refreshTokenValue == null && requestBody != null) {
            refreshTokenValue = requestBody.getRefreshToken();
        }
        if (refreshTokenValue == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        AuthResponse authResponse = authService.refreshToken(new RefreshTokenRequest(refreshTokenValue));

        int accessMaxAge = (int) (authResponse.getExpiresIn() / 1000);
        int refreshMaxAge = (int) (refreshTokenExpiration / 1000);
        CookieUtils.addAccessTokenCookie(response, authResponse.getAccessToken(), accessMaxAge, cookieSecure);
        CookieUtils.addRefreshTokenCookie(response, authResponse.getRefreshToken(), refreshMaxAge, cookieSecure);

        return ResponseEntity.ok(authResponse);
    }

    @Operation(summary = "Deconnexion", description = "Invalide le refresh token de la session courante et efface les cookies.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Deconnexion reussie")
    })
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(
            HttpServletRequest httpRequest,
            HttpServletResponse response,
            @RequestBody(required = false) RefreshTokenRequest requestBody) {

        // Priorité au cookie, fallback sur le body
        String refreshTokenValue = CookieUtils.extractTokenFromCookie(httpRequest, CookieUtils.REFRESH_TOKEN_COOKIE);
        if (refreshTokenValue == null && requestBody != null) {
            refreshTokenValue = requestBody.getRefreshToken();
        }

        if (refreshTokenValue != null) {
            authService.logout(refreshTokenValue);
        }

        CookieUtils.clearTokenCookies(response, cookieSecure);
        return ResponseEntity.ok(Map.of("message", "Déconnexion réussie"));
    }

    @Operation(summary = "Deconnexion globale", description = "Invalide tous les refresh tokens de l'utilisateur (toutes les sessions) et efface les cookies.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Toutes les sessions fermees"),
        @ApiResponse(responseCode = "401", description = "Non authentifie")
    })
    @PostMapping("/logout-all")
    public ResponseEntity<Map<String, String>> logoutAll(HttpServletResponse response) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = authService.getUserByEmail(email);
        authService.logoutAll(user.getId());
        CookieUtils.clearTokenCookies(response, cookieSecure);
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

    @Operation(summary = "Profil utilisateur courant", description = "Retourne les informations de l'utilisateur connecte via cookie ou Bearer token.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profil utilisateur"),
        @ApiResponse(responseCode = "401", description = "Non authentifie")
    })
    @GetMapping("/me")
    public ResponseEntity<AuthResponse.UserInfo> getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = authService.getUserByEmail(email);
        AuthResponse.UserInfo userInfo = authService.buildUserInfo(user);
        return ResponseEntity.ok(userInfo);
    }

    @Operation(summary = "Token WebSocket court", description = "Retourne un token d'acces court (5 min) pour authentifier la connexion STOMP/WebSocket. Non stocke en cookie.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Token WebSocket genere"),
        @ApiResponse(responseCode = "401", description = "Non authentifie")
    })
    @GetMapping("/ws-token")
    public ResponseEntity<Map<String, Object>> getWsToken() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = authService.getUserByEmail(email);
        String wsToken = jwtTokenService.generateShortLivedToken(user, wsTokenExpiration);
        return ResponseEntity.ok(Map.of("wsToken", wsToken, "expiresIn", wsTokenExpiration));
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
