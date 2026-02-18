package com.digiSchool.digiSchool.auth.model;

/**
 * Enumération des rôles utilisateurs pour le système d'authentification.
 * Basé sur US-005 du Product Backlog.
 */
public enum RoleType {
    SUPER_ADMIN,      // Administrateur global du système
    ADMIN_ECOLE,      // Directeur/Administrateur d'une école
    ENSEIGNANT,       // Professeur
    PARENT,           // Parent d'élève
    SECRETAIRE,       // Secrétaire de l'école
    COMPTABLE,        // Comptable de l'école
    ELEVE             // Élève
}
