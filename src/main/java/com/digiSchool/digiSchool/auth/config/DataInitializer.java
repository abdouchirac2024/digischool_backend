package com.digiSchool.digiSchool.auth.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.digiSchool.digiSchool.academic.organisation.model.Classe;
import com.digiSchool.digiSchool.academic.organisation.model.Ecole;
import com.digiSchool.digiSchool.academic.organisation.model.Niveau;
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

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final EcoleRepository ecoleRepository;
    private final ParentRepository parentRepository;
    private final EleveRepository eleveRepository;
    private final EleveParentRepository eleveParentRepository;
    private final QuartierRepository quartierRepository;
    private final ClasseRepository classeRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
            EcoleRepository ecoleRepository,
            ParentRepository parentRepository,
            EleveRepository eleveRepository,
            EleveParentRepository eleveParentRepository,
            QuartierRepository quartierRepository,
            ClasseRepository classeRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.ecoleRepository = ecoleRepository;
        this.parentRepository = parentRepository;
        this.eleveRepository = eleveRepository;
        this.eleveParentRepository = eleveParentRepository;
        this.quartierRepository = quartierRepository;
        this.classeRepository = classeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n=================================================");
        System.out.println("   INITIALISATION DES DONNÉES DE TEST");
        System.out.println("=================================================\n");

        // --- INITIALISATION DES ÉCOLES ---

        // École 1 - DIGI-001 (Douala)
        createEcoleIfNotExists(
                "DIGI-001",
                "École DigiSchool Bastos I",
                "Avenue Kennedy, Bastos",
                "+237 699 123 456",
                "contact@digischool-bastos.cm",
                "YDE-BAS",
                "DIGI-001");

        // École 2 - DIGI-002 (Yaoundé)
        createEcoleIfNotExists(
                "DIGI-002",
                "Lycée Bilingue de Yaoundé",
                "Quartier Essos, Yaoundé",
                "+237 222 111 222",
                "contact@lby-yde.cm",
                "YDE-ESS",
                "DIGI-002");

        // École 3 - DIGI-003 (Bafoussam)
        createEcoleIfNotExists(
                "DIGI-003",
                "Collège Saint-Jean",
                "Quartier Tamdja, Bafoussam",
                "+237 333 444 555",
                "info@saintjean-bfs.cm",
                "BFS-TAM",
                "DIGI-003");

        // --- INITIALISATION DES CLASSES ---

        // Classes pour DIGI-001
        createClasseIfNotExists("Cours Préparatoire", Niveau.PRIMAIRE, "DIGI-001", "DIGI-001");
        createClasseIfNotExists("Cours Élémentaire 1", Niveau.PRIMAIRE, "DIGI-001", "DIGI-001");
        createClasseIfNotExists("Cours Élémentaire 2", Niveau.PRIMAIRE, "DIGI-001", "DIGI-001");
        createClasseIfNotExists("Cours Moyen 1", Niveau.PRIMAIRE, "DIGI-001", "DIGI-001");
        createClasseIfNotExists("Cours Moyen 2", Niveau.PRIMAIRE, "DIGI-001", "DIGI-001");

        // Classes pour DIGI-002
        createClasseIfNotExists("6ème Bilingue", Niveau.COLLEGE, "DIGI-002", "DIGI-002");
        createClasseIfNotExists("5ème Bilingue", Niveau.COLLEGE, "DIGI-002", "DIGI-002");

        // Classes pour DIGI-003
        createClasseIfNotExists("Terminale C", Niveau.LYCEE, "DIGI-003", "DIGI-003");

        // --- INITIALISATION DES UTILISATEURS ---

        // École 1 - DIGI-001
        createUserIfNotExists("directeur@digi-001.com", "Admin123!", "Mbarga", "Jean-Pierre", RoleType.ADMIN_ECOLE,
                "DIGI-001");
        createUserIfNotExists("secretaire@digi-001.com", "Admin123!", "Fotso", "Marie", RoleType.SECRETAIRE,
                "DIGI-001");
        createUserIfNotExists("enseignant@digi-001.com", "Admin123!", "Nguemo", "Paul", RoleType.ENSEIGNANT,
                "DIGI-001");

        // École 2 - DIGI-002
        createUserIfNotExists("directeur@digi-002.com", "Admin123!", "Nkoulou", "François", RoleType.ADMIN_ECOLE,
                "DIGI-002");
        createUserIfNotExists("secretaire@digi-002.com", "Admin123!", "Ebanga", "Carine", RoleType.SECRETAIRE,
                "DIGI-002");

        // École 3 - DIGI-003
        createUserIfNotExists("directeur@digi-003.com", "Admin123!", "Tchinda", "Albert", RoleType.ADMIN_ECOLE,
                "DIGI-003");

        // Super Admin
        createUserIfNotExists("admin@digischool.com", "SuperAdmin123!", "System", "Admin", RoleType.SUPER_ADMIN,
                "GLOBAL");

        // --- INITIALISATION DES PARENTS ---

        // Parents pour DIGI-001
        createParentIfNotExists("P001-001", "Mbianda", "Guy", "guy.mbianda@email.com", "677112233", "Bastos, YDE",
                "YDE-BAS", "Ingénieur", "DIGI-001");
        createParentIfNotExists("P001-002", "Ngassa", "Alice", "alice.ngassa@email.com", "699554433", "Nlongkak, YDE",
                "YDE-NLS", "Commerçante", "DIGI-001");

        // Parents pour DIGI-002
        createParentIfNotExists("P002-001", "Kamga", "Jean", "jean.kamga@email.com", "688001122", "Essos, YDE",
                "YDE-ESS", "Avocat", "DIGI-002");

        // Parents pour DIGI-003
        createParentIfNotExists("P003-001", "Wabo", "Claude", "claude.wabo@email.com", "655778899", "Tamdja, BFS",
                "BFS-TAM", "Médecin", "DIGI-003");

        // --- INITIALISATION DES ÉLÈVES ---

        // Élèves pour DIGI-002 (2 élèves)
        createEleveIfNotExists("ELV-2024-006", "Biya", "Samuel", "2012-08-20", "YDE-ESS", "6ème Bilingue",
                "DIGI-002");
        createEleveIfNotExists("ELV-2024-007", "Etoa", "Stéphanie", "2013-02-15", "YDE-ESS", "5ème Bilingue",
                "DIGI-002");

        // Élèves pour DIGI-003 (2 élèves)
        createEleveIfNotExists("ELV-2024-008", "Tchouta", "Rodrigue", "2006-11-10", "BFS-TAM", "Terminale C",
                "DIGI-003");
        createEleveIfNotExists("ELV-2024-009", "Djeukam", "Patricia", "2007-04-05", "BFS-TAM", "Terminale C",
                "DIGI-003");

        // --- INITIALISATION DES RELATIONS ÉLÈVE-PARENT ---

        // Relations pour DIGI-001
        createEleveParentIfNotExists("ELV-2024-001", "P001-001", TypeRelation.PERE, true, "DIGI-001");
        createEleveParentIfNotExists("ELV-2024-002", "P001-002", TypeRelation.MERE, true, "DIGI-001");

        // Relations pour DIGI-002
        createEleveParentIfNotExists("ELV-2024-006", "P002-001", TypeRelation.PERE, true, "DIGI-002");

        // Relations pour DIGI-003
        createEleveParentIfNotExists("ELV-2024-008", "P003-001", TypeRelation.PERE, true, "DIGI-003");

        System.out.println("\n=================================================");
        System.out.println("   INITIALISATION TERMINÉE");
        System.out.println("=================================================\n");
    }

    private void createEcoleIfNotExists(String code, String nom, String adresse, String tel,
            String email, String quartierCode, String tenantId) {
        if (!ecoleRepository.existsByCodeEcole(code)) {
            Ecole ecole = new Ecole();
            ecole.setCodeEcole(code);
            ecole.setNom(nom);
            ecole.setAdresse(adresse);
            ecole.setTelephone(tel);
            ecole.setEmail(email);
            ecole.setStatut(true);
            ecole.setTenant(tenantId);

            quartierRepository.findByCode(quartierCode).ifPresent(ecole::setQuartier);

            ecoleRepository.save(ecole);
            System.out.println("✓ École créée: " + nom + " [" + code + "]");
        } else {
            System.out.println("• École existe: " + code);
        }
    }

    private void createClasseIfNotExists(String nom, Niveau niveau, String ecoleCode, String tenantId) {
        ecoleRepository.findByCodeEcole(ecoleCode).ifPresent(ecole -> {
            if (!classeRepository.existsByNomClasseAndEcoleIdEcole(nom, ecole.getIdEcole())) {
                Classe classe = new Classe();
                classe.setNomClasse(nom);
                classe.setNiveau(niveau);
                classe.setEcole(ecole);
                classe.setTenant(tenantId);
                classeRepository.save(classe);
                System.out.println("✓ Classe créée: " + nom + " (" + ecoleCode + ")");
            } else {
                System.out.println("• Classe existe: " + nom + " (" + ecoleCode + ")");
            }
        });
    }

    private void createUserIfNotExists(String email, String password, String nom,
            String prenom, RoleType role, String tenantId) {
        if (!userRepository.existsByEmail(email)) {
            User user = new User();
            user.setEmail(email);
            user.setPasswordHash(passwordEncoder.encode(password));
            user.setNom(nom);
            user.setPrenom(prenom);
            user.setRole(role);
            user.setTenantId(tenantId);
            user.setStatus(UserStatus.ACTIVE);

            userRepository.save(user);
            System.out.println("✓ Utilisateur créé: " + email + " - Tenant: " + tenantId);
        } else {
            System.out.println("• Utilisateur existe: " + email);
        }
    }

    private void createParentIfNotExists(String matricule, String nom, String prenom, String email,
            String tel, String adresse, String quartierCode, String profession, String tenantId) {
        if (!parentRepository.existsByMatriculeParent(matricule)) {
            Parent parent = new Parent();
            parent.setMatriculeParent(matricule);
            parent.setNom(nom);
            parent.setPrenom(prenom);
            parent.setEmail(email);
            parent.setTelephone(tel);
            parent.setAdresse(adresse);
            parent.setProfession(profession);
            parent.setActif(true);
            parent.setTenant(tenantId);

            quartierRepository.findByCode(quartierCode).ifPresent(parent::setQuartier);

            parentRepository.save(parent);
            System.out.println("✓ Parent créé: " + nom + " " + prenom + " [" + matricule + "]");
        } else {
            System.out.println("• Parent existe: " + matricule);
        }
    }

    private void createEleveIfNotExists(String matricule, String nom, String prenom, String dateNaissance,
            String quartierCode, String classeNom, String tenantId) {
        if (!eleveRepository.existsByMatricule(matricule)) {
            Eleve eleve = new Eleve();
            eleve.setMatricule(matricule);
            eleve.setNom(nom);
            eleve.setPrenom(prenom);
            eleve.setDateNaissance(java.time.LocalDate.parse(dateNaissance));
            eleve.setTenant(tenantId);

            // Assigner le quartier à l'élève
            quartierRepository.findByCode(quartierCode).ifPresent(eleve::setQuartier);

            eleveRepository.save(eleve);
            System.out.println(
                    "✓ Élève créé: " + nom + " " + prenom + " [" + matricule + "] - Quartier: " + quartierCode);
        } else {
            System.out.println("• Élève existe: " + matricule);
        }
    }

    private void createEleveParentIfNotExists(String eleveMatricule, String parentMatricule,
            TypeRelation typeRelation, boolean estPrincipal, String tenantId) {
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
                    eleveParent.setTenant(tenantId);

                    eleveParentRepository.save(eleveParent);
                    System.out.println("✓ Relation créée: " + eleve.getPrenom() + " " + eleve.getNom()
                            + " → " + parent.getPrenom() + " " + parent.getNom() + " (" + typeRelation + ")");
                } else {
                    System.out.println("• Relation existe: " + eleveMatricule + " → " + parentMatricule);
                }
            });
        });
    }
}
