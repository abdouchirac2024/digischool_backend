package com.digiSchool.digiSchool.auth.dto;

import java.time.LocalDateTime;

/**
 * DTO pour les informations utilisateur (sans mot de passe).
 */
public class UserDto {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String statut; // EN_ATTENTE, ACTIF, INACTIF
    private LocalDateTime derniereConnexion;
    private Long roleId;
    private String roleName;
    private Long ecoleId;
    private String ecoleNom;

    public UserDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public LocalDateTime getDerniereConnexion() {
        return derniereConnexion;
    }

    public void setDerniereConnexion(LocalDateTime derniereConnexion) {
        this.derniereConnexion = derniereConnexion;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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
}
