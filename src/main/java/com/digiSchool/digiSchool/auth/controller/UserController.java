package com.digiSchool.digiSchool.auth.controller;

import java.util.List;
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

import com.digiSchool.digiSchool.auth.dto.AuthResponse.UserInfo;
import com.digiSchool.digiSchool.auth.dto.RegisterRequest;
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

    @Operation(summary = "Creer un utilisateur", description = "Cree un nouveau compte utilisateur. Les Admin Ecole ne peuvent creer que des utilisateurs pour leur propre tenant.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Utilisateur cree"),
        @ApiResponse(responseCode = "400", description = "Donnees invalides ou role non autorise"),
        @ApiResponse(responseCode = "403", description = "Role insuffisant")
    })
    @PostMapping
    @RequireRole({RoleType.SUPER_ADMIN, RoleType.ADMIN_ECOLE})
    public ResponseEntity<UserInfo> createUser(
            @Valid @RequestBody RegisterRequest request,
            @Parameter(hidden = true) @CurrentUser User currentUser) {

        // Les ADMIN_ECOLE ne peuvent créer que des utilisateurs pour leur tenant
        if (currentUser.getRole() == RoleType.ADMIN_ECOLE) {
            request.setTenantId(currentUser.getTenantId());
            // Limiter les rôles que l'admin peut créer
            if (request.getRole() == RoleType.SUPER_ADMIN || request.getRole() == RoleType.ADMIN_ECOLE) {
                return ResponseEntity.badRequest().build();
            }
        }

        User user = userService.createUser(request);
        return ResponseEntity.ok(toUserInfo(user));
    }

    @Operation(summary = "Lister les utilisateurs", description = "Retourne les utilisateurs filtres par tenant et/ou role. Les Admin Ecole ne voient que leur propre tenant.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste des utilisateurs"),
        @ApiResponse(responseCode = "400", description = "Parametres manquants"),
        @ApiResponse(responseCode = "403", description = "Role insuffisant")
    })
    @GetMapping
    @RequireRole({RoleType.SUPER_ADMIN, RoleType.ADMIN_ECOLE})
    public ResponseEntity<List<UserInfo>> getUsers(
            @Parameter(description = "Filtrer par tenant ID") @RequestParam(required = false) String tenantId,
            @Parameter(description = "Filtrer par role") @RequestParam(required = false) RoleType role,
            @Parameter(hidden = true) @CurrentUser User currentUser) {

        String effectiveTenantId = tenantId;

        // Les ADMIN_ECOLE ne peuvent voir que les utilisateurs de leur tenant
        if (currentUser.getRole() == RoleType.ADMIN_ECOLE) {
            effectiveTenantId = currentUser.getTenantId();
        }

        List<User> users;
        if (role != null && effectiveTenantId != null) {
            users = userService.getUsersByTenantAndRole(effectiveTenantId, role);
        } else if (effectiveTenantId != null) {
            users = userService.getUsersByTenant(effectiveTenantId);
        } else {
            return ResponseEntity.badRequest().build();
        }

        List<UserInfo> userInfos = users.stream()
                .map(this::toUserInfo)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userInfos);
    }

    @Operation(summary = "Obtenir un utilisateur par ID", description = "Retourne le detail d'un utilisateur. Les Admin Ecole ne voient que les utilisateurs de leur tenant.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detail de l'utilisateur"),
        @ApiResponse(responseCode = "404", description = "Utilisateur introuvable ou hors tenant")
    })
    @GetMapping("/{id}")
    @RequireRole({RoleType.SUPER_ADMIN, RoleType.ADMIN_ECOLE})
    public ResponseEntity<UserInfo> getUser(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long id,
            @Parameter(hidden = true) @CurrentUser User currentUser) {

        User user = userService.getUserById(id);

        // Les ADMIN_ECOLE ne peuvent voir que les utilisateurs de leur tenant
        if (currentUser.getRole() == RoleType.ADMIN_ECOLE &&
            !user.getTenantId().equals(currentUser.getTenantId())) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toUserInfo(user));
    }

    @Operation(summary = "Modifier le statut d'un utilisateur", description = "Active, desactive ou suspend un compte utilisateur. Un admin ne peut pas modifier son propre statut.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Statut mis a jour"),
        @ApiResponse(responseCode = "400", description = "Tentative de modification de son propre statut"),
        @ApiResponse(responseCode = "404", description = "Utilisateur introuvable ou hors tenant")
    })
    @PutMapping("/{id}/status")
    @RequireRole({RoleType.SUPER_ADMIN, RoleType.ADMIN_ECOLE})
    public ResponseEntity<UserInfo> updateUserStatus(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long id,
            @Parameter(description = "Nouveau statut") @RequestParam UserStatus status,
            @Parameter(hidden = true) @CurrentUser User currentUser) {

        User user = userService.getUserById(id);

        // Les ADMIN_ECOLE ne peuvent modifier que les utilisateurs de leur tenant
        if (currentUser.getRole() == RoleType.ADMIN_ECOLE &&
            !user.getTenantId().equals(currentUser.getTenantId())) {
            return ResponseEntity.notFound().build();
        }

        // Un admin ne peut pas se désactiver lui-même
        if (user.getId().equals(currentUser.getId())) {
            return ResponseEntity.badRequest().build();
        }

        User updated = userService.updateUserStatus(id, status);
        return ResponseEntity.ok(toUserInfo(updated));
    }

    @Operation(summary = "Supprimer un utilisateur", description = "Supprime definitivement un compte utilisateur. Un admin ne peut pas se supprimer lui-meme.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Utilisateur supprime"),
        @ApiResponse(responseCode = "400", description = "Tentative de suppression de soi-meme"),
        @ApiResponse(responseCode = "404", description = "Utilisateur introuvable ou hors tenant")
    })
    @DeleteMapping("/{id}")
    @RequireRole({RoleType.SUPER_ADMIN, RoleType.ADMIN_ECOLE})
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long id,
            @Parameter(hidden = true) @CurrentUser User currentUser) {

        User user = userService.getUserById(id);

        // Les ADMIN_ECOLE ne peuvent supprimer que les utilisateurs de leur tenant
        if (currentUser.getRole() == RoleType.ADMIN_ECOLE &&
            !user.getTenantId().equals(currentUser.getTenantId())) {
            return ResponseEntity.notFound().build();
        }

        // Un admin ne peut pas se supprimer lui-même
        if (user.getId().equals(currentUser.getId())) {
            return ResponseEntity.badRequest().build();
        }

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
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
            user.getEcole() != null ? user.getEcole().getCodeEcole() : null
        );
    }
}
