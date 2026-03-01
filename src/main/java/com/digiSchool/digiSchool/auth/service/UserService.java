package com.digiSchool.digiSchool.auth.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.digiSchool.digiSchool.auth.dto.ChangePasswordRequest;
import com.digiSchool.digiSchool.auth.dto.RegisterRequest;
import com.digiSchool.digiSchool.auth.dto.UpdateProfileRequest;
import com.digiSchool.digiSchool.auth.exception.AuthenticationException;
import com.digiSchool.digiSchool.auth.model.RoleType;
import com.digiSchool.digiSchool.auth.model.User;
import com.digiSchool.digiSchool.auth.model.UserStatus;
import com.digiSchool.digiSchool.auth.repository.RefreshTokenRepository;
import com.digiSchool.digiSchool.auth.repository.UserRepository;
import com.digiSchool.digiSchool.storage.MinioStorageService;
import com.digiSchool.digiSchool.storage.StorageException;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final PresenceService presenceService;
    private final MinioStorageService storageService;

    public UserService(UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            PresenceService presenceService,
            MinioStorageService storageService) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.presenceService = presenceService;
        this.storageService = storageService;
    }

    public User createUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail().toLowerCase().trim())) {
            throw new AuthenticationException("Cet email est déjà utilisé");
        }

        User user = new User();
        user.setEmail(request.getEmail().toLowerCase().trim());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setRole(request.getRole());
        user.setTenantId(request.getTenantId());
        user.setStatus(UserStatus.ACTIVE);

        return userRepository.save(user);
    }

    public User createAdminEcole(String email, String password, String nom, String prenom, String tenantId) {
        RegisterRequest request = new RegisterRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setNom(nom);
        request.setPrenom(prenom);
        request.setRole(RoleType.ADMIN_ECOLE);
        request.setTenantId(tenantId);

        return createUser(request);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AuthenticationException("Utilisateur non trouvé"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new AuthenticationException("Utilisateur non trouvé"));
    }

    public List<User> getUsersByTenant(String tenantId) {
        return userRepository.findByTenantId(tenantId);
    }

    public List<User> getUsersByTenantAndRole(String tenantId, RoleType role) {
        return userRepository.findByTenantIdAndRole(tenantId, role);
    }

    public User updateUserStatus(Long userId, UserStatus status) {
        User user = getUserById(userId);
        user.setStatus(status);
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email.toLowerCase().trim());
    }

    public List<User> getAllUsers() {
        return userRepository.findAllByOrderByCreatedAtDesc();
    }

    public Map<String, Long> getUserStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", userRepository.count());
        stats.put("active", userRepository.countByStatus(UserStatus.ACTIVE));
        stats.put("inactive", userRepository.countByStatus(UserStatus.INACTIVE));
        stats.put("locked", userRepository.countByStatus(UserStatus.LOCKED));
        stats.put("pending", userRepository.countByStatus(UserStatus.PENDING));
        stats.put("connected", presenceService.getOnlineCount());
        return stats;
    }

    public List<User> getConnectedUsers() {
        Set<Long> onlineIds = presenceService.getOnlineUserIds();
        if (onlineIds.isEmpty()) {
            return new ArrayList<>();
        }
        return userRepository.findAllById(onlineIds);
    }

    public List<Long> getConnectedUserIds() {
        return new ArrayList<>(presenceService.getOnlineUserIds());
    }

    public User updateProfile(Long userId, UpdateProfileRequest request) {
        User user = getUserById(userId);

        if (request.getTelephone() != null && !request.getTelephone().isBlank()) {
            userRepository.findByTelephoneAndIdNot(request.getTelephone(), userId)
                    .ifPresent(existing -> {
                        throw new AuthenticationException("Ce numéro de téléphone est déjà utilisé");
                    });
            user.setTelephone(request.getTelephone());
        } else {
            user.setTelephone(null);
        }

        user.setNom(request.getNom().trim());
        user.setPrenom(request.getPrenom().trim());

        return userRepository.save(user);
    }

    /**
     * Upload de la photo de profil via MinIO — compatible multi-instances.
     * La validation (taille, type MIME) est déléguée à MinioStorageService.
     */
    public User uploadAvatar(Long userId, MultipartFile file) {
        User user = getUserById(userId);

        try {
            // Supprimer l'ancien avatar de MinIO si existant
            if (user.getAvatarUrl() != null) {
                storageService.deleteAvatar(user.getAvatarUrl());
            }

            // Upload vers MinIO
            String avatarUrl = storageService.uploadAvatar(userId, file);
            user.setAvatarUrl(avatarUrl);
            return userRepository.save(user);

        } catch (StorageException e) {
            throw new AuthenticationException(e.getMessage());
        } catch (Exception e) {
            throw new AuthenticationException("Erreur lors de l'upload de la photo : " + e.getMessage());
        }
    }

    /**
     * Suppression de la photo de profil depuis MinIO.
     */
    public User deleteAvatar(Long userId) {
        User user = getUserById(userId);

        if (user.getAvatarUrl() != null) {
            storageService.deleteAvatar(user.getAvatarUrl());
            user.setAvatarUrl(null);
            return userRepository.save(user);
        }

        return user;
    }

    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = getUserById(userId);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new AuthenticationException("Le mot de passe actuel est incorrect");
        }
        if (passwordEncoder.matches(request.getNewPassword(), user.getPasswordHash())) {
            throw new AuthenticationException("Le nouveau mot de passe doit être différent de l'ancien");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AuthenticationException("Les mots de passe ne correspondent pas");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
