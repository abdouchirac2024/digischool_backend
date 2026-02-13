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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digiSchool.digiSchool.auth.model.RoleType;
import com.digiSchool.digiSchool.auth.security.RequireRole;
import com.digiSchool.digiSchool.user.dto.ParentDto;
import com.digiSchool.digiSchool.user.service.ParentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/parents")
@CrossOrigin
@Tag(name = "Parents")
public class ParentController {

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @Operation(summary = "Creer un parent", description = "Enregistre un nouveau parent d'eleve. Reserve aux Admin Ecole et Secretaires.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Parent cree"),
        @ApiResponse(responseCode = "400", description = "Donnees invalides"),
        @ApiResponse(responseCode = "403", description = "Role insuffisant")
    })
    @PostMapping
    @RequireRole({RoleType.ADMIN_ECOLE, RoleType.SECRETAIRE})
    public ResponseEntity<ParentDto> create(@RequestBody ParentDto dto) {
        return ResponseEntity.ok(parentService.create(dto));
    }

    @Operation(summary = "Modifier un parent")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Parent modifie"),
        @ApiResponse(responseCode = "404", description = "Parent introuvable")
    })
    @PutMapping("/{id}")
    @RequireRole({RoleType.ADMIN_ECOLE, RoleType.SECRETAIRE})
    public ResponseEntity<ParentDto> update(
            @Parameter(description = "ID du parent") @PathVariable Long id,
            @RequestBody ParentDto dto) {
        return ResponseEntity.ok(parentService.update(id, dto));
    }

    @Operation(summary = "Obtenir un parent par ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detail du parent"),
        @ApiResponse(responseCode = "404", description = "Parent introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ParentDto> getById(
            @Parameter(description = "ID du parent") @PathVariable Long id) {
        return ResponseEntity.ok(parentService.getById(id));
    }

    @Operation(summary = "Obtenir un parent par matricule")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Parent trouve"),
        @ApiResponse(responseCode = "404", description = "Matricule introuvable")
    })
    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<ParentDto> getByMatricule(
            @Parameter(description = "Matricule du parent") @PathVariable String matricule) {
        return ResponseEntity.ok(parentService.getByMatricule(matricule));
    }

    @Operation(summary = "Lister tous les parents")
    @ApiResponse(responseCode = "200", description = "Liste des parents")
    @GetMapping
    public ResponseEntity<List<ParentDto>> getAll() {
        return ResponseEntity.ok(parentService.getAll());
    }

    @Operation(summary = "Rechercher des parents", description = "Recherche par nom, prenom ou matricule.")
    @ApiResponse(responseCode = "200", description = "Resultats de recherche")
    @GetMapping("/search")
    public ResponseEntity<List<ParentDto>> search(
            @Parameter(description = "Terme de recherche") @RequestParam String query) {
        return ResponseEntity.ok(parentService.search(query));
    }

    @Operation(summary = "Supprimer un parent", description = "Reserve aux Admin Ecole.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Parent supprime"),
        @ApiResponse(responseCode = "404", description = "Parent introuvable")
    })
    @DeleteMapping("/{id}")
    @RequireRole(RoleType.ADMIN_ECOLE)
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID du parent") @PathVariable Long id) {
        parentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
