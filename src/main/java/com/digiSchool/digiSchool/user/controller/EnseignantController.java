package com.digiSchool.digiSchool.user.controller;

import com.digiSchool.digiSchool.user.dto.EnseignantDto;
import com.digiSchool.digiSchool.user.model.Enseignant;
import com.digiSchool.digiSchool.user.service.EnseignantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@Tag(name = "Enseignants")
public class EnseignantController {

    @Autowired
    private EnseignantService enseignantService;

    @Operation(summary = "Lister tous les enseignants", description = "Retourne tous les enseignants de l'ecole.")
    @ApiResponse(responseCode = "200", description = "Liste des enseignants")
    @GetMapping
    public ResponseEntity<List<EnseignantDto>> getAllEnseignants() {
        return ResponseEntity.ok(enseignantService.getAllEnseignants());
    }

    @Operation(summary = "Obtenir un enseignant par ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detail de l'enseignant"),
        @ApiResponse(responseCode = "404", description = "Enseignant introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EnseignantDto> getEnseignantById(
            @Parameter(description = "ID de l'enseignant") @PathVariable Long id) {
        return ResponseEntity.ok(enseignantService.getEnseignantById(id));
    }

    @Operation(summary = "Creer un enseignant", description = "Enregistre un nouvel enseignant dans l'ecole.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Enseignant cree"),
        @ApiResponse(responseCode = "400", description = "Donnees invalides")
    })
    @PostMapping
    public ResponseEntity<Enseignant> createEnseignant(@RequestBody EnseignantDto dto) {
        return ResponseEntity.ok(enseignantService.createEnseignant(dto));
    }

    @Operation(summary = "Modifier un enseignant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Enseignant modifie"),
        @ApiResponse(responseCode = "404", description = "Enseignant introuvable")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Enseignant> updateEnseignant(
            @Parameter(description = "ID de l'enseignant") @PathVariable Long id,
            @RequestBody EnseignantDto dto) {
        return ResponseEntity.ok(enseignantService.updateEnseignant(id, dto));
    }

    @Operation(summary = "Supprimer un enseignant")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Enseignant supprime"),
        @ApiResponse(responseCode = "404", description = "Enseignant introuvable")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnseignant(
            @Parameter(description = "ID de l'enseignant") @PathVariable Long id) {
        enseignantService.deleteEnseignant(id);
        return ResponseEntity.noContent().build();
    }
}
