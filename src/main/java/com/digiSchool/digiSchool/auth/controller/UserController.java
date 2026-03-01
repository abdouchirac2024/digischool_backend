package com.digiSchool.digiSchool.auth.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.digiSchool.digiSchool.auth.dto.AuthResponse.UserInfo;
import com.digiSchool.digiSchool.auth.dto.ChangePasswordRequest;
import com.digiSchool.digiSchool.auth.dto.RegisterRequest;
import com.digiSchool.digiSchool.auth.dto.UpdateProfileRequest;
import com.digiSchool.digiSchool.auth.model.RoleType;
import com.digiSchool.digiSchool.auth.model.User;
import com.digiSchool.digiSchool.auth.model.UserStatus;
import com.digiSchool.digiSchool.auth.security.CurrentUser;
import com.digiSchool.digiSchool.auth.security.RequireRole;
import com.digiSchool.digiSchool.auth.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Utilisateurs")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Créer un utilisateur", description = "Crée un nouveau compte utilisateur.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "403", description = "Rôle insuffisant")
    })
    @PostMapping
    @RequireRole({ RoleType.SUPER_ADMIN, RoleType.ADMIN_ECOLE })
    public ResponseEntity<UserInfo> createUser(
            @Valid @RequestBody RegisterRequest request,
            @Parameter(hidden = true) @CurrentUser User currentUser) {

        if (currentUser.getRole() == RoleType.ADMIN_ECOLE) {
            request.setTenantId(currentUser.getTenantId());
            if (request.getRole() == RoleType.SUPER_ADMIN || request.getRole() == RoleType.ADMIN_ECOLE) {
                return ResponseEntity.badRequest().build();
            }
        }

        User user = userService.createUser(request);
        return ResponseEntity.ok(toUserInfo(user));
    }

    @Operation(summary = "Lister les utilisateurs")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des utilisateurs"),
            @ApiResponse(responseCode = "403", description = "Rôle insuffisant")
    })
    @GetMapping
    @RequireRole({ RoleType.SUPER_ADMIN, RoleType.ADMIN_ECOLE })
    public ResponseEntity<List<UserInfo>> getUsers(
            @RequestParam(required = false) String tenantId,
            @RequestParam(required = false) RoleType role,
            @Parameter(hidden = true) @CurrentUser User currentUser) {

        String effectiveTenantId = tenantId;
        if (currentUser.getRole() == RoleType.ADMIN_ECOLE) {
            effectiveTenantId = currentUser.getTenantId();
        }

        List<User> users;
        if (role != null && effectiveTenantId != null) {
            users = userService.getUsersByTenantAndRole(effectiveTenantId, role);
        } else if (effectiveTenantId != null) {
            users = userService.getUsersByTenant(effectiveTenantId);
        } else if (currentUser.getRole() == RoleType.SUPER_ADMIN) {
            users = userService.getAllUsers();
        } else {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(users.stream().map(this::toUserInfo).collect(Collectors.toList()));
    }

    @Operation(summary = "Statistiques utilisateurs")
    @ApiResponse(responseCode = "200", description = "Statistiques des utilisateurs")
    @GetMapping("/stats")
    @RequireRole({ RoleType.SUPER_ADMIN })
    public ResponseEntity<Map<String, Long>> getUserStats() {
        return ResponseEntity.ok(userService.getUserStats());
    }

    @Operation(summary = "Utilisateurs connectés")
    @ApiResponse(responseCode = "200", description = "Liste des utilisateurs connectés")
    @GetMapping("/connected")
    @RequireRole({ RoleType.SUPER_ADMIN })
    public ResponseEntity<List<UserInfo>> getConnectedUsers() {
        return ResponseEntity.ok(
                userService.getConnectedUsers().stream()
                        .map(this::toUserInfo)
                        .collect(Collectors.toList()));
    }

    @Operation(summary = "Obtenir un utilisateur par ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Détail de l'utilisateur"),
            @ApiResponse(responseCode = "404", description = "Utilisateur introuvable")
    })
    @GetMapping("/{id:\\d+}")
    @RequireRole({ RoleType.SUPER_ADMIN, RoleType.ADMIN_ECOLE })
    public ResponseEntity<UserInfo> getUser(
            @PathVariable Long id,
            @Parameter(hidden = true) @CurrentUser User currentUser) {

        User user = userService.getUserById(id);

        if (currentUser.getRole() == RoleType.ADMIN_ECOLE &&
                !user.getTenantId().equals(currentUser.getTenantId())) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toUserInfo(user));
    }

    @Operation(summary = "Modifier le statut d'un utilisateur")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Statut mis à jour"),
            @ApiResponse(responseCode = "400", description = "Tentative de modification de son propre statut"),
            @ApiResponse(responseCode = "404", description = "Utilisateur introuvable")
    })
    @PutMapping("/{id:\\d+}/status")
    @RequireRole({ RoleType.SUPER_ADMIN, RoleType.ADMIN_ECOLE })
    public ResponseEntity<UserInfo> updateUserStatus(
            @PathVariable Long id,
            @RequestParam UserStatus status,
            @Parameter(hidden = true) @CurrentUser User currentUser) {

        User user = userService.getUserById(id);

        if (currentUser.getRole() == RoleType.ADMIN_ECOLE &&
                !user.getTenantId().equals(currentUser.getTenantId())) {
            return ResponseEntity.notFound().build();
        }

        if (user.getId().equals(currentUser.getId())) {
            throw new com.digiSchool.digiSchool.auth.exception.ForbiddenException(
                "Vous ne pouvez pas modifier votre propre statut"
            );
        }

        return ResponseEntity.ok(toUserInfo(userService.updateUserStatus(id, status)));
    }

    @Operation(summary = "Supprimer un utilisateur")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Utilisateur supprimé"),
            @ApiResponse(responseCode = "400", description = "Tentative de suppression de soi-même"),
            @ApiResponse(responseCode = "404", description = "Utilisateur introuvable")
    })
    @DeleteMapping("/{id:\\d+}")
    @RequireRole({ RoleType.SUPER_ADMIN, RoleType.ADMIN_ECOLE })
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            @Parameter(hidden = true) @CurrentUser User currentUser) {

        User user = userService.getUserById(id);

        if (currentUser.getRole() == RoleType.ADMIN_ECOLE &&
                !user.getTenantId().equals(currentUser.getTenantId())) {
            return ResponseEntity.notFound().build();
        }

        if (user.getId().equals(currentUser.getId())) {
            return ResponseEntity.badRequest().build();
        }

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // =============================================
    // PROFILE & PASSWORD (tous rôles)
    // =============================================

    @Operation(summary = "Obtenir son profil")
    @ApiResponse(responseCode = "200", description = "Profil de l'utilisateur")
    @GetMapping("/profile")
    public ResponseEntity<UserInfo> getProfile(@Parameter(hidden = true) @CurrentUser User currentUser) {
        return ResponseEntity.ok(toUserInfo(currentUser));
    }

    @Operation(summary = "Mettre à jour son profil")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profil mis à jour"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PutMapping("/profile")
    public ResponseEntity<UserInfo> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            @Parameter(hidden = true) @CurrentUser User currentUser) {
        return ResponseEntity.ok(toUserInfo(userService.updateProfile(currentUser.getId(), request)));
    }

    @Operation(summary = "Changer son mot de passe")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mot de passe changé"),
            @ApiResponse(responseCode = "400", description = "Mot de passe actuel incorrect")
    })
    @PutMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @Parameter(hidden = true) @CurrentUser User currentUser) {
        userService.changePassword(currentUser.getId(), request);
        return ResponseEntity.ok(Map.of("message", "Mot de passe modifié avec succès"));
    }

    /**
     * Upload de la photo de profil.
     * Le fichier est stocké dans MinIO (bucket avatars) — URL publique retournée.
     * Formats acceptés : JPG, PNG, GIF, WebP. Taille max : 5 Mo.
     */
    @Operation(summary = "Uploader sa photo de profil", description = "Upload ou remplace la photo de profil. Stockage MinIO centralisé. "
            + "Formats : JPG, PNG, GIF, WebP. Taille max : 5 Mo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Photo uploadée — URL MinIO retournée"),
            @ApiResponse(responseCode = "400", description = "Fichier invalide, trop volumineux ou format non supporté")
    })
    @PostMapping("/avatar")
    public ResponseEntity<UserInfo> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            @Parameter(hidden = true) @CurrentUser User currentUser) {
        User updated = userService.uploadAvatar(currentUser.getId(), file);
        return ResponseEntity.ok(toUserInfo(updated));
    }

    @Operation(summary = "Supprimer sa photo de profil")
    @ApiResponse(responseCode = "200", description = "Photo supprimée")
    @DeleteMapping("/avatar")
    public ResponseEntity<UserInfo> deleteAvatar(@Parameter(hidden = true) @CurrentUser User currentUser) {
        User updated = userService.deleteAvatar(currentUser.getId());
        return ResponseEntity.ok(toUserInfo(updated));
    }

    private UserInfo toUserInfo(User user) {
        return new UserInfo(
                user.getId(),
                user.getEmail(),
                user.getNom(),
                user.getPrenom(),
                user.getTelephone(),
                user.getRole(),
                user.getTenantId(),
                user.getEcole() != null ? user.getEcole().getIdEcole() : null,
                user.getEcole() != null ? user.getEcole().getNom() : null,
                user.getEcole() != null ? user.getEcole().getCodeEcole() : null,
                user.getEcole() != null && user.getEcole().getStatutEcole() != null
                        ? user.getEcole().getStatutEcole().name()
                        : null,
                user.getStatus() != null ? user.getStatus().name() : null,
                user.getLastLogin(),
                user.getCreatedAt(),
                user.getAvatarUrl());
    }
}
