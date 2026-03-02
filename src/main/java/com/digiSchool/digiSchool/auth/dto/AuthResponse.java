package com.digiSchool.digiSchool.auth.dto;

import java.time.LocalDateTime;

import com.digiSchool.digiSchool.auth.model.RoleType;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Reponse d'authentification contenant les tokens JWT et les informations utilisateur")
public class AuthResponse {

    @Schema(description = "Token d'acces JWT", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String accessToken;

    @Schema(description = "Token de rafraichissement", example = "dGhpcyBpcyBhIHJlZnJlc2g...")
    private String refreshToken;

    @Schema(description = "Type du token", example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(description = "Duree de validite en millisecondes", example = "86400000")
    private Long expiresIn;

    @Schema(description = "Informations de l'utilisateur connecte")
    private UserInfo user;

    public AuthResponse() {}

    public AuthResponse(String accessToken, String refreshToken, Long expiresIn, UserInfo user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    @Schema(description = "Informations detaillees de l'utilisateur")
    public static class UserInfo {
        @Schema(description = "ID unique", example = "1")
        private Long id;

        @Schema(description = "Adresse email", example = "admin@digischool.cm")
        private String email;

        @Schema(description = "Nom de famille", example = "Dupont")
        private String nom;

        @Schema(description = "Prenom", example = "Jean")
        private String prenom;

        @Schema(description = "Numero de telephone", example = "+237677123456")
        private String telephone;

        @Schema(description = "Role de l'utilisateur", example = "ADMIN_ECOLE")
        private RoleType role;

        @Schema(description = "Identifiant du tenant", example = "CM-CENTRE-ECOLE-001")
        private String tenantId;

        @Schema(description = "ID de l'ecole", example = "1")
        private Long ecoleId;

        @Schema(description = "Nom de l'ecole", example = "Ecole Bilingue La Victoire")
        private String ecoleNom;

        @Schema(description = "Code unique de l'ecole", example = "ECB-001")
        private String codeEcole;

        @Schema(description = "Statut de l'ecole", example = "VALIDEE")
        private String statutEcole;

        @Schema(description = "Statut du compte utilisateur", example = "ACTIVE")
        private String status;

        @Schema(description = "Date de derniere connexion")
        private LocalDateTime lastLogin;

        @Schema(description = "Date de creation du compte")
        private LocalDateTime createdAt;

        @Schema(description = "URL de la photo de profil")
        private String avatarUrl;

        public UserInfo() {}

        public UserInfo(Long id, String email, String nom, String prenom, String telephone,
                        RoleType role, String tenantId, Long ecoleId, String ecoleNom, String codeEcole,
                        String statutEcole, String status, LocalDateTime lastLogin, LocalDateTime createdAt,
                        String avatarUrl) {
            this.id = id;
            this.email = email;
            this.nom = nom;
            this.prenom = prenom;
            this.telephone = telephone;
            this.role = role;
            this.tenantId = tenantId;
            this.ecoleId = ecoleId;
            this.ecoleNom = ecoleNom;
            this.codeEcole = codeEcole;
            this.statutEcole = statutEcole;
            this.status = status;
            this.lastLogin = lastLogin;
            this.createdAt = createdAt;
            this.avatarUrl = avatarUrl;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getPrenom() {
            return prenom;
        }

        public void setPrenom(String prenom) {
            this.prenom = prenom;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public RoleType getRole() {
            return role;
        }

        public void setRole(RoleType role) {
            this.role = role;
        }

        public String getTenantId() {
            return tenantId;
        }

        public void setTenantId(String tenantId) {
            this.tenantId = tenantId;
        }

        public Long getEcoleId() {
            return ecoleId;
        }

        public void setEcoleId(Long ecoleId) {
            this.ecoleId = ecoleId;
        }

        public String getEcoleNom() {
            return ecoleNom;
        }

        public void setEcoleNom(String ecoleNom) {
            this.ecoleNom = ecoleNom;
        }

        public String getCodeEcole() {
            return codeEcole;
        }

        public void setCodeEcole(String codeEcole) {
            this.codeEcole = codeEcole;
        }

        public String getStatutEcole() {
            return statutEcole;
        }

        public void setStatutEcole(String statutEcole) {
            this.statutEcole = statutEcole;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDateTime getLastLogin() {
            return lastLogin;
        }

        public void setLastLogin(LocalDateTime lastLogin) {
            this.lastLogin = lastLogin;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }
    }
}
