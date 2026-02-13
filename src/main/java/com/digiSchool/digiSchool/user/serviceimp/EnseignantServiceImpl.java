package com.digiSchool.digiSchool.user.serviceimp;

import com.digiSchool.digiSchool.auth.model.RoleType;
import com.digiSchool.digiSchool.auth.model.User;
import com.digiSchool.digiSchool.auth.repository.UserRepository;
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
        // Create User first
        User user = new User();
        user.setNom(dto.getNom());
        user.setPrenom(dto.getPrenom());
        user.setEmail(dto.getEmail());
        // Default password or generated
        user.setPasswordHash(passwordEncoder.encode("DefaultPassword123!"));
        user.setRole(RoleType.ENSEIGNANT);
        // TenantId is handled by context or needs to be set if manual creation.
        // Assuming TenantContext is available, but User entity requires it.
        // In a real flow, this comes from the logged-in admin's context.
        // user.setTenantId(TenantContext.getTenant());

        // However, standard saving might require manual setting if not intercepted or
        // if context is missing.
        // Letting standard flow handle it if possible, otherwise we might need to set
        // it.

        user = userRepository.save(user);

        Enseignant enseignant = new Enseignant();
        enseignant.setUser(user);
        enseignant.setTelephone(dto.getTelephone());
        enseignant.setSpecialite(dto.getSpecialite());
        enseignant.setGrade(dto.getGrade());
        enseignant.setExperience(dto.getExperience());
        enseignant.setBio(dto.getBio());
        enseignant.setPhotoUrl(dto.getPhotoUrl());

        return enseignantRepository.save(enseignant);
    }

    @Override
    public Enseignant updateEnseignant(Long id, EnseignantDto dto) {
        Enseignant enseignant = enseignantRepository.findById(id)
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
            // Email update might require verification
            userRepository.save(user);
        }

        return enseignantRepository.save(enseignant);
    }

    @Override
    public EnseignantDto getEnseignantById(Long id) {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé"));
        return convertToDto(enseignant);
    }

    @Override
    public List<EnseignantDto> getAllEnseignants() {
        return enseignantRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteEnseignant(Long id) {
        enseignantRepository.deleteById(id);
        // Optionally delete linked user or deactivate
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
