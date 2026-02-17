package com.digiSchool.digiSchool.user.serviceimp;

import com.digiSchool.digiSchool.auth.model.RoleType;
import com.digiSchool.digiSchool.auth.model.User;
import com.digiSchool.digiSchool.auth.repository.UserRepository;
import com.digiSchool.digiSchool.Exceptionconfig.service.TenantContext;
import com.digiSchool.digiSchool.user.dto.EnseignantDto;
import com.digiSchool.digiSchool.user.model.Enseignant;
import com.digiSchool.digiSchool.user.repository.EnseignantRepository;
import com.digiSchool.digiSchool.user.service.EnseignantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EnseignantServiceImpl implements EnseignantService {

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Enseignant createEnseignant(EnseignantDto dto) {
        String tenant = TenantContext.getTenant();

        // Create User first
        User user = new User();
        user.setNom(dto.getNom());
        user.setPrenom(dto.getPrenom());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode("DefaultPassword123!"));
        user.setRole(RoleType.ENSEIGNANT);
        user.setTenantId(tenant);
        user = userRepository.save(user);

        Enseignant enseignant = new Enseignant();
        enseignant.setUser(user);
        enseignant.setTelephone(dto.getTelephone());
        enseignant.setSpecialite(dto.getSpecialite());
        enseignant.setGrade(dto.getGrade());
        enseignant.setExperience(dto.getExperience());
        enseignant.setBio(dto.getBio());
        enseignant.setPhotoUrl(dto.getPhotoUrl());
        enseignant.setTenant(tenant);

        return enseignantRepository.save(enseignant);
    }

    @Override
    public Enseignant updateEnseignant(Long id, EnseignantDto dto) {
        String tenant = TenantContext.getTenant();
        Enseignant enseignant = enseignantRepository.findByIdAndTenant(id, tenant)
                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé"));

        enseignant.setTelephone(dto.getTelephone());
        enseignant.setSpecialite(dto.getSpecialite());
        enseignant.setGrade(dto.getGrade());
        enseignant.setExperience(dto.getExperience());
        enseignant.setBio(dto.getBio());

        User user = enseignant.getUser();
        if (user != null) {
            user.setNom(dto.getNom());
            user.setPrenom(dto.getPrenom());
            userRepository.save(user);
        }

        return enseignantRepository.save(enseignant);
    }

    @Override
    public EnseignantDto getEnseignantById(Long id) {
        String tenant = TenantContext.getTenant();
        Enseignant enseignant = enseignantRepository.findByIdAndTenant(id, tenant)
                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé"));
        return convertToDto(enseignant);
    }

    @Override
    public List<EnseignantDto> getAllEnseignants() {
        String tenant = TenantContext.getTenant();
        return enseignantRepository.findAllByTenant(tenant).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteEnseignant(Long id) {
        String tenant = TenantContext.getTenant();
        Enseignant enseignant = enseignantRepository.findByIdAndTenant(id, tenant)
                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé"));
        enseignantRepository.delete(enseignant);
    }

    private EnseignantDto convertToDto(Enseignant enseignant) {
        EnseignantDto dto = new EnseignantDto();
        dto.setId(enseignant.getId());
        dto.setTelephone(enseignant.getTelephone());
        dto.setSpecialite(enseignant.getSpecialite());
        dto.setGrade(enseignant.getGrade());
        dto.setExperience(enseignant.getExperience());
        dto.setBio(enseignant.getBio());
        dto.setPhotoUrl(enseignant.getPhotoUrl());

        if (enseignant.getUser() != null) {
            dto.setUserId(enseignant.getUser().getId());
            dto.setNom(enseignant.getUser().getNom());
            dto.setPrenom(enseignant.getUser().getPrenom());
            dto.setEmail(enseignant.getUser().getEmail());
        }
        return dto;
    }
}
