package com.digiSchool.digiSchool.notification.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.digiSchool.digiSchool.auth.service.UserContextService;
import com.digiSchool.digiSchool.notification.dto.NotificationDto;
import com.digiSchool.digiSchool.notification.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin
@Tag(name = "Notifications", description = "Gestion des notifications in-app de l'utilisateur connecté (lecture, comptage, marquage).")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserContextService userContextService;

    public NotificationController(NotificationService notificationService, UserContextService userContextService) {
        this.notificationService = notificationService;
        this.userContextService = userContextService;
    }

    @Operation(summary = "Lister mes notifications", description = "Retourne toutes les notifications (lues et non lues) de l'utilisateur connecté.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste des notifications"),
        @ApiResponse(responseCode = "401", description = "Non authentifié", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<NotificationDto>> getAll() {
        Long userId = userContextService.getCurrentUserId();
        return ResponseEntity.ok(notificationService.getNotifications(userId));
    }

    @Operation(summary = "Notifications non lues", description = "Retourne uniquement les notifications non lues de l'utilisateur connecté.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste des notifications non lues"),
        @ApiResponse(responseCode = "401", description = "Non authentifié", content = @Content)
    })
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDto>> getUnread() {
        Long userId = userContextService.getCurrentUserId();
        return ResponseEntity.ok(notificationService.getNotificationsNonLues(userId));
    }

    @Operation(summary = "Compter les non lues", description = "Retourne le nombre de notifications non lues. Utile pour afficher un badge dans l'interface.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Compteur ex: {\"count\": 5}"),
        @ApiResponse(responseCode = "401", description = "Non authentifié", content = @Content)
    })
    @GetMapping("/unread/count")
    public ResponseEntity<Map<String, Long>> countUnread() {
        Long userId = userContextService.getCurrentUserId();
        long count = notificationService.compterNonLues(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @Operation(summary = "Marquer une notification comme lue", description = "Marque une notification spécifique comme lue par son ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notification marquée comme lue"),
        @ApiResponse(responseCode = "401", description = "Non authentifié", content = @Content),
        @ApiResponse(responseCode = "404", description = "Notification introuvable", content = @Content)
    })
    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(
            @Parameter(description = "ID de la notification", required = true, example = "1")
            @PathVariable Long id) {
        notificationService.marquerCommeLue(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Marquer toutes comme lues", description = "Marque toutes les notifications de l'utilisateur connecté comme lues.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Toutes les notifications marquées comme lues"),
        @ApiResponse(responseCode = "401", description = "Non authentifié", content = @Content)
    })
    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead() {
        Long userId = userContextService.getCurrentUserId();
        notificationService.marquerToutesCommeLues(userId);
        return ResponseEntity.ok().build();
    }
}