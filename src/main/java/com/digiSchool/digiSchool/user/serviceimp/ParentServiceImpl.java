package com.digiSchool.digiSchool.user.serviceimp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.digiSchool.digiSchool.Exceptionconfig.model.Quartier;
import com.digiSchool.digiSchool.Exceptionconfig.service.TenantContext;
import com.digiSchool.digiSchool.academic.organisation.model.Ecole;
import com.digiSchool.digiSchool.academic.organisation.repository.EcoleRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.QuartierRepository;
import com.digiSchool.digiSchool.auth.model.RoleType;
import com.digiSchool.digiSchool.auth.model.User;
import com.digiSchool.digiSchool.auth.model.UserStatus;
import com.digiSchool.digiSchool.auth.repository.UserRepository;
import com.digiSchool.digiSchool.user.dto.ParentDto;
import com.digiSchool.digiSchool.user.model.Parent;
import com.digiSchool.digiSchool.user.repository.ParentRepository;
import com.digiSchool.digiSchool.user.service.ParentService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ParentServiceImpl implements ParentService {

    private final ParentRepository parentRepository;
    private final QuartierRepository quartierRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EcoleRepository ecoleRepository;

    public ParentServiceImpl(ParentRepository parentRepository, QuartierRepository quartierRepository,
            UserRepository userRepository, PasswordEncoder passwordEncoder, EcoleRepository ecoleRepository) {
        this.parentRepository = parentRepository;
        this.quartierRepository = quartierRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.ecoleRepository = ecoleRepository;
    }

    @Override
    public ParentDto create(ParentDto dto) {
        String tenant = TenantContext.getTenant();

        // Validation de l'unicité de l'email
        if (dto.getEmail() != null && parentRepository.existsByEmailAndTenant(dto.getEmail(), tenant)) {
            throw new RuntimeException("Un parent avec cet email existe déjà");
        }

        // Validation de l'unicité du téléphone
        if (dto.getTelephone() != null && parentRepository.existsByTelephoneAndTenant(dto.getTelephone(), tenant)) {
            throw new RuntimeException("Un parent avec ce numéro de téléphone existe déjà");
        }

        // Vérifier que l'email n'est pas déjà utilisé dans la table users
        if (dto.getEmail() != null && userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Un compte utilisateur avec cet email existe déjà");
        }

        // Validation du quartier obligatoire
        if (dto.getQuartierId() == null) {
            throw new RuntimeException("Le quartier est obligatoire");
        }

        Parent parent = toEntity(dto);
        parent.setTenant(tenant);
        parent.setMatriculeParent(generateMatricule(tenant));
        parent.setActif(true);

        Parent saved = parentRepository.save(parent);

        // Créer automatiquement un compte utilisateur pour le parent
        String generatedPassword = generatePassword();
        createUserAccount(dto, tenant, saved, generatedPassword);

        ParentDto result = toDto(saved);
        result.setGeneratedPassword(generatedPassword);
        return result;
    }

    private String generatePassword() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return "Parent@" + code;
    }

    private void createUserAccount(ParentDto dto, String tenant, Parent parent, String rawPassword) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setNom(dto.getNom());
        user.setPrenom(dto.getPrenom());
        user.setTelephone(dto.getTelephone());
        user.setRole(RoleType.PARENT);
        user.setStatus(UserStatus.ACTIVE);
        user.setTenantId(tenant);

        // Associer l'école du tenant
        Ecole ecole = ecoleRepository.findByTenant(tenant).orElse(null);
        if (ecole != null) {
            user.setEcole(ecole);
        }

        // Associer le quartier si renseigné
    

        userRepository.save(user);
    }

    @Override
    public ParentDto update(Long id, ParentDto dto) {
        String tenant = TenantContext.getTenant();
        Parent parent = parentRepository.findByIdAndTenant(id, tenant)
                .orElseThrow(() -> new RuntimeException("Parent introuvable"));

        // Validation de l'unicité de l'email (si modifié)
        if (dto.getEmail() != null && !dto.getEmail().equals(parent.getEmail())) {
            if (parentRepository.existsByEmailAndTenant(dto.getEmail(), tenant)) {
                throw new RuntimeException("Un parent avec cet email existe déjà");
            }
        }

        // Validation de l'unicité du téléphone (si modifié)
        if (dto.getTelephone() != null && !dto.getTelephone().equals(parent.getTelephone())) {
            if (parentRepository.existsByTelephoneAndTenant(dto.getTelephone(), tenant)) {
                throw new RuntimeException("Un parent avec ce numéro de téléphone existe déjà");
            }
        }

        parent.setNom(dto.getNom());
        parent.setPrenom(dto.getPrenom());
        parent.setEmail(dto.getEmail());
        parent.setTelephone(dto.getTelephone());
        parent.setTelephoneSecondaire(dto.getTelephoneSecondaire());
        parent.setAdresse(dto.getAdresse());
        parent.setProfession(dto.getProfession());
        parent.setEmployeur(dto.getEmployeur());
        parent.setPhotoUrl(dto.getPhotoUrl());
        parent.setPieceIdentiteType(dto.getPieceIdentiteType());
        parent.setPieceIdentiteNumero(dto.getPieceIdentiteNumero());

        if (dto.getActif() != null) {
            parent.setActif(dto.getActif());
        }

//        if (dto.getQuartierId() != null) {
//            Quartier quartier = quartierRepository.findById(dto.getQuartierId())
//                    .orElseThrow(() -> new RuntimeException("Quartier introuvable"));
//            parent.setQuartier(quartier);
//        }

        return toDto(parentRepository.save(parent));
    }

    @Override
    public ParentDto getById(Long id) {
        String tenant = TenantContext.getTenant();
        return parentRepository.findByIdAndTenant(id, tenant)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Parent introuvable"));
    }

    @Override
    public ParentDto getByMatricule(String matricule) {
        String tenant = TenantContext.getTenant();
        return parentRepository.findByMatriculeParentAndTenant(matricule, tenant)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Parent introuvable"));
    }

    @Override
    public List<ParentDto> getAll() {
        String tenant = TenantContext.getTenant();
        return parentRepository.findAllByTenant(tenant)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<ParentDto> search(String query) {
        String tenant = TenantContext.getTenant();
        return parentRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(query, query)
                .stream()
                .filter(p -> p.getDeletedAt() == null && tenant.equals(p.getTenant()))
                .map(this::toDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        String tenant = TenantContext.getTenant();
        Parent parent = parentRepository.findByIdAndTenant(id, tenant)
                .orElseThrow(() -> new RuntimeException("Parent introuvable"));

        // Soft delete
        parent.setDeletedAt(LocalDateTime.now());
        parent.setActif(false);
        parentRepository.save(parent);
    }

    // =====================
    // Génération du matricule
    // =====================

    private String generateMatricule(String tenant) {
        // Générer un matricule unique globalement en utilisant le tenant comme préfixe
        Integer maxNumber = parentRepository.findMaxMatriculeNumber(tenant);
        int nextNumber = (maxNumber != null ? maxNumber : 0) + 1;
        // Format: {TENANT}-PAR-{NUMBER} pour garantir l'unicité globale
        return String.format("%s-PAR-%05d", tenant, nextNumber);
    }

    // =====================
    // Mapping DTO <-> Entity
    // =====================

    private Parent toEntity(ParentDto dto) {
        Parent parent = new Parent();
        parent.setNom(dto.getNom());
        parent.setPrenom(dto.getPrenom());
        parent.setEmail(dto.getEmail());
        parent.setTelephone(dto.getTelephone());
        parent.setTelephoneSecondaire(dto.getTelephoneSecondaire());
        parent.setAdresse(dto.getAdresse());
        parent.setProfession(dto.getProfession());
        parent.setEmployeur(dto.getEmployeur());
        parent.setPhotoUrl(dto.getPhotoUrl());
        parent.setPieceIdentiteType(dto.getPieceIdentiteType());
        parent.setPieceIdentiteNumero(dto.getPieceIdentiteNumero());

//        if (dto.getQuartierId() != null) {
//            Quartier quartier = quartierRepository.findById(dto.getQuartierId())
//                    .orElseThrow(() -> new RuntimeException("Quartier introuvable"));
//            parent.setQuartier(quartier);
//        }

        return parent;
    }

    private ParentDto toDto(Parent parent) {
        ParentDto dto = new ParentDto();
        dto.setIdParent(parent.getIdParent());
        dto.setMatriculeParent(parent.getMatriculeParent());
        dto.setNom(parent.getNom());
        dto.setPrenom(parent.getPrenom());
        dto.setEmail(parent.getEmail());
        dto.setTelephone(parent.getTelephone());
        dto.setTelephoneSecondaire(parent.getTelephoneSecondaire());
        dto.setAdresse(parent.getAdresse());
        dto.setProfession(parent.getProfession());
        dto.setEmployeur(parent.getEmployeur());
        dto.setPhotoUrl(parent.getPhotoUrl());
        dto.setPieceIdentiteType(parent.getPieceIdentiteType());
        dto.setPieceIdentiteNumero(parent.getPieceIdentiteNumero());
        dto.setActif(parent.getActif());
        dto.setCreatedAt(parent.getCreatedAt());
        dto.setUpdatedAt(parent.getUpdatedAt());
        dto.setDeletedAt(parent.getDeletedAt());

//        if (parent.getQuartier() != null) {
//            dto.setQuartierId(parent.getQuartier().getId());
//            dto.setQuartierNom(parent.getQuartier().getNom());
//        }

        return dto;
    }
}
