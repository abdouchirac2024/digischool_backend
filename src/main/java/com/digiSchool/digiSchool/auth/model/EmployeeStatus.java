package com.digiSchool.digiSchool.auth.model;

public enum EmployeeStatus {
	 ACTIF,                 // En activité normale
	    EN_CONGE,              // Congé annuel / maladie / maternité
	    EN_PERMISSION,         // Permission courte durée
	    SANCTIONNE,            // Sous sanction disciplinaire
	    SUSPENDU,              // Suspension temporaire
	    DESACTIVE,             // Compte désactivé administrativement
	    RETRAITE,              // Retraité
	    DEMISSIONNAIRE,        // A démissionné
	    LICENCIE,              // Licencié
	    EN_ESSAI,              // En période d’essai
	    ABSENT_NON_JUSTIFIE    // Absence injustifiée
}
