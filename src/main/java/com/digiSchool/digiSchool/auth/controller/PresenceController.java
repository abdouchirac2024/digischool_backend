package com.digiSchool.digiSchool.auth.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digiSchool.digiSchool.auth.dto.PresenceInfo;
import com.digiSchool.digiSchool.auth.service.PresenceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/presence")
@Tag(name = "Présence", description = "Suivi de présence en temps réel via WebSocket/Redis. Indique les utilisateurs connectés.")
public class PresenceController {

    private final PresenceService presenceService;

    public PresenceController(PresenceService presenceService) {
        this.presenceService = presenceService;
    }

    @Operation(summary = "Utilisateurs en ligne", description = "Retourne la liste complète des utilisateurs actuellement connectés. Réservé aux admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des présences en ligne"),
            @ApiResponse(responseCode = "403", description = "Rôle insuffisant (SUPER_ADMIN ou ADMIN_ECOLE requis)", content = @Content)
    })
    @GetMapping("/online")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_ECOLE')")
    public ResponseEntity<List<PresenceInfo>> getOnlineUsers() {
        return ResponseEntity.ok(presenceService.getAllOnlinePresences());
    }

    @Operation(summary = "Tous les utilisateurs trackés", description = "Retourne tous les utilisateurs tracés : en ligne ET récemment déconnectés (fenêtre 24h). Réservé aux admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste complète des présences (online + offline récents)"),
            @ApiResponse(responseCode = "403", description = "Rôle insuffisant", content = @Content)
    })
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_ECOLE')")
    public ResponseEntity<List<PresenceInfo>> getAllUsers() {
        return ResponseEntity.ok(presenceService.getAllPresences());
    }

    @Operation(summary = "IDs des utilisateurs en ligne", description = "Retourne uniquement les IDs des utilisateurs connectés. Accessible à tous les utilisateurs authentifiés.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ensemble des IDs en ligne"),
            @ApiResponse(responseCode = "401", description = "Non authentifié", content = @Content)
    })
    @GetMapping("/online/ids")
    public ResponseEntity<Set<Long>> getOnlineUserIds() {
        return ResponseEntity.ok(presenceService.getOnlineUserIds());
    }

    @Operation(summary = "Nombre d'utilisateurs en ligne", description = "Retourne le compteur d'utilisateurs actuellement connectés.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Compteur ex: {\"count\": 12}"),
            @ApiResponse(responseCode = "401", description = "Non authentifié", content = @Content)
    })
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getOnlineCount() {
        return ResponseEntity.ok(Map.of("count", presenceService.getOnlineCount()));
    }

    @Operation(summary = "Présence d'un utilisateur", description = "Retourne le statut de présence (en ligne / dernière connexion) d'un utilisateur spécifique.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Informations de présence de l'utilisateur"),
            @ApiResponse(responseCode = "401", description = "Non authentifié", content = @Content),
            @ApiResponse(responseCode = "404", description = "Utilisateur introuvable", content = @Content)
    })
    @GetMapping("/{userId}")
    public ResponseEntity<PresenceInfo> getUserPresence(
            @Parameter(description = "ID de l'utilisateur", required = true, example = "1") @PathVariable Long userId) {
        return ResponseEntity.ok(presenceService.getUserPresence(userId));
    }

    @Operation(summary = "Forcer la déconnexion d'un utilisateur", description = "Déconnecte immédiatement un utilisateur (WebSocket + session Redis invalidés). Réservé aux admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur déconnecté — {\"message\": \"User disconnected successfully\"}"),
            @ApiResponse(responseCode = "403", description = "Rôle insuffisant", content = @Content),
            @ApiResponse(responseCode = "404", description = "Utilisateur introuvable", content = @Content)
    })
    @PostMapping("/force-disconnect/{userId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN_ECOLE')")
    public ResponseEntity<Map<String, String>> forceDisconnect(
            @Parameter(description = "ID de l'utilisateur à déconnecter", required = true, example = "42") @PathVariable Long userId) {
        presenceService.forceDisconnectUser(userId);
        return ResponseEntity.ok(Map.of("message", "User disconnected successfully"));
    }
}