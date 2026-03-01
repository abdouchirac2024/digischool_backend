package com.digiSchool.digiSchool.academic.organisation.serviceimp;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.digiSchool.digiSchool.Exceptionconfig.model.Quartier;
import com.digiSchool.digiSchool.academic.organisation.dto.BrandingRequest;
import com.digiSchool.digiSchool.academic.organisation.dto.EcoleDto;
import com.digiSchool.digiSchool.academic.organisation.dto.InscriptionEcoleRequest;
import com.digiSchool.digiSchool.academic.organisation.dto.ValidationEcoleRequest;
import com.digiSchool.digiSchool.academic.organisation.model.Ecole;
import com.digiSchool.digiSchool.academic.organisation.model.SousSysteme;
import com.digiSchool.digiSchool.academic.organisation.model.StatutEcole;
import com.digiSchool.digiSchool.academic.organisation.model.TypeEtablissement;
import com.digiSchool.digiSchool.academic.organisation.model.TypeSecteur;
import com.digiSchool.digiSchool.academic.organisation.repository.ClasseRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.EcoleRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.QuartierRepository;
import com.digiSchool.digiSchool.academic.organisation.service.EcoleService;
import com.digiSchool.digiSchool.auth.model.RoleType;
import com.digiSchool.digiSchool.auth.model.User;
import com.digiSchool.digiSchool.auth.model.UserStatus;
import com.digiSchool.digiSchool.auth.repository.UserRepository;
import com.digiSchool.digiSchool.notification.model.TypeNotification;
import com.digiSchool.digiSchool.notification.service.EmailService;
import com.digiSchool.digiSchool.notification.service.NotificationService;

import com.digiSchool.digiSchool.auth.exception.ResourceNotFoundException;
import com.digiSchool.digiSchool.auth.exception.ConflictException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EcoleServiceImpl implements EcoleService {

    private final EcoleRepository ecoleRepository;
    private final QuartierRepository quartierRepository;
    private final ClasseRepository classeRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public EcoleServiceImpl(EcoleRepository ecoleRepository,
                            QuartierRepository quartierRepository,
                            ClasseRepository classeRepository,
                            UserRepository userRepository,
                            NotificationService notificationService,
                            EmailService emailService,
                            PasswordEncoder passwordEncoder) {
        this.ecoleRepository = ecoleRepository;
        this.quartierRepository = quartierRepository;
        this.classeRepository = classeRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public EcoleDto inscrireEcole(InscriptionEcoleRequest request) {
        // 1. Check if email already exists
        if (ecoleRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Une ecole avec cet email existe deja");
        }

        // 2. Lookup quartier from hierarchy
        Quartier quartier = quartierRepository.findById(request.getQuartierId())
                .orElseThrow(() -> new ResourceNotFoundException("Quartier introuvable avec l'ID: " + request.getQuartierId()));

        // 3. Generate slug from school name
        String slug = generateSlug(request.getSchoolName());
        if (ecoleRepository.existsBySlug(slug)) {
            int counter = 1;
            while (ecoleRepository.existsBySlug(slug + "-" + counter)) {
                counter++;
            }
            slug = slug + "-" + counter;
        }

        // 4. Generate unique codeEcole
        String codeEcole;
        Random random = new Random();
        do {
            int num = 100 + random.nextInt(900); // 3-digit number
            codeEcole = "EC-" + num;
        } while (ecoleRepository.existsByCodeEcole(codeEcole));

        // 5. Create Ecole with EN_ATTENTE status
        Ecole ecole = new Ecole();
        ecole.setNom(request.getSchoolName());
        ecole.setAdresse(request.getAdresse());
        ecole.setTelephone(request.getTelephone());
        ecole.setEmail(request.getEmail());
        ecole.setNombreEleves(request.getNombreEleves());
        ecole.setStatutEcole(StatutEcole.EN_ATTENTE);
        ecole.setSlug(slug);
        ecole.setCodeEcole(codeEcole);
        ecole.setQuartier(quartier);
        ecole.setTenant("TEMP");

        // Set new fields
        if (request.getTypeSecteur() != null) {
            ecole.setTypeSecteur(TypeSecteur.valueOf(request.getTypeSecteur()));
        }
        if (request.getTypeEtablissement() != null) {
            ecole.setTypeEtablissement(TypeEtablissement.valueOf(request.getTypeEtablissement()));
        }
        if (request.getSousSysteme() != null) {
            ecole.setSousSysteme(SousSysteme.valueOf(request.getSousSysteme()));
        }
        ecole.setBoitePostale(request.getBoitePostale());
        ecole.setSiteWeb(request.getSiteWeb());
        ecole.setDevise(request.getDevise());
        ecole.setAnneeFondation(request.getAnneeFondation());
        ecole.setNumeroAutorisation(request.getNumeroAutorisation());

        // 6. Save to get ID
        Ecole saved = ecoleRepository.save(ecole);

        // 7. Generate tenant ID from geographic hierarchy: CM-{REGION}-ECOLE-{padded_id}
        String regionNom = "UNKNOWN";
        try {
            regionNom = quartier.getVille()
                    .getArrondissement()
                    .getDepartement()
                    .getRegion()
                    .getNom();
        } catch (NullPointerException e) {
            // Hierarchy incomplete, use UNKNOWN
        }
        String regionNormalized = normalizeRegion(regionNom);
        String paddedId = String.format("%03d", saved.getIdEcole());
        String tenantId = "CM-" + regionNormalized + "-ECOLE-" + paddedId;
        saved.setTenant(tenantId);
        saved = ecoleRepository.save(saved);

        // 7. Create admin user with ACTIVE status (school status controls dashboard access)
        User adminUser = new User();
        adminUser.setEmail(request.getAdminEmail());
        adminUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        adminUser.setNom(request.getAdminNom());
        adminUser.setPrenom(request.getAdminPrenom());
        adminUser.setTelephone(request.getAdminTelephone());
        adminUser.setRole(RoleType.ADMIN_ECOLE);
        adminUser.setStatus(UserStatus.ACTIVE);
        adminUser.setTenantId(tenantId);
        adminUser.setEcole(saved);
        userRepository.save(adminUser);

        // 8. Notify all SUPER_ADMIN users
        List<User> superAdmins = userRepository.findByRole(RoleType.SUPER_ADMIN);
        for (User superAdmin : superAdmins) {
            notificationService.envoyerNotification(
                    superAdmin,
                    "Nouvelle inscription ecole",
                    "L'ecole \"" + request.getSchoolName() + "\" a demande une inscription.",
                    TypeNotification.INFO
            );
        }

        // 9. Send email confirmation
        emailService.sendSchoolRegistrationConfirmation(request.getAdminEmail(), request.getSchoolName());

        return toDto(saved);
    }

    @Override
    public EcoleDto validerEcole(Long ecoleId, ValidationEcoleRequest request) {
        Ecole ecole = ecoleRepository.findById(ecoleId)
                .orElseThrow(() -> new ResourceNotFoundException("Ecole introuvable"));

        if (request.isApprouve()) {
            // Approve
            ecole.setStatutEcole(StatutEcole.VALIDEE);
            ecole.setDateValidation(LocalDateTime.now());
            Ecole saved = ecoleRepository.save(ecole);

            // Activate the ADMIN_ECOLE user
            if (ecole.getUsers() != null) {
                ecole.getUsers().stream()
                        .filter(u -> u.getRole() == RoleType.ADMIN_ECOLE)
                        .findFirst()
                        .ifPresent(admin -> {
                            admin.setStatus(UserStatus.ACTIVE);
                            userRepository.save(admin);

                            // Notify the admin
                            notificationService.envoyerNotification(
                                    admin,
                                    "Ecole validee",
                                    "Votre ecole \"" + ecole.getNom() + "\" a ete validee. Vous pouvez maintenant vous connecter.",
                                    TypeNotification.VALIDATION_ECOLE
                            );

                            // Send validation email
                            emailService.sendSchoolValidationEmail(admin.getEmail(), ecole.getNom(), ecole.getSlug());
                        });
            }

            return toDto(saved);
        } else {
            // Reject
            ecole.setStatutEcole(StatutEcole.REJETEE);
            ecole.setMotifRejet(request.getMotifRejet());
            Ecole saved = ecoleRepository.save(ecole);

            // Notify the admin
            if (ecole.getUsers() != null) {
                ecole.getUsers().stream()
                        .filter(u -> u.getRole() == RoleType.ADMIN_ECOLE)
                        .findFirst()
                        .ifPresent(admin -> {
                            notificationService.envoyerNotification(
                                    admin,
                                    "Ecole rejetee",
                                    "Votre ecole \"" + ecole.getNom() + "\" a ete rejetee. Motif: " + request.getMotifRejet(),
                                    TypeNotification.REJET_ECOLE
                            );

                            // Send rejection email
                            emailService.sendSchoolRejectionEmail(admin.getEmail(), ecole.getNom(), request.getMotifRejet());
                        });
            }

            return toDto(saved);
        }
    }

    @Override
    public EcoleDto getById(Long id) {
        Ecole ecole = ecoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ecole introuvable"));
        return toDto(ecole);
    }

    @Override
    public EcoleDto getBySlug(String slug) {
        Ecole ecole = ecoleRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Ecole introuvable"));
        return toDto(ecole);
    }

    @Override
    public List<EcoleDto> getAll() {
        return ecoleRepository.findAllWithGeography().stream()
                .map(this::toDtoOptimized)
                .collect(Collectors.toList());
    }

    @Override
    public Page<EcoleDto> getAllPaged(Pageable pageable) {
        return ecoleRepository.findAll(pageable)
                .map(this::toDtoOptimized);
    }

    @Override
    public List<EcoleDto> getByStatut(StatutEcole statut) {
        return ecoleRepository.findByStatutEcole(statut).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EcoleDto updateBranding(Long ecoleId, BrandingRequest request) {
        Ecole ecole = ecoleRepository.findById(ecoleId)
                .orElseThrow(() -> new ResourceNotFoundException("Ecole introuvable"));

        if (request.getLogoUrl() != null) ecole.setLogoUrl(request.getLogoUrl());
        if (request.getCouleurPrimaire() != null) ecole.setCouleurPrimaire(request.getCouleurPrimaire());
        if (request.getCouleurSecondaire() != null) ecole.setCouleurSecondaire(request.getCouleurSecondaire());

        Ecole saved = ecoleRepository.save(ecole);
        return toDto(saved);
    }

    @Override
    public Map<String, Long> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", ecoleRepository.count());
        stats.put("enAttente", ecoleRepository.countByStatutEcole(StatutEcole.EN_ATTENTE));
        stats.put("validees", ecoleRepository.countByStatutEcole(StatutEcole.VALIDEE));
        stats.put("rejetees", ecoleRepository.countByStatutEcole(StatutEcole.REJETEE));
        stats.put("suspendues", ecoleRepository.countByStatutEcole(StatutEcole.SUSPENDUE));
        return stats;
    }

    @Override
    public void delete(Long id) {
        Ecole ecole = ecoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ecole introuvable"));
        ecoleRepository.delete(ecole);
    }

    private EcoleDto toDto(Ecole ecole) {
        EcoleDto dto = new EcoleDto();
        dto.setId(ecole.getIdEcole());
        dto.setCodeEcole(ecole.getCodeEcole());
        dto.setNom(ecole.getNom());
        dto.setAdresse(ecole.getAdresse());
        dto.setTelephone(ecole.getTelephone());
        dto.setEmail(ecole.getEmail());
        dto.setStatutEcole(ecole.getStatutEcole());
        dto.setSlug(ecole.getSlug());
        dto.setMotifRejet(ecole.getMotifRejet());
        dto.setDateValidation(ecole.getDateValidation());
        dto.setNombreEleves(ecole.getNombreEleves());
        dto.setLogoUrl(ecole.getLogoUrl());
        dto.setCouleurPrimaire(ecole.getCouleurPrimaire());
        dto.setCouleurSecondaire(ecole.getCouleurSecondaire());
        dto.setCreatedAt(ecole.getCreatedAt());
        dto.setUpdatedAt(ecole.getUpdatedAt());

        // New fields
        if (ecole.getTypeSecteur() != null) {
            dto.setTypeSecteur(ecole.getTypeSecteur().name());
        }
        if (ecole.getTypeEtablissement() != null) {
            dto.setTypeEtablissement(ecole.getTypeEtablissement().name());
        }
        if (ecole.getSousSysteme() != null) {
            dto.setSousSysteme(ecole.getSousSysteme().name());
        }
        dto.setBoitePostale(ecole.getBoitePostale());
        dto.setSiteWeb(ecole.getSiteWeb());
        dto.setDevise(ecole.getDevise());
        dto.setAnneeFondation(ecole.getAnneeFondation());
        dto.setNumeroAutorisation(ecole.getNumeroAutorisation());

        // Quartier and geographic hierarchy
        if (ecole.getQuartier() != null) {
            dto.setQuartierId(ecole.getQuartier().getId());
            dto.setQuartierNom(ecole.getQuartier().getNom());
            try {
                dto.setVilleNom(ecole.getQuartier().getVille().getNom());
                dto.setDepartementNom(ecole.getQuartier().getVille()
                        .getArrondissement().getDepartement().getNom());
                String regionNom = ecole.getQuartier()
                        .getVille()
                        .getArrondissement()
                        .getDepartement()
                        .getRegion()
                        .getNom();
                dto.setRegionNom(regionNom);
            } catch (NullPointerException e) {
                // Hierarchy incomplete, skip
            }
        }

        // Directeur (ADMIN_ECOLE user)
        if (ecole.getUsers() != null) {
            ecole.getUsers().stream()
                    .filter(u -> u.getRole() == RoleType.ADMIN_ECOLE)
                    .findFirst()
                    .ifPresent(admin -> {
                        dto.setDirecteurNom(admin.getNom() + " " + admin.getPrenom());
                        dto.setDirecteurEmail(admin.getEmail());
                    });
        }

        // Nombre de classes
        dto.setNombreClasses(ecole.getClasses() != null ? ecole.getClasses().size() : 0);

        return dto;
    }

    private EcoleDto toDtoOptimized(Ecole ecole) {
        EcoleDto dto = new EcoleDto();
        dto.setId(ecole.getIdEcole());
        dto.setCodeEcole(ecole.getCodeEcole());
        dto.setNom(ecole.getNom());
        dto.setAdresse(ecole.getAdresse());
        dto.setTelephone(ecole.getTelephone());
        dto.setEmail(ecole.getEmail());
        dto.setStatutEcole(ecole.getStatutEcole());
        dto.setSlug(ecole.getSlug());
        dto.setMotifRejet(ecole.getMotifRejet());
        dto.setDateValidation(ecole.getDateValidation());
        dto.setNombreEleves(ecole.getNombreEleves());
        dto.setLogoUrl(ecole.getLogoUrl());
        dto.setCouleurPrimaire(ecole.getCouleurPrimaire());
        dto.setCouleurSecondaire(ecole.getCouleurSecondaire());
        dto.setCreatedAt(ecole.getCreatedAt());
        dto.setUpdatedAt(ecole.getUpdatedAt());

        if (ecole.getTypeSecteur() != null) {
            dto.setTypeSecteur(ecole.getTypeSecteur().name());
        }
        if (ecole.getTypeEtablissement() != null) {
            dto.setTypeEtablissement(ecole.getTypeEtablissement().name());
        }
        if (ecole.getSousSysteme() != null) {
            dto.setSousSysteme(ecole.getSousSysteme().name());
        }
        dto.setBoitePostale(ecole.getBoitePostale());
        dto.setSiteWeb(ecole.getSiteWeb());
        dto.setDevise(ecole.getDevise());
        dto.setAnneeFondation(ecole.getAnneeFondation());
        dto.setNumeroAutorisation(ecole.getNumeroAutorisation());

        // Geography is already fetched via JOIN FETCH
        if (ecole.getQuartier() != null) {
            dto.setQuartierId(ecole.getQuartier().getId());
            dto.setQuartierNom(ecole.getQuartier().getNom());
            try {
                dto.setVilleNom(ecole.getQuartier().getVille().getNom());
                dto.setDepartementNom(ecole.getQuartier().getVille()
                        .getArrondissement().getDepartement().getNom());
                dto.setRegionNom(ecole.getQuartier()
                        .getVille()
                        .getArrondissement()
                        .getDepartement()
                        .getRegion()
                        .getNom());
            } catch (NullPointerException e) {
                // Hierarchy incomplete, skip
            }
        }

        // Use dedicated query instead of lazy-loading ecole.getUsers()
        Optional<User> admin = userRepository.findFirstByEcoleIdEcoleAndRole(
                ecole.getIdEcole(), RoleType.ADMIN_ECOLE);
        admin.ifPresent(a -> {
            dto.setDirecteurNom(a.getNom() + " " + a.getPrenom());
            dto.setDirecteurEmail(a.getEmail());
        });

        // Use COUNT query instead of lazy-loading ecole.getClasses().size()
        dto.setNombreClasses((int) classeRepository.countByEcoleIdEcole(ecole.getIdEcole()));

        return dto;
    }

    private String generateSlug(String nom) {
        if (nom == null) return "";
        // Normalize accents
        String normalized = Normalizer.normalize(nom, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        // Lowercase
        normalized = normalized.toLowerCase();
        // Replace spaces and special chars with hyphens
        normalized = normalized.replaceAll("[^a-z0-9]", "-");
        // Remove consecutive hyphens
        normalized = normalized.replaceAll("-+", "-");
        // Remove leading/trailing hyphens
        normalized = normalized.replaceAll("^-|-$", "");
        // Truncate to 60 chars
        if (normalized.length() > 60) {
            normalized = normalized.substring(0, 60);
            // Don't end with a hyphen
            normalized = normalized.replaceAll("-$", "");
        }
        return normalized;
    }

    private String normalizeRegion(String region) {
        if (region == null) return "UNKNOWN";
        String normalized = Normalizer.normalize(region, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        normalized = normalized.toUpperCase();
        normalized = normalized.replace(" ", "-");
        return normalized;
    }
}
