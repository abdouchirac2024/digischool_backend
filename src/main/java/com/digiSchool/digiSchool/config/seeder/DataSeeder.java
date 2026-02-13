package com.digiSchool.digiSchool.config.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Orchestrateur principal des seeders.
 *
 * Appelle les seeders dans l'ordre correct pour respecter les dependances:
 * 1. RoleSeeder - Cree les roles (ADMIN, DIRECTEUR, etc.)
 * 2. RegionSeeder - Cree la geographie (regions, departements, villes,
 * quartiers)
 * 3. EcoleSeeder - Cree les ecoles (depend des quartiers)
 * 4. AnneeScolaireSeeder - Cree les annees scolaires
 * 5. UtilisateurSeeder - Cree les utilisateurs (depend des roles et ecoles)
 * 6. ClasseSeeder - Cree les classes (depend des ecoles, annees et
 * utilisateurs)
 *
 * Chaque seeder verifie si les donnees existent deja avant de les creer.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleSeeder roleSeeder;
    private final RegionSeeder regionSeeder;
    private final EcoleSeeder ecoleSeeder;
    private final UserSeeder userSeeder;
    private final AnneeScolaireSeeder anneeScolaireSeeder;
    private final UtilisateurSeeder utilisateurSeeder;
    private final ClasseSeeder classeSeeder;

    public DataSeeder(RoleSeeder roleSeeder,
            RegionSeeder regionSeeder,
            EcoleSeeder ecoleSeeder,
            UserSeeder userSeeder,
            AnneeScolaireSeeder anneeScolaireSeeder,
            UtilisateurSeeder utilisateurSeeder,
            ClasseSeeder classeSeeder) {
        this.roleSeeder = roleSeeder;
        this.regionSeeder = regionSeeder;
        this.ecoleSeeder = ecoleSeeder;
        this.userSeeder = userSeeder;
        this.anneeScolaireSeeder = anneeScolaireSeeder;
        this.utilisateurSeeder = utilisateurSeeder;
        this.classeSeeder = classeSeeder;
    }

    @Override
    public void run(String... args) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║              DIGISCHOOL - INITIALISATION DES DONNEES         ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();

        // 1. Roles (pas de dependance)
        roleSeeder.seed();

        // 2. Geographie (pas de dependance)
        regionSeeder.seed();

        // 3. Ecoles (depend des quartiers)
        ecoleSeeder.seed();

        // 4. Users Auth (depend des ecoles pour le tenant ID)
        userSeeder.seed();

        // 5. Annees scolaires (pas de dependance)
        anneeScolaireSeeder.seed();

        // 6. Utilisateurs (depend des roles et ecoles)
        utilisateurSeeder.seed();

        // 7. Classes (depend des ecoles, annees et utilisateurs)
        classeSeeder.seed();

        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    INITIALISATION TERMINEE                   ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
}
