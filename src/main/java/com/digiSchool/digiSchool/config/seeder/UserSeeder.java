package com.digiSchool.digiSchool.config.seeder;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.digiSchool.digiSchool.academic.organisation.model.Ecole;
import com.digiSchool.digiSchool.auth.model.RoleType;
import com.digiSchool.digiSchool.auth.model.User;
import com.digiSchool.digiSchool.auth.model.UserStatus;
import com.digiSchool.digiSchool.auth.repository.UserRepository;

/**
 * Seeder pour les utilisateurs du systeme.
 * Cree tous les comptes (admin, directeurs, enseignants, secretaires, parents, tests).
 */
@Component
public class UserSeeder {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EcoleSeeder ecoleSeeder;

    private Map<String, User> usersMap = new HashMap<>();

    public UserSeeder(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EcoleSeeder ecoleSeeder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.ecoleSeeder = ecoleSeeder;
    }

    /**
     * Cree tous les comptes utilisateurs
     */
    public void seed() {
        if (userRepository.count() > 0) {
            System.out.println("  -> Users : deja presents, skip");
            loadExistingUsers();
            return;
        }

        // Recuperer les ecoles
        Ecole ecoleBilingue = ecoleSeeder.getEcole("ECB-001");
        Ecole ecoleAnglo = ecoleSeeder.getEcole("ECA-001");
        Ecole ecoleFranco = ecoleSeeder.getEcole("ECF-001");

        // ============ SUPER ADMIN (acces global) ============
        createUser("Super", "Admin", "admin@digischool.cm", "+237600000000",
                "Admin@2025", RoleType.SUPER_ADMIN, UserStatus.ACTIVE,
                ecoleBilingue);

        // ============ Directeurs par ecole ============
        createUser("Samuel", "Mbarga", "smbarga@lavictoire.cm", "+237677123456",
                "Directeur@2025", RoleType.ADMIN_ECOLE, UserStatus.ACTIVE,
                ecoleBilingue);

        createUser("John", "Tamba", "jtamba@progressive.cm", "+237699987654",
                "Directeur@2025", RoleType.ADMIN_ECOLE, UserStatus.ACTIVE,
                ecoleAnglo);

        createUser("Pierre", "Nguemo", "pnguemo@leschampions.cm", "+237655456789",
                "Directeur@2025", RoleType.ADMIN_ECOLE, UserStatus.ACTIVE,
                ecoleFranco);

        // ============ Enseignants ============
        User enseignant1 = createUser("Jean-Pierre", "Kamga", "jpkamga@lavictoire.cm", "+237670111222",
                "Enseignant@2025", RoleType.ENSEIGNANT, UserStatus.ACTIVE,
                ecoleBilingue);
        usersMap.put("enseignant1", enseignant1);

        User enseignant2 = createUser("Marie", "Ngo Bassa", "mngo@lavictoire.cm", "+237680222333",
                "Enseignant@2025", RoleType.ENSEIGNANT, UserStatus.ACTIVE,
                ecoleBilingue);
        usersMap.put("enseignant2", enseignant2);

        User enseignant3 = createUser("Paul", "Fotso", "pfotso@leschampions.cm", "+237690333444",
                "Enseignant@2025", RoleType.ENSEIGNANT, UserStatus.ACTIVE,
                ecoleFranco);
        usersMap.put("enseignant3", enseignant3);

        User enseignant4 = createUser("Grace", "Njoya", "gnjoya@progressive.cm", "+237650444555",
                "Enseignant@2025", RoleType.ENSEIGNANT, UserStatus.ACTIVE,
                ecoleAnglo);
        usersMap.put("enseignant4", enseignant4);

        // ============ Secretaires ============
        createUser("Chantal", "Atangana", "catangana@lavictoire.cm", "+237660555666",
                "Secretaire@2025", RoleType.SECRETAIRE, UserStatus.ACTIVE,
                ecoleBilingue);

        // ============ Parents ============
        createUser("Francois", "Nkoulou", "fnkoulou@gmail.com", "+237691666777",
                "Parent@2025", RoleType.PARENT, UserStatus.ACTIVE,
                ecoleBilingue);

        createUser("Beatrice", "Manga", "bmanga@yahoo.fr", "+237682777888",
                "Parent@2025", RoleType.PARENT, UserStatus.ACTIVE,
                ecoleBilingue);

        createUser("Martin", "Ebogo", "mebogo@gmail.com", "+237673888999",
                "Parent@2025", RoleType.PARENT, UserStatus.ACTIVE,
                ecoleAnglo);

        // ============ Utilisateur en attente (pour test) ============
        createUser("EnAttente", "Test", "enattente@test.cm", "+237699000111",
                "Test@2025", RoleType.PARENT, UserStatus.PENDING,
                ecoleBilingue);

        // ============ Utilisateur inactif (pour test) ============
        createUser("Inactif", "Test", "inactif@test.cm", "+237699000222",
                "Test@2025", RoleType.PARENT, UserStatus.INACTIVE,
                ecoleBilingue);

        System.out.println("  -> Users : 14 utilisateurs crees");
        System.out.println("     Comptes de connexion:");
        System.out.println("     - Admin: admin@digischool.cm / Admin@2025");
        System.out.println("     - Directeur: smbarga@lavictoire.cm / Directeur@2025");
        System.out.println("     - Enseignant: jpkamga@lavictoire.cm / Enseignant@2025");
        System.out.println("     - Secretaire: catangana@lavictoire.cm / Secretaire@2025");
        System.out.println("     - Parent: fnkoulou@gmail.com / Parent@2025");
        System.out.println("     - En attente: enattente@test.cm (statut PENDING)");
        System.out.println("     - Inactif: inactif@test.cm (statut INACTIVE)");
    }

    /**
     * Recupere un utilisateur par sa cle
     */
    public User getUser(String key) {
        return usersMap.get(key);
    }

    private void loadExistingUsers() {
        userRepository.findAll().forEach(u -> {
            if (u.getEmail().contains("jpkamga")) usersMap.put("enseignant1", u);
            if (u.getEmail().contains("mngo")) usersMap.put("enseignant2", u);
            if (u.getEmail().contains("pfotso")) usersMap.put("enseignant3", u);
            if (u.getEmail().contains("gnjoya")) usersMap.put("enseignant4", u);
        });
    }

    private User createUser(String prenom, String nom, String email, String telephone,
            String password, RoleType role, UserStatus status, Ecole ecole) {
        User user = new User();
        user.setPrenom(prenom);
        user.setNom(nom);
        user.setEmail(email);
        user.setTelephone(telephone);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(role);
        user.setStatus(status);
        user.setEcole(ecole);
        user.setTenantId(ecole.getTenant());

        return userRepository.save(user);
    }
}
