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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @RequireRole({RoleType.SUPER_ADMIN, RoleType.ADMIN_ECOLE})
    public ResponseEntity<UserInfo> createUser(
            @Valid @RequestBody RegisterRequest request,
            @CurrentUser User currentUser) {

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

    @GetMapping
    @RequireRole({RoleType.SUPER_ADMIN, RoleType.ADMIN_ECOLE})
    public ResponseEntity<List<UserInfo>> getUsers(
            @RequestParam(required = false) String tenantId,
            @RequestParam(required = false) RoleType role,
            @CurrentUser User currentUser) {

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

    @GetMapping("/{id}")
    @RequireRole({RoleType.SUPER_ADMIN, RoleType.ADMIN_ECOLE})
    public ResponseEntity<UserInfo> getUser(
            @PathVariable Long id,
            @CurrentUser User currentUser) {

        User user = userService.getUserById(id);

        // Les ADMIN_ECOLE ne peuvent voir que les utilisateurs de leur tenant
        if (currentUser.getRole() == RoleType.ADMIN_ECOLE &&
            !user.getTenantId().equals(currentUser.getTenantId())) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toUserInfo(user));
    }

    @PutMapping("/{id}/status")
    @RequireRole({RoleType.SUPER_ADMIN, RoleType.ADMIN_ECOLE})
    public ResponseEntity<UserInfo> updateUserStatus(
            @PathVariable Long id,
            @RequestParam UserStatus status,
            @CurrentUser User currentUser) {

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

    @DeleteMapping("/{id}")
    @RequireRole({RoleType.SUPER_ADMIN, RoleType.ADMIN_ECOLE})
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            @CurrentUser User currentUser) {

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
            user.getRole(),
            user.getTenantId()
        );
    }
}
