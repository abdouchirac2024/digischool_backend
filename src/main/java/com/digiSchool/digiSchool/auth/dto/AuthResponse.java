package com.digiSchool.digiSchool.auth.dto;

import com.digiSchool.digiSchool.auth.model.RoleType;

public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
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

    public static class UserInfo {
        private Long id;
        private String email;
        private String nom;
        private String prenom;
        private String telephone;
        private RoleType role;
        private String tenantId;
        private Long ecoleId;
        private String ecoleNom;
        private String codeEcole;

        public UserInfo() {}

        public UserInfo(Long id, String email, String nom, String prenom, String telephone,
                        RoleType role, String tenantId, Long ecoleId, String ecoleNom, String codeEcole) {
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
    }
}
