package com.digiSchool.digiSchool.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digiSchool.digiSchool.auth.model.RoleType;
import com.digiSchool.digiSchool.auth.security.RequireRole;
import com.digiSchool.digiSchool.user.dto.EleveParentDto;
import com.digiSchool.digiSchool.user.service.EleveParentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/eleve-parents")
@CrossOrigin
@Tag(name = "Relations Eleve-Parent")
public class EleveParentController {

    private final EleveParentService eleveParentService;

    public EleveParentController(EleveParentService eleveParentService) {
        this.eleveParentService = eleveParentService;
    }

    @Operation(summary = "Creer une relation eleve-parent", description = "Associe un parent a un eleve avec un type de relation (pere, mere, tuteur, etc.).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Relation creee"),
        @ApiResponse(responseCode = "400", description = "Donnees invalides")
    })
    @PostMapping
    @RequireRole({ RoleType.ADMIN_ECOLE, RoleType.SECRETAIRE })
    public ResponseEntity<EleveParentDto> create(@RequestBody EleveParentDto dto) {
        return ResponseEntity.ok(eleveParentService.create(dto));
    }

    @Operation(summary = "Modifier une relation eleve-parent")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Relation modifiee"),
        @ApiResponse(responseCode = "404", description = "Relation introuvable")
    })
    @PutMapping("/{id}")
    @RequireRole({ RoleType.ADMIN_ECOLE, RoleType.SECRETAIRE })
    public ResponseEntity<EleveParentDto> update(
            @Parameter(description = "ID de la relation") @PathVariable Long id,
            @RequestBody EleveParentDto dto) {
        return ResponseEntity.ok(eleveParentService.update(id, dto));
    }

    @Operation(summary = "Obtenir une relation par ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detail de la relation"),
        @ApiResponse(responseCode = "404", description = "Relation introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EleveParentDto> getById(
            @Parameter(description = "ID de la relation") @PathVariable Long id) {
        return ResponseEntity.ok(eleveParentService.getById(id));
    }

    @Operation(summary = "Lister toutes les relations")
    @ApiResponse(responseCode = "200", description = "Liste des relations")
    @GetMapping
    public ResponseEntity<List<EleveParentDto>> getAll() {
        return ResponseEntity.ok(eleveParentService.getAll());
    }

    @Operation(summary = "Parents d'un eleve", description = "Retourne tous les parents associes a un eleve.")
    @ApiResponse(responseCode = "200", description = "Liste des parents de l'eleve")
    @GetMapping("/eleve/{eleveId}")
    public ResponseEntity<List<EleveParentDto>> getByEleve(
            @Parameter(description = "ID de l'eleve") @PathVariable Long eleveId) {
        return ResponseEntity.ok(eleveParentService.getByEleve(eleveId));
    }

    @Operation(summary = "Enfants d'un parent", description = "Retourne tous les eleves associes a un parent.")
    @ApiResponse(responseCode = "200", description = "Liste des eleves du parent")
    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<EleveParentDto>> getByParent(
            @Parameter(description = "ID du parent") @PathVariable Long parentId) {
        return ResponseEntity.ok(eleveParentService.getByParent(parentId));
    }

    @Operation(summary = "Contacts d'urgence d'un eleve", description = "Retourne les parents marques comme contacts d'urgence pour cet eleve.")
    @ApiResponse(responseCode = "200", description = "Liste des contacts d'urgence")
    @GetMapping("/eleve/{eleveId}/contacts-urgence")
    public ResponseEntity<List<EleveParentDto>> getContactsUrgence(
            @Parameter(description = "ID de l'eleve") @PathVariable Long eleveId) {
        return ResponseEntity.ok(eleveParentService.getContactsUrgenceByEleve(eleveId));
    }

    @Operation(summary = "Responsables legaux d'un eleve", description = "Retourne les parents marques comme responsables legaux pour cet eleve.")
    @ApiResponse(responseCode = "200", description = "Liste des responsables legaux")
    @GetMapping("/eleve/{eleveId}/responsables-legaux")
    public ResponseEntity<List<EleveParentDto>> getResponsablesLegaux(
            @Parameter(description = "ID de l'eleve") @PathVariable Long eleveId) {
        return ResponseEntity.ok(eleveParentService.getResponsablesLegauxByEleve(eleveId));
    }

    @Operation(summary = "Parent principal d'un eleve", description = "Retourne le parent designe comme contact principal pour cet eleve.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Parent principal"),
        @ApiResponse(responseCode = "404", description = "Aucun parent principal defini")
    })
    @GetMapping("/eleve/{eleveId}/principal")
    public ResponseEntity<EleveParentDto> getPrincipal(
            @Parameter(description = "ID de l'eleve") @PathVariable Long eleveId) {
        EleveParentDto principal = eleveParentService.getPrincipalByEleve(eleveId);
        if (principal == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(principal);
    }

    @Operation(summary = "Supprimer une relation eleve-parent")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Relation supprimee"),
        @ApiResponse(responseCode = "404", description = "Relation introuvable")
    })
    @DeleteMapping("/{id}")
    @RequireRole({ RoleType.ADMIN_ECOLE, RoleType.SECRETAIRE })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la relation") @PathVariable Long id) {
        eleveParentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
