package com.digiSchool.digiSchool.academic.organisation.controller;

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

import com.digiSchool.digiSchool.academic.organisation.dto.ArrondissementDto;
import com.digiSchool.digiSchool.academic.organisation.service.ArrondissementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/arrondissements")
@CrossOrigin
@Tag(name = "Arrondissements")
public class ArrondissementController {

    private final ArrondissementService arrondissementService;

    public ArrondissementController(ArrondissementService arrondissementService) {
        this.arrondissementService = arrondissementService;
    }

    @Operation(summary = "Creer un arrondissement")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Arrondissement cree"),
        @ApiResponse(responseCode = "400", description = "Donnees invalides")
    })
    @PostMapping
    public ResponseEntity<ArrondissementDto> create(@RequestBody ArrondissementDto dto) {
        return ResponseEntity.ok(arrondissementService.create(dto));
    }

    @Operation(summary = "Modifier un arrondissement")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Arrondissement modifie"),
        @ApiResponse(responseCode = "404", description = "Arrondissement introuvable")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ArrondissementDto> update(
            @Parameter(description = "ID de l'arrondissement") @PathVariable Long id,
            @RequestBody ArrondissementDto dto
    ) {
        return ResponseEntity.ok(arrondissementService.update(id, dto));
    }

    @Operation(summary = "Obtenir un arrondissement par ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detail de l'arrondissement"),
        @ApiResponse(responseCode = "404", description = "Arrondissement introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ArrondissementDto> getById(
            @Parameter(description = "ID de l'arrondissement") @PathVariable Long id) {
        return ResponseEntity.ok(arrondissementService.getById(id));
    }

    @Operation(summary = "Lister tous les arrondissements")
    @ApiResponse(responseCode = "200", description = "Liste des arrondissements")
    @GetMapping
    public ResponseEntity<List<ArrondissementDto>> getAll() {
        return ResponseEntity.ok(arrondissementService.getAll());
    }

    @Operation(summary = "Arrondissements d'un departement", description = "Retourne les arrondissements appartenant a un departement.")
    @ApiResponse(responseCode = "200", description = "Liste des arrondissements du departement")
    @GetMapping("/departement/{departementId}")
    public ResponseEntity<List<ArrondissementDto>> getByDepartementId(
            @Parameter(description = "ID du departement") @PathVariable Long departementId) {
        return ResponseEntity.ok(arrondissementService.getByDepartementId(departementId));
    }

    @Operation(summary = "Supprimer un arrondissement")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Arrondissement supprime"),
        @ApiResponse(responseCode = "404", description = "Arrondissement introuvable")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de l'arrondissement") @PathVariable Long id) {
        arrondissementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
