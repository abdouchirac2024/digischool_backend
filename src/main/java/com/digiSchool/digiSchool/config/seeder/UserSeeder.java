package com.digiSchool.digiSchool.config.seeder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.digiSchool.digiSchool.academic.organisation.model.Ecole;
import com.digiSchool.digiSchool.auth.model.RoleType;
import com.digiSchool.digiSchool.auth.model.User;
import com.digiSchool.digiSchool.auth.model.UserStatus;
import com.digiSchool.digiSchool.auth.repository.UserRepository;

/**
 * Seeder pour les utilisateurs du systeme d'authentification.
 * Cree un compte administrateur par defaut pour chaque ecole.
 * 
 * IMPORTANT: Utilise le Tenant ID de l'ecole (idEcole.toString()).
 */
@Component
public class UserSeeder {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EcoleSeeder ecoleSeeder;

    public UserSeeder(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EcoleSeeder ecoleSeeder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.ecoleSeeder = ecoleSeeder;
    }

    /**
     * Cree les comptes administrateurs pour chaque ecole
     */
    public void seed() {
        if (userRepository.count() > 0) {
            System.out.println("  -> Users (Auth): deja presents, skip");
            return;
        }

        // Recuperer les ecoles
        Ecole ecoleBilingue = ecoleSeeder.getEcole("ECB-001");
        Ecole ecoleAnglo = ecoleSeeder.getEcole("ECA-001");
        Ecole ecoleFranco = ecoleSeeder.getEcole("ECF-001");

        // Creer un directeur pour chaque ecole
        createUser(
                "Samuel",
                "Mbarga",
                "directeur@lavictoire.cm",
                "Directeur@2025",
                RoleType.ADMIN_ECOLE,
                ecoleBilingue.getTenant() // Utilise le tenant de l'ecole
        );

        createUser(
                "John",
                "Tamba",
                "directeur@progressive.cm",
                "Directeur@2025",
                RoleType.ADMIN_ECOLE,
                ecoleAnglo.getTenant());

        createUser(
                "Pierre",
                "Nguemo",
                "directeur@leschampions.cm",
                "Directeur@2025",
                RoleType.ADMIN_ECOLE,
                ecoleFranco.getTenant());

        // Creer un super admin (acces multi-tenant)
        createUser(
                "Super",
                "Admin",
                "admin@digischool.cm",
                "Admin@2025",
                RoleType.SUPER_ADMIN,
                ecoleBilingue.getTenant() // Admin global - utilise le tenant de la premiere ecole
        );

        System.out.println("  -> Users (Auth): 4 comptes crees");
        System.out.println("     Comptes de connexion:");
        System.out
                .println("     - Admin: admin@digischool.cm / Admin@2025 (Tenant: " + ecoleBilingue.getTenant() + ")");
        System.out.println("     - Directeur Ecole 1: directeur@lavictoire.cm / Directeur@2025 (Tenant: "
                + ecoleBilingue.getTenant() + ")");
        System.out.println("     - Directeur Ecole 2: directeur@progressive.cm / Directeur@2025 (Tenant: "
                + ecoleAnglo.getTenant() + ")");
        System.out.println("     - Directeur Ecole 3: directeur@leschampions.cm / Directeur@2025 (Tenant: "
                + ecoleFranco.getTenant() + ")");
    }

    private User createUser(String prenom, String nom, String email,
            String password, RoleType role, String tenantId) {
        User user = new User();
        user.setPrenom(prenom);
        user.setNom(nom);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(role);
        user.setStatus(UserStatus.ACTIVE);
        user.setTenantId(tenantId);

        return userRepository.save(user);
    }
}
