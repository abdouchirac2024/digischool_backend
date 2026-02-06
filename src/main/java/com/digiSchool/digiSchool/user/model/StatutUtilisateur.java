package com.digiSchool.digiSchool.user.model;

/**
 * Statut d'un utilisateur dans le système.
 */
public enum StatutUtilisateur {
    /**
     * Compte en attente de validation (inscription récente)
     */
    EN_ATTENTE,

    /**
     * Compte actif et fonctionnel
     */
    ACTIF,

    /**
     * Compte désactivé (temporairement ou définitivement)
     */
    INACTIF
}
