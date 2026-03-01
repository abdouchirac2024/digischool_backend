package com.digiSchool.digiSchool.auth.model;

/**
 * Enumération des statuts possibles pour un utilisateur.
 */
public enum UserStatus {
    ACTIVE,           // Compte actif
    INACTIVE,         // Compte inactif
    LOCKED,           // Compte verrouillé (après tentatives échouées)
    PENDING           // En attente de validation
}
