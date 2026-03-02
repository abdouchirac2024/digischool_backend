package com.digiSchool.digiSchool.auth.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.digiSchool.digiSchool.academic.organisation.model.Anneescolaire;
import com.digiSchool.digiSchool.academic.organisation.model.Classe;
import com.digiSchool.digiSchool.academic.organisation.model.Ecole;
import com.digiSchool.digiSchool.academic.organisation.model.Niveau;
import com.digiSchool.digiSchool.academic.organisation.model.StatutClasse;
import com.digiSchool.digiSchool.academic.organisation.model.StatutEcole;
import com.digiSchool.digiSchool.academic.organisation.repository.AnneeScolaireRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.ClasseRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.EcoleRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.QuartierRepository;
import com.digiSchool.digiSchool.auth.model.RoleType;
import com.digiSchool.digiSchool.auth.model.User;
import com.digiSchool.digiSchool.auth.model.UserStatus;
import com.digiSchool.digiSchool.auth.repository.UserRepository;
import com.digiSchool.digiSchool.user.model.Eleve;
import com.digiSchool.digiSchool.user.model.EleveParent;
import com.digiSchool.digiSchool.user.model.Parent;
import com.digiSchool.digiSchool.user.model.TypeRelation;
import com.digiSchool.digiSchool.user.repository.EleveParentRepository;
import com.digiSchool.digiSchool.user.repository.EleveRepository;
import com.digiSchool.digiSchool.user.repository.ParentRepository;

/**
 * Initialisation des donnees de test (Order 2 = apres DataSeeder).
 *
 * Tenant = ecole.getTenant() partout → format CM-{REGION}-ECOLE-{ID}
 * Aucun tenant hardcode : on passe l'objet Ecole directement.
 */
@Component
@Order(2)
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final EcoleRepository ecoleRepository;
    private final ParentRepository parentRepository;
    private final EleveRepository eleveRepository;
    private final EleveParentRepository eleveParentRepository;
    private final QuartierRepository quartierRepository;
    private final ClasseRepository classeRepository;
    private final AnneeScolaireRepository anneescolaireRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
            EcoleRepository ecoleRepository,
            ParentRepository parentRepository,
            EleveRepository eleveRepository,
            EleveParentRepository eleveParentRepository,
            QuartierRepository quartierRepository,
            ClasseRepository classeRepository,
            AnneeScolaireRepository anneescolaireRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.ecoleRepository = ecoleRepository;
        this.parentRepository = parentRepository;
        this.eleveRepository = eleveRepository;
        this.eleveParentRepository = eleveParentRepository;
        this.quartierRepository = quartierRepository;
        this.classeRepository = classeRepository;
        this.anneescolaireRepository = anneescolaireRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n=================================================");
        System.out.println("   INITIALISATION DES DONNÉES DE TEST");
        System.out.println("=================================================\n");

        // --- 1. ÉCOLES (tenant genere au format CM-{REGION}-ECOLE-{CODE}) ---

        createEcoleIfNotExists("DIGI-001", "École DigiSchool Bastos I",
                "Avenue Kennedy, Bastos", "+237 699 123 456",
                "contact@digischool-bastos.cm", "QBS");

        createEcoleIfNotExists("DIGI-002", "Lycée Bilingue de Yaoundé",
                "Quartier Essos, Yaoundé", "+237 222 111 222",
                "contact@lby-yde.cm", "QES");

        createEcoleIfNotExists("DIGI-003", "Collège Saint-Jean",
                "Quartier Tamdja, Bafoussam", "+237 333 444 555",
                "info@saintjean-bfs.cm", "QTD");

        // Recuperer les ecoles avec leur tenant CM-{REGION}-ECOLE-{ID}
        Ecole ecole1 = ecoleRepository.findByCodeEcole("DIGI-001").orElse(null);
        Ecole ecole2 = ecoleRepository.findByCodeEcole("DIGI-002").orElse(null);
        Ecole ecole3 = ecoleRepository.findByCodeEcole("DIGI-003").orElse(null);

        if (ecole1 == null || ecole2 == null || ecole3 == null) {
            System.out.println("⚠ Ecoles DIGI introuvables, arret de l'initialisation");
            return;
        }

        System.out.println("  Tenants: " + ecole1.getTenant() + ", "
                + ecole2.getTenant() + ", " + ecole3.getTenant());

        // --- 2. ANNÉE SCOLAIRE ACTIVE ---

        // Récupérer l'année scolaire active déjà créée par AnneeScolaireSeeder
        // (On ne crée pas de nouvelle année ici pour éviter les doublons)
        Anneescolaire anneeScolaire = anneescolaireRepository.findFirstByStatutTrue()
                .orElse(null);

        if (anneeScolaire == null) {
            System.out.println("⚠ Aucune année scolaire active trouvée, arret du DataInitializer");
            return;
        }

        // --- 3. CLASSES (tenant = ecole.getTenant()) ---

        createClasseIfNotExists("Cours Préparatoire", Niveau.PRIMAIRE, ecole1, anneeScolaire, 40, 75000.0);
        createClasseIfNotExists("Cours Élémentaire 1", Niveau.PRIMAIRE, ecole1, anneeScolaire, 40, 75000.0);
        createClasseIfNotExists("Cours Élémentaire 2", Niveau.PRIMAIRE, ecole1, anneeScolaire, 35, 80000.0);
        createClasseIfNotExists("Cours Moyen 1", Niveau.PRIMAIRE, ecole1, anneeScolaire, 35, 85000.0);
        createClasseIfNotExists("Cours Moyen 2", Niveau.PRIMAIRE, ecole1, anneeScolaire, 35, 85000.0);

        createClasseIfNotExists("6ème Bilingue", Niveau.COLLEGE, ecole2, anneeScolaire, 50, 120000.0);
        createClasseIfNotExists("5ème Bilingue", Niveau.COLLEGE, ecole2, anneeScolaire, 50, 120000.0);

        createClasseIfNotExists("Terminale C", Niveau.LYCEE, ecole3, anneeScolaire, 45, 150000.0);

        // --- 3b. Mise a jour des classes existantes sans frais/capacite ---
        updateClassesWithMissingData(anneeScolaire);

        // --- 4. UTILISATEURS (tenant = ecole.getTenant()) ---

        createUserIfNotExists("directeur@digi-001.com", "Admin123!", "Mbarga",
                "Jean-Pierre", RoleType.ADMIN_ECOLE, ecole1);
        createUserIfNotExists("secretaire@digi-001.com", "Admin123!", "Fotso",
                "Marie", RoleType.SECRETAIRE, ecole1);
        createUserIfNotExists("enseignant@digi-001.com", "Admin123!", "Nguemo",
                "Paul", RoleType.ENSEIGNANT, ecole1);

        createUserIfNotExists("directeur@digi-002.com", "Admin123!", "Nkoulou",
                "François", RoleType.ADMIN_ECOLE, ecole2);
        createUserIfNotExists("secretaire@digi-002.com", "Admin123!", "Ebanga",
                "Carine", RoleType.SECRETAIRE, ecole2);

        createUserIfNotExists("directeur@digi-003.com", "Admin123!", "Tchinda",
                "Albert", RoleType.ADMIN_ECOLE, ecole3);

        // Super Admin (pas d'ecole, tenant global)
        createSuperAdminIfNotExists("admin@digischool.com", "SuperAdmin123!",
                "System", "Admin");

        // --- 5. PARENTS (tenant = ecole.getTenant()) ---

        createParentIfNotExists("P001-001", "Mbianda", "Guy",
                "guy.mbianda@email.com", "677112233", "Bastos, YDE",
                "QBS", "Ingénieur", ecole1);
        createParentIfNotExists("P001-002", "Ngassa", "Alice",
                "alice.ngassa@email.com", "699554433", "Nlongkak, YDE",
                "QNL", "Commerçante", ecole1);

        createParentIfNotExists("P002-001", "Kamga", "Jean",
                "jean.kamga@email.com", "688001122", "Essos, YDE",
                "QES", "Avocat", ecole2);

        createParentIfNotExists("P003-001", "Wabo", "Claude",
                "claude.wabo@email.com", "655778899", "Tamdja, BFS",
                "QTD", "Médecin", ecole3);

        // --- 6. ÉLÈVES (tenant = ecole.getTenant()) ---

        createEleveIfNotExists("ELV-2024-006", "Biya", "Samuel",
                "2012-08-20", "QES", ecole2);
        createEleveIfNotExists("ELV-2024-007", "Etoa", "Stéphanie",
                "2013-02-15", "QES", ecole2);

        createEleveIfNotExists("ELV-2024-008", "Tchouta", "Rodrigue",
                "2006-11-10", "QTD", ecole3);
        createEleveIfNotExists("ELV-2024-009", "Djeukam", "Patricia",
                "2007-04-05", "QTD", ecole3);

        // --- 7. RELATIONS ÉLÈVE-PARENT (tenant = eleve.getTenant()) ---

        createEleveParentIfNotExists("ELV-2024-001", "P001-001", TypeRelation.PERE, true);
        createEleveParentIfNotExists("ELV-2024-002", "P001-002", TypeRelation.MERE, true);
        createEleveParentIfNotExists("ELV-2024-006", "P002-001", TypeRelation.PERE, true);
        createEleveParentIfNotExists("ELV-2024-008", "P003-001", TypeRelation.PERE, true);

        System.out.println("\n=================================================");
        System.out.println("   INITIALISATION TERMINÉE");
        System.out.println("=================================================\n");
    }

    // ──────────────────────────────────────────────────────────────
    // Ecole : genere le tenant CM-{REGION}-ECOLE-{CODE}
    // ──────────────────────────────────────────────────────────────

    private void createEcoleIfNotExists(String code, String nom, String adresse,
            String tel, String email, String quartierCode) {
        if (!ecoleRepository.existsByCodeEcole(code)) {
            Ecole ecole = new Ecole();
            ecole.setCodeEcole(code);
            ecole.setNom(nom);
            ecole.setAdresse(adresse);
            ecole.setTelephone(tel);
            ecole.setEmail(email);
            ecole.setStatutEcole(StatutEcole.VALIDEE);

            var quartier = quartierRepository.findByCode(quartierCode).orElse(null);
            if (quartier != null) {
                ecole.setQuartier(quartier);
                String regionNom = quartier.getVille().getArrondissement()
                        .getDepartement().getRegion().getNom();
                ecole.setTenant("CM-" + regionNom.toUpperCase().replace(" ", "-")
                        + "-ECOLE-" + code);
            } else {
                // Fallback si quartier absent (ne devrait pas arriver apres RegionSeeder)
                ecole.setTenant("CM-UNKNOWN-ECOLE-" + code);
            }

            ecoleRepository.save(ecole);
            System.out.println("✓ École créée: " + nom + " [" + code
                    + "] tenant=" + ecole.getTenant());
        } else {
            System.out.println("• École existe: " + code);
        }
    }

    // ──────────────────────────────────────────────────────────────
    // Classe : tenant = ecole.getTenant()
    // ──────────────────────────────────────────────────────────────

    private void createClasseIfNotExists(String nom, Niveau niveau, Ecole ecole,
            Anneescolaire anneeScolaire, Integer capacite, Double fraisScolarite) {
        if (!classeRepository.existsByNomClasseAndEcoleIdEcole(nom, ecole.getIdEcole())) {
            Classe classe = new Classe();
            classe.setNomClasse(nom);
            classe.setNiveau(niveau);
            classe.setEcole(ecole);
            classe.setAnneeScolaire(anneeScolaire);
            classe.setCapacite(capacite);
            classe.setFraisScolarite(fraisScolarite);
            classe.setStatut(StatutClasse.ACTIVE);
            classe.setTenant(ecole.getTenant());
            classeRepository.save(classe);
            System.out.println("✓ Classe créée: " + nom + " capacite=" + capacite
                    + " frais=" + fraisScolarite + " tenant=" + ecole.getTenant());
        } else {
            // Mettre a jour les classes existantes si frais/capacite manquants
            classeRepository.findAll().stream()
                    .filter(c -> c.getNomClasse().equals(nom) && c.getEcole().getIdEcole().equals(ecole.getIdEcole()))
                    .findFirst()
                    .ifPresent(c -> {
                        boolean updated = false;
                        if (c.getFraisScolarite() == null) {
                            c.setFraisScolarite(fraisScolarite);
                            updated = true;
                        }
                        if (c.getCapacite() == null) {
                            c.setCapacite(capacite);
                            updated = true;
                        }
                        if (c.getStatut() == null) {
                            c.setStatut(StatutClasse.ACTIVE);
                            updated = true;
                        }
                        if (c.getAnneeScolaire() == null) {
                            c.setAnneeScolaire(anneeScolaire);
                            updated = true;
                        }
                        if (updated) {
                            classeRepository.save(c);
                            System.out.println("↻ Classe mise à jour: " + nom + " frais=" + fraisScolarite
                                    + " capacite=" + capacite);
                        } else {
                            System.out.println("• Classe existe: " + nom);
                        }
                    });
        }
    }

    // ──────────────────────────────────────────────────────────────
    // User : tenant = ecole.getTenant()
    // ──────────────────────────────────────────────────────────────

    private void createUserIfNotExists(String email, String password, String nom,
            String prenom, RoleType role, Ecole ecole) {
        if (!userRepository.existsByEmail(email)) {
            User user = new User();
            user.setEmail(email);
            user.setPasswordHash(passwordEncoder.encode(password));
            user.setNom(nom);
            user.setPrenom(prenom);
            user.setRole(role);
            user.setStatus(UserStatus.ACTIVE);
            user.setTenantId(ecole.getTenant());
            user.setEcole(ecole);

            userRepository.save(user);
            System.out.println("✓ Utilisateur créé: " + email
                    + " tenant=" + ecole.getTenant());
        } else {
            System.out.println("• Utilisateur existe: " + email);
        }
    }

    private void createSuperAdminIfNotExists(String email, String password,
            String nom, String prenom) {
        if (!userRepository.existsByEmail(email)) {
            User user = new User();
            user.setEmail(email);
            user.setPasswordHash(passwordEncoder.encode(password));
            user.setNom(nom);
            user.setPrenom(prenom);
            user.setRole(RoleType.SUPER_ADMIN);
            user.setStatus(UserStatus.ACTIVE);
            user.setTenantId("GLOBAL");

            userRepository.save(user);
            System.out.println("✓ Super Admin créé: " + email + " tenant=GLOBAL");
        } else {
            System.out.println("• Utilisateur existe: " + email);
        }
    }

    // ──────────────────────────────────────────────────────────────
    // Parent : tenant = ecole.getTenant()
    // ──────────────────────────────────────────────────────────────

    private void createParentIfNotExists(String matricule, String nom, String prenom,
            String email, String tel, String adresse, String quartierCode,
            String profession, Ecole ecole) {
        if (!parentRepository.existsByMatriculeParent(matricule)) {
            var quartier = quartierRepository.findByCode(quartierCode).orElse(null);
            if (quartier == null) {
                System.out.println("⚠ Parent ignoré (quartier " + quartierCode
                        + " introuvable): " + matricule);
                return;
            }

            Parent parent = new Parent();
            parent.setMatriculeParent(matricule);
            parent.setNom(nom);
            parent.setPrenom(prenom);
            parent.setEmail(email);
            parent.setTelephone(tel);
            parent.setAdresse(adresse);
            parent.setProfession(profession);
            parent.setActif(true);
            // parent.setQuartier(quartier);
            parent.setTenant(ecole.getTenant());

            parentRepository.save(parent);
            System.out.println("✓ Parent créé: " + nom + " " + prenom
                    + " [" + matricule + "] tenant=" + ecole.getTenant());
        } else {
            System.out.println("• Parent existe: " + matricule);
        }
    }

    // ──────────────────────────────────────────────────────────────
    // Eleve : tenant = ecole.getTenant()
    // ──────────────────────────────────────────────────────────────

    private void createEleveIfNotExists(String matricule, String nom, String prenom,
            String dateNaissance, String quartierCode, Ecole ecole) {
        if (!eleveRepository.existsByMatricule(matricule)) {
            Eleve eleve = new Eleve();
            eleve.setMatricule(matricule);
            eleve.setNom(nom);
            eleve.setPrenom(prenom);
            eleve.setDateNaissance(java.time.LocalDate.parse(dateNaissance));
            eleve.setTenant(ecole.getTenant());

            quartierRepository.findByCode(quartierCode).ifPresent(eleve::setQuartier);

            eleveRepository.save(eleve);
            System.out.println("✓ Élève créé: " + nom + " " + prenom
                    + " [" + matricule + "] tenant=" + ecole.getTenant());
        } else {
            System.out.println("• Élève existe: " + matricule);
        }
    }

    // ──────────────────────────────────────────────────────────────
    // EleveParent : tenant = eleve.getTenant() (herite de l'ecole)
    // ──────────────────────────────────────────────────────────────

    private void createEleveParentIfNotExists(String eleveMatricule,
            String parentMatricule, TypeRelation typeRelation, boolean estPrincipal) {
        eleveRepository.findByMatricule(eleveMatricule).ifPresent(eleve -> {
            parentRepository.findByMatriculeParent(parentMatricule).ifPresent(parent -> {
                if (!eleveParentRepository.existsByEleveIdEleveAndParentIdParent(
                        eleve.getIdEleve(), parent.getIdParent())) {
                    EleveParent eleveParent = new EleveParent();
                    eleveParent.setEleve(eleve);
                    eleveParent.setParent(parent);
                    eleveParent.setTypeRelation(typeRelation);
                    eleveParent.setEstPrincipal(estPrincipal);
                    eleveParent.setAutorisePriseEnCharge(true);
                    eleveParent.setAutoriseUrgence(true);
                    eleveParent.setTenant(eleve.getTenant());

                    eleveParentRepository.save(eleveParent);
                    System.out.println("✓ Relation créée: " + eleve.getPrenom()
                            + " " + eleve.getNom() + " → " + parent.getPrenom()
                            + " " + parent.getNom() + " (" + typeRelation
                            + ") tenant=" + eleve.getTenant());
                } else {
                    System.out.println("• Relation existe: " + eleveMatricule
                            + " → " + parentMatricule);
                }
            });
        });
    }

    // ──────────────────────────────────────────────────────────────
    // Mise a jour globale des classes sans frais/capacite/statut
    // ──────────────────────────────────────────────────────────────

    private void updateClassesWithMissingData(Anneescolaire anneeScolaire) {
        classeRepository.findAll().stream()
                .filter(c -> c.getFraisScolarite() == null || c.getCapacite() == null || c.getStatut() == null)
                .forEach(c -> {
                    if (c.getFraisScolarite() == null) {
                        // Definir des frais par defaut selon le niveau
                        double frais = switch (c.getNiveau()) {
                            case MATERNELLE -> 50000.0;
                            case PRIMAIRE, PRIMARY -> 75000.0;
                            case COLLEGE, SECONDARY -> 100000.0;
                            case LYCEE, HIGH_SCHOOL -> 120000.0;
                            default -> 75000.0;
                        };
                        c.setFraisScolarite(frais);
                    }
                    if (c.getCapacite() == null)
                        c.setCapacite(40);
                    if (c.getStatut() == null)
                        c.setStatut(StatutClasse.ACTIVE);
                    if (c.getAnneeScolaire() == null)
                        c.setAnneeScolaire(anneeScolaire);
                    classeRepository.save(c);
                    System.out.println("↻ Classe corrigée: " + c.getNomClasse()
                            + " frais=" + c.getFraisScolarite() + " capacite=" + c.getCapacite());
                });
    }
}
