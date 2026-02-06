package com.digiSchool.digiSchool.auth.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.digiSchool.digiSchool.auth.dto.LoginRequest;
import com.digiSchool.digiSchool.auth.dto.LoginResponse;
import com.digiSchool.digiSchool.auth.dto.UserDto;
import com.digiSchool.digiSchool.auth.exception.AuthenticationException;
import com.digiSchool.digiSchool.user.model.StatutUtilisateur;
import com.digiSchool.digiSchool.user.model.Utilisateur;
import com.digiSchool.digiSchool.user.repository.UtilisateurRepository;

/**
 * Service d'authentification.
 * Gère la logique métier de connexion, déconnexion et validation des utilisateurs.
 * Supporte le login via email OU téléphone.
 */
@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UtilisateurRepository utilisateurRepository,
                      PasswordEncoder passwordEncoder,
                      JwtService jwtService) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Authentifie un utilisateur avec email/téléphone et mot de passe.
     *
     * @param request Requête de connexion contenant identifier (email ou téléphone) et mot de passe
     * @return Réponse contenant le token JWT et les infos utilisateur
     * @throws AuthenticationException si l'authentification échoue
     */
    public LoginResponse authenticate(LoginRequest request) {
        String identifier = request.getIdentifier();
        if (identifier == null || identifier.trim().isEmpty()) {
            throw new AuthenticationException("Email ou téléphone requis");
        }

        // Rechercher l'utilisateur par email OU téléphone
        Utilisateur user = utilisateurRepository.findByEmailOrTelephone(identifier.trim())
                .orElseThrow(() -> new AuthenticationException("Identifiant ou mot de passe incorrect"));

        // Vérifier le mot de passe
        if (!isPasswordValid(request.getPassword(), user.getMotDePasse())) {
            throw new AuthenticationException("Identifiant ou mot de passe incorrect");
        }

        // Vérifier que le compte est actif
        if (user.getStatut() == null || user.getStatut() == StatutUtilisateur.INACTIF) {
            throw new AuthenticationException("Compte désactivé. Contactez l'administrateur.", 403);
        }

        if (user.getStatut() == StatutUtilisateur.EN_ATTENTE) {
            throw new AuthenticationException("Compte en attente de validation", 403);
        }

        // Mettre à jour la dernière connexion
        user.setDerniereConnexion(LocalDateTime.now());
        utilisateurRepository.save(user);

        // Générer le token JWT
        Long ecoleId = user.getEcole() != null ? user.getEcole().getIdEcole() : null;
        String roleName = user.getRole() != null ? user.getRole().getNomRole() : null;
        String token = jwtService.generateToken(user.getEmail(), ecoleId, roleName);
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        // Construire la réponse
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        response.setUser(toUserDto(user));
        response.setMessage("Connexion réussie");
        response.setExpiresIn(jwtService.getExpirationTime());

        return response;
    }

    /**
     * Récupère l'utilisateur à partir du token JWT.
     *
     * @param token Token JWT (sans le préfixe "Bearer ")
     * @return DTO de l'utilisateur
     * @throws AuthenticationException si le token est invalide
     */
    public UserDto getUserFromToken(String token) {
        if (!jwtService.isTokenValid(token)) {
            throw new AuthenticationException("Token invalide ou expiré");
        }

        String email = jwtService.extractUsername(token);
        Utilisateur user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Utilisateur non trouvé", 404));

        return toUserDto(user);
    }

    /**
     * Rafraîchit le token JWT.
     *
     * @param refreshToken Le refresh token
     * @return Nouvelle réponse avec nouveaux tokens
     * @throws AuthenticationException si le refresh token est invalide
     */
    public LoginResponse refreshToken(String refreshToken) {
        if (!jwtService.isTokenValid(refreshToken)) {
            throw new AuthenticationException("Refresh token invalide ou expiré");
        }

        String email = jwtService.extractUsername(refreshToken);
        Utilisateur user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Utilisateur non trouvé"));

        // Vérifier que le compte est toujours actif
        if (user.getStatut() != StatutUtilisateur.ACTIF) {
            throw new AuthenticationException("Compte non actif", 403);
        }

        // Générer de nouveaux tokens
        Long ecoleId = user.getEcole() != null ? user.getEcole().getIdEcole() : null;
        String roleName = user.getRole() != null ? user.getRole().getNomRole() : null;
        String newToken = jwtService.generateToken(user.getEmail(), ecoleId, roleName);
        String newRefreshToken = jwtService.generateRefreshToken(user.getEmail());

        LoginResponse response = new LoginResponse();
        response.setToken(newToken);
        response.setRefreshToken(newRefreshToken);
        response.setUser(toUserDto(user));
        response.setMessage("Token rafraîchi avec succès");
        response.setExpiresIn(jwtService.getExpirationTime());

        return response;
    }

    /**
     * Vérifie si le mot de passe est valide.
     * Utilise uniquement BCrypt pour la sécurité.
     */
    private boolean isPasswordValid(String rawPassword, String encodedPassword) {
        if (encodedPassword == null || encodedPassword.isEmpty()) {
            return false;
        }

        try {
            return passwordEncoder.matches(rawPassword, encodedPassword);
        } catch (Exception e) {
            // Format de hash invalide
            return false;
        }
    }

    /**
     * Convertit un Utilisateur en UserDto.
     */
    public UserDto toUserDto(Utilisateur user) {
        UserDto dto = new UserDto();
        dto.setId(user.getIdUtilisateur());
        dto.setNom(user.getNom());
        dto.setPrenom(user.getPrenom());
        dto.setEmail(user.getEmail());
        dto.setTelephone(user.getTelephone());
        dto.setStatut(user.getStatut() != null ? user.getStatut().name() : null);
        dto.setDerniereConnexion(user.getDerniereConnexion());

        if (user.getRole() != null) {
            dto.setRoleId(user.getRole().getIdRole());
            dto.setRoleName(user.getRole().getNomRole());
        }

        if (user.getEcole() != null) {
            dto.setEcoleId(user.getEcole().getIdEcole());
            dto.setEcoleNom(user.getEcole().getNom());
        }

        return dto;
    }

    /**
     * Récupère un utilisateur par son email.
     */
    public Utilisateur findByEmail(String email) {
        return utilisateurRepository.findByEmail(email).orElse(null);
    }

    /**
     * Récupère un utilisateur par son téléphone.
     */
    public Utilisateur findByTelephone(String telephone) {
        return utilisateurRepository.findByTelephone(telephone).orElse(null);
    }

    /**
     * Vérifie si un email existe déjà.
     */
    public boolean emailExists(String email) {
        return utilisateurRepository.existsByEmail(email);
    }

    /**
     * Vérifie si un téléphone existe déjà.
     */
    public boolean telephoneExists(String telephone) {
        return utilisateurRepository.existsByTelephone(telephone);
    }
}
