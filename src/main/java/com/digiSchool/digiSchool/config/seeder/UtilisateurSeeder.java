package com.digiSchool.digiSchool.config.seeder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.digiSchool.digiSchool.academic.organisation.model.Ecole;
import com.digiSchool.digiSchool.user.model.Role;
import com.digiSchool.digiSchool.user.model.StatutUtilisateur;
import com.digiSchool.digiSchool.user.model.Utilisateur;
import com.digiSchool.digiSchool.user.repository.UtilisateurRepository;

/**
 * Seeder pour les utilisateurs.
 * Cree les utilisateurs de test avec differents roles et statuts.
 */
@Component
public class UtilisateurSeeder {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleSeeder roleSeeder;
    private final EcoleSeeder ecoleSeeder;

    // Stockage des utilisateurs pour les autres seeders
    private Map<String, Utilisateur> utilisateursMap = new HashMap<>();

    public UtilisateurSeeder(UtilisateurRepository utilisateurRepository,
                             PasswordEncoder passwordEncoder,
                             RoleSeeder roleSeeder,
                             EcoleSeeder ecoleSeeder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleSeeder = roleSeeder;
        this.ecoleSeeder = ecoleSeeder;
    }

    /**
     * Cree les utilisateurs de test
     */
    public void seed() {
        if (utilisateurRepository.count() > 0) {
            System.out.println("  -> Utilisateurs : deja presents, skip");
            loadExistingUtilisateurs();
            return;
        }

        // Recuperer les roles et ecoles
        Role roleAdmin = roleSeeder.getRole("ADMIN");
        Role roleDirecteur = roleSeeder.getRole("DIRECTEUR");
        Role roleEnseignant = roleSeeder.getRole("ENSEIGNANT");
        Role roleSecretaire = roleSeeder.getRole("SECRETAIRE");
        Role roleParent = roleSeeder.getRole("PARENT");

        Ecole ecoleBilingue = ecoleSeeder.getEcole("ECB-001");
        Ecole ecoleAnglo = ecoleSeeder.getEcole("ECA-001");
        Ecole ecoleFranco = ecoleSeeder.getEcole("ECF-001");

        // ============ SUPER ADMIN (acces global) ============
        createUtilisateur("Admin", "Super", "admin@digischool.cm",
                "+237600000000", "Admin@2025", StatutUtilisateur.ACTIF, roleAdmin, null);

        // ============ Directeurs par ecole ============
        Utilisateur directeur1 = createUtilisateur("Mbarga", "Samuel", "smbarga@lavictoire.cm",
                "+237677123456", "Directeur@2025", StatutUtilisateur.ACTIF, roleDirecteur, ecoleBilingue);
        utilisateursMap.put("directeur1", directeur1);

        createUtilisateur("Tamba", "John", "jtamba@progressive.cm",
                "+237699987654", "Directeur@2025", StatutUtilisateur.ACTIF, roleDirecteur, ecoleAnglo);

        createUtilisateur("Nguemo", "Pierre", "pnguemo@leschampions.cm",
                "+237655456789", "Directeur@2025", StatutUtilisateur.ACTIF, roleDirecteur, ecoleFranco);

        // ============ Enseignants ============
        Utilisateur enseignant1 = createUtilisateur("Kamga", "Jean-Pierre", "jpkamga@lavictoire.cm",
                "+237670111222", "Enseignant@2025", StatutUtilisateur.ACTIF, roleEnseignant, ecoleBilingue);
        utilisateursMap.put("enseignant1", enseignant1);

        Utilisateur enseignant2 = createUtilisateur("Ngo Bassa", "Marie", "mngo@lavictoire.cm",
                "+237680222333", "Enseignant@2025", StatutUtilisateur.ACTIF, roleEnseignant, ecoleBilingue);
        utilisateursMap.put("enseignant2", enseignant2);

        Utilisateur enseignant3 = createUtilisateur("Fotso", "Paul", "pfotso@leschampions.cm",
                "+237690333444", "Enseignant@2025", StatutUtilisateur.ACTIF, roleEnseignant, ecoleFranco);
        utilisateursMap.put("enseignant3", enseignant3);

        Utilisateur enseignant4 = createUtilisateur("Njoya", "Grace", "gnjoya@progressive.cm",
                "+237650444555", "Enseignant@2025", StatutUtilisateur.ACTIF, roleEnseignant, ecoleAnglo);
        utilisateursMap.put("enseignant4", enseignant4);

        // ============ Secretaires ============
        createUtilisateur("Atangana", "Chantal", "catangana@lavictoire.cm",
                "+237660555666", "Secretaire@2025", StatutUtilisateur.ACTIF, roleSecretaire, ecoleBilingue);

        // ============ Parents ============
        createUtilisateur("Nkoulou", "Francois", "fnkoulou@gmail.com",
                "+237691666777", "Parent@2025", StatutUtilisateur.ACTIF, roleParent, ecoleBilingue);

        createUtilisateur("Manga", "Beatrice", "bmanga@yahoo.fr",
                "+237682777888", "Parent@2025", StatutUtilisateur.ACTIF, roleParent, ecoleBilingue);

        createUtilisateur("Ebogo", "Martin", "mebogo@gmail.com",
                "+237673888999", "Parent@2025", StatutUtilisateur.ACTIF, roleParent, ecoleAnglo);

        // ============ Utilisateur en attente (pour test) ============
        createUtilisateur("Test", "EnAttente", "enattente@test.cm",
                "+237699000111", "Test@2025", StatutUtilisateur.EN_ATTENTE, roleParent, ecoleBilingue);

        // ============ Utilisateur inactif (pour test) ============
        createUtilisateur("Test", "Inactif", "inactif@test.cm",
                "+237699000222", "Test@2025", StatutUtilisateur.INACTIF, roleParent, ecoleBilingue);

        System.out.println("  -> Utilisateurs : 14 utilisateurs crees");
        System.out.println("     Comptes de connexion (email OU telephone + mot de passe):");
        System.out.println("     - Admin: admin@digischool.cm | +237600000000 / Admin@2025");
        System.out.println("     - Directeur: smbarga@lavictoire.cm | +237677123456 / Directeur@2025");
        System.out.println("     - Enseignant: jpkamga@lavictoire.cm | +237670111222 / Enseignant@2025");
        System.out.println("     - Secretaire: catangana@lavictoire.cm | +237660555666 / Secretaire@2025");
        System.out.println("     - Parent: fnkoulou@gmail.com | +237691666777 / Parent@2025");
        System.out.println("     - En attente: enattente@test.cm (statut EN_ATTENTE)");
        System.out.println("     - Inactif: inactif@test.cm (statut INACTIF)");
    }

    /**
     * Recupere un utilisateur par sa cle
     */
    public Utilisateur getUtilisateur(String key) {
        return utilisateursMap.get(key);
    }

    private void loadExistingUtilisateurs() {
        utilisateurRepository.findAll().forEach(u -> {
            if (u.getEmail().contains("jpkamga")) utilisateursMap.put("enseignant1", u);
            if (u.getEmail().contains("mngo")) utilisateursMap.put("enseignant2", u);
            if (u.getEmail().contains("pfotso")) utilisateursMap.put("enseignant3", u);
            if (u.getEmail().contains("gnjoya")) utilisateursMap.put("enseignant4", u);
        });
    }

    private Utilisateur createUtilisateur(String nom, String prenom, String email,
                                           String telephone, String motDePasse,
                                           StatutUtilisateur statut, Role role, Ecole ecole) {
        Utilisateur u = new Utilisateur();
        u.setNom(nom);
        u.setPrenom(prenom);
        u.setEmail(email);
        u.setTelephone(telephone);
        u.setMotDePasse(passwordEncoder.encode(motDePasse));
        u.setStatut(statut);
        u.setRole(role);
        u.setEcole(ecole);
        u.setDateCreation(LocalDateTime.now());
        return utilisateurRepository.save(u);
    }
}
