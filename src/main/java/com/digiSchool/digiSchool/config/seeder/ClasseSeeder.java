package com.digiSchool.digiSchool.config.seeder;

import org.springframework.stereotype.Component;

import com.digiSchool.digiSchool.academic.organisation.model.Anneescolaire;
import com.digiSchool.digiSchool.academic.organisation.model.Classe;
import com.digiSchool.digiSchool.academic.organisation.model.Ecole;
import com.digiSchool.digiSchool.academic.organisation.model.Niveau;
import com.digiSchool.digiSchool.academic.organisation.model.SousSysteme;
import com.digiSchool.digiSchool.academic.organisation.model.StatutClasse;
import com.digiSchool.digiSchool.academic.organisation.repository.ClasseRepository;
import com.digiSchool.digiSchool.user.model.Utilisateur;

/**
 * Seeder pour les classes.
 * Cree les classes pour chaque ecole avec differents niveaux et sous-systemes.
 */
@Component
public class ClasseSeeder {

    private final ClasseRepository classeRepository;
    private final EcoleSeeder ecoleSeeder;
    private final AnneeScolaireSeeder anneeScolaireSeeder;
    private final UtilisateurSeeder utilisateurSeeder;

    private static final String TENANT = "default";

    public ClasseSeeder(ClasseRepository classeRepository,
                        EcoleSeeder ecoleSeeder,
                        AnneeScolaireSeeder anneeScolaireSeeder,
                        UtilisateurSeeder utilisateurSeeder) {
        this.classeRepository = classeRepository;
        this.ecoleSeeder = ecoleSeeder;
        this.anneeScolaireSeeder = anneeScolaireSeeder;
        this.utilisateurSeeder = utilisateurSeeder;
    }

    /**
     * Cree les classes pour toutes les ecoles
     */
    public void seed() {
        if (classeRepository.count() > 0) {
            System.out.println("  -> Classes : deja presentes, skip");
            return;
        }

        Ecole ecoleBilingue = ecoleSeeder.getEcole("ECB-001");
        Ecole ecoleAnglo = ecoleSeeder.getEcole("ECA-001");
        Ecole ecoleFranco = ecoleSeeder.getEcole("ECF-001");

        Anneescolaire annee2024 = anneeScolaireSeeder.getAnnee("2024-2025");
        Anneescolaire annee2025 = anneeScolaireSeeder.getAnnee("2025-2026");

        Utilisateur enseignant1 = utilisateurSeeder.getUtilisateur("enseignant1");
        Utilisateur enseignant2 = utilisateurSeeder.getUtilisateur("enseignant2");
        Utilisateur enseignant3 = utilisateurSeeder.getUtilisateur("enseignant3");
        Utilisateur enseignant4 = utilisateurSeeder.getUtilisateur("enseignant4");

        // ============================================================
        // Ecole Bilingue La Victoire (Francophone + Anglophone)
        // ============================================================
        seedEcoleBilingue(ecoleBilingue, annee2025, enseignant1, enseignant2);

        // ============================================================
        // Progressive Comprehensive College (Anglophone)
        // ============================================================
        seedEcoleAnglo(ecoleAnglo, annee2025, enseignant4);

        // ============================================================
        // Groupe Scolaire Les Champions (Francophone)
        // ============================================================
        seedEcoleFranco(ecoleFranco, annee2025, enseignant3);

        // ============================================================
        // Classe archivee (annee 2024-2025)
        // ============================================================
        Classe classeArchivee = createClasse("CM2-A", Niveau.PRIMAIRE, SousSysteme.FRANCOPHONE, "A", 48, 22000.0,
                "Classe 2024-2025 archivee", ecoleBilingue, annee2024, null);
        classeArchivee.setStatut(StatutClasse.ARCHIVEE);
        classeRepository.save(classeArchivee);

        System.out.println("  -> Classes : 20 classes creees (19 actives + 1 archivee)");
    }

    private void seedEcoleBilingue(Ecole ecole, Anneescolaire annee, Utilisateur enseignant1, Utilisateur enseignant2) {
        // Primaire Francophone
        createClasse("SIL-A", Niveau.PRIMAIRE, SousSysteme.FRANCOPHONE, "A", 45, 25000.0,
                "Section d'Initiation au Langage", ecole, annee, enseignant1);
        createClasse("CP-A", Niveau.PRIMAIRE, SousSysteme.FRANCOPHONE, "A", 45, 25000.0,
                "Cours Preparatoire", ecole, annee, enseignant2);
        createClasse("CE1-A", Niveau.PRIMAIRE, SousSysteme.FRANCOPHONE, "A", 50, 25000.0,
                null, ecole, annee, null);
        createClasse("CE2-A", Niveau.PRIMAIRE, SousSysteme.FRANCOPHONE, "A", 50, 25000.0,
                null, ecole, annee, null);
        createClasse("CM1-A", Niveau.PRIMAIRE, SousSysteme.FRANCOPHONE, "A", 50, 30000.0,
                null, ecole, annee, null);
        createClasse("CM2-A", Niveau.PRIMAIRE, SousSysteme.FRANCOPHONE, "A", 50, 30000.0,
                "Classe d'examen CEP", ecole, annee, null);

        // Primary Anglophone
        createClasse("Class 1-A", Niveau.PRIMARY, SousSysteme.ANGLOPHONE, "A", 40, 30000.0,
                null, ecole, annee, null);
        createClasse("Class 6-A", Niveau.PRIMARY, SousSysteme.ANGLOPHONE, "A", 40, 35000.0,
                "Exam class - FSLC", ecole, annee, null);

        // Maternelle
        createClasse("Petite Section", Niveau.MATERNELLE, SousSysteme.FRANCOPHONE, null, 30, 20000.0,
                "3-4 ans", ecole, annee, null);
        createClasse("Grande Section", Niveau.MATERNELLE, SousSysteme.FRANCOPHONE, null, 30, 20000.0,
                "5-6 ans", ecole, annee, null);
    }

    private void seedEcoleAnglo(Ecole ecole, Anneescolaire annee, Utilisateur enseignant4) {
        // Secondary (College anglophone)
        createClasse("Form 1-A", Niveau.SECONDARY, SousSysteme.ANGLOPHONE, "A", 60, 50000.0,
                null, ecole, annee, enseignant4);
        createClasse("Form 5-A", Niveau.SECONDARY, SousSysteme.ANGLOPHONE, "A", 55, 55000.0,
                "Exam class - GCE O/L", ecole, annee, null);

        // High School (Lycee anglophone)
        createClasse("Lower 6th Science", Niveau.HIGH_SCHOOL, SousSysteme.ANGLOPHONE, null, 45, 65000.0,
                "Sciences", ecole, annee, null);
        createClasse("Upper 6th Arts", Niveau.HIGH_SCHOOL, SousSysteme.ANGLOPHONE, null, 40, 65000.0,
                "GCE A/L Arts", ecole, annee, null);
    }

    private void seedEcoleFranco(Ecole ecole, Anneescolaire annee, Utilisateur enseignant3) {
        // College francophone
        createClasse("6eme-A", Niveau.COLLEGE, SousSysteme.FRANCOPHONE, "A", 60, 45000.0,
                null, ecole, annee, enseignant3);
        createClasse("6eme-B", Niveau.COLLEGE, SousSysteme.FRANCOPHONE, "B", 60, 45000.0,
                null, ecole, annee, null);
        createClasse("3eme-A", Niveau.COLLEGE, SousSysteme.FRANCOPHONE, "A", 55, 50000.0,
                "Classe d'examen BEPC", ecole, annee, null);

        // Lycee francophone
        createClasse("Tle D", Niveau.LYCEE, SousSysteme.FRANCOPHONE, null, 50, 60000.0,
                "Terminale Sciences - Baccalaureat", ecole, annee, null);
        createClasse("Tle A4", Niveau.LYCEE, SousSysteme.FRANCOPHONE, null, 50, 60000.0,
                "Terminale Lettres - Baccalaureat", ecole, annee, null);
    }

    private Classe createClasse(String nomClasse, Niveau niveau, SousSysteme sousSysteme,
                                 String section, Integer capacite, Double fraisScolarite,
                                 String description, Ecole ecole, Anneescolaire annee,
                                 Utilisateur titulaire) {
        Classe c = new Classe();
        c.setNomClasse(nomClasse);
        c.setNiveau(niveau);
        c.setSousSysteme(sousSysteme);
        c.setSection(section);
        c.setCapacite(capacite);
        c.setFraisScolarite(fraisScolarite);
        c.setDescription(description);
        c.setStatut(StatutClasse.ACTIVE);
        c.setEcole(ecole);
        c.setAnneeScolaire(annee);
        c.setTitulaire(titulaire);
        c.setTenant(TENANT);
        return classeRepository.save(c);
    }
}
