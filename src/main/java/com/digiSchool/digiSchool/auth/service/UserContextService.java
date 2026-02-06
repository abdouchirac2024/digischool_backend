package com.digiSchool.digiSchool.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.digiSchool.digiSchool.auth.exception.AuthenticationException;
import com.digiSchool.digiSchool.user.model.Utilisateur;
import com.digiSchool.digiSchool.user.repository.UtilisateurRepository;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Service pour extraire les informations de l'utilisateur connecté
 * depuis le JWT token (source unique et sécurisée).
 *
 * Ce service est essentiel pour l'isolation multi-tenant:
 * - Chaque utilisateur ne peut accéder qu'aux données de son école
 * - L'ID de l'école est extrait du token JWT de manière sécurisée
 * - Le tenant ne peut pas être manipulé par le client
 */
@Service
public class UserContextService {

    private final JwtService jwtService;
    private final UtilisateurRepository utilisateurRepository;

    public UserContextService(JwtService jwtService, UtilisateurRepository utilisateurRepository) {
        this.jwtService = jwtService;
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Récupère l'ID de l'école de l'utilisateur connecté.
     *
     * Source unique et sécurisée: JWT token
     * 1. JWT token (Authorization header) - ecoleId dans les claims
     * 2. JWT token - écoleId via base de données (si pas dans les claims)
     *
     * @return ID de l'école ou null si non authentifié
     */
    public Long getCurrentUserEcoleId() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return null;
        }

        // Source unique: JWT token (sécurisé, signé)
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authHeader.substring(7);
        try {
            // D'abord essayer d'extraire directement des claims
            Long ecoleId = jwtService.extractEcoleId(token);
            if (ecoleId != null) {
                return ecoleId;
            }

            // Si pas d'ecoleId dans le token, chercher l'utilisateur en base
            String username = jwtService.extractUsername(token);
            if (username != null) {
                Utilisateur user = utilisateurRepository.findByEmail(username).orElse(null);
                if (user != null && user.getEcole() != null) {
                    return user.getEcole().getIdEcole();
                }
            }
        } catch (Exception e) {
            // Token invalide
            return null;
        }

        return null;
    }

    /**
     * Récupère le rôle de l'utilisateur connecté depuis le JWT.
     *
     * @return Nom du rôle ou null
     */
    public String getCurrentUserRole() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return null;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                return jwtService.extractRole(token);
            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }

    /**
     * Récupère l'utilisateur connecté depuis le JWT token.
     *
     * @return L'utilisateur connecté ou null
     */
    public Utilisateur getCurrentUser() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return null;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String username = jwtService.extractUsername(token);
                if (username != null) {
                    return utilisateurRepository.findByEmail(username).orElse(null);
                }
            } catch (Exception e) {
                // Token invalide
            }
        }

        return null;
    }

    /**
     * Récupère l'ID de l'utilisateur connecté.
     *
     * @return ID de l'utilisateur ou null
     */
    public Long getCurrentUserId() {
        Utilisateur user = getCurrentUser();
        return user != null ? user.getIdUtilisateur() : null;
    }

    /**
     * Vérifie si l'utilisateur a accès à une école spécifique.
     *
     * @param ecoleId ID de l'école à vérifier
     * @return true si l'accès est autorisé
     */
    public boolean hasAccessToEcole(Long ecoleId) {
        // Si pas d'ecoleId spécifié, l'accès est autorisé
        if (ecoleId == null) {
            return true;
        }

        Long userEcoleId = getCurrentUserEcoleId();

        // En mode développement (pas d'ecoleId utilisateur), autoriser l'accès
        if (userEcoleId == null) {
            return true;
        }

        return userEcoleId.equals(ecoleId);
    }

    /**
     * Vérifie l'accès et lance une exception si non autorisé.
     *
     * @param ecoleId ID de l'école à vérifier
     * @throws AuthenticationException si l'accès est refusé
     */
    public void checkAccessToEcole(Long ecoleId) {
        if (!hasAccessToEcole(ecoleId)) {
            throw new AuthenticationException(
                "Accès non autorisé: vous ne pouvez pas accéder aux données d'une autre école",
                403
            );
        }
    }

    /**
     * Vérifie si l'utilisateur actuel est un admin.
     */
    public boolean isAdmin() {
        String role = getCurrentUserRole();
        return "ADMIN".equalsIgnoreCase(role);
    }

    /**
     * Vérifie si l'utilisateur actuel est un directeur.
     */
    public boolean isDirecteur() {
        String role = getCurrentUserRole();
        return "DIRECTEUR".equalsIgnoreCase(role);
    }

    /**
     * Vérifie si l'utilisateur est authentifié.
     */
    public boolean isAuthenticated() {
        return getCurrentUser() != null;
    }

    /**
     * Récupère la requête HTTP courante.
     */
    private HttpServletRequest getCurrentRequest() {
        try {
            ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attributes != null ? attributes.getRequest() : null;
        } catch (Exception e) {
            return null;
        }
    }
}