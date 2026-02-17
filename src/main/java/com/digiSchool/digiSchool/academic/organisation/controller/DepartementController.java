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

import com.digiSchool.digiSchool.academic.organisation.dto.DepartementDto;
import com.digiSchool.digiSchool.academic.organisation.service.DepartementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/departements")
@CrossOrigin
@Tag(name = "Departements")
public class DepartementController {

    private final DepartementService departementService;

    public DepartementController(DepartementService departementService) {
        this.departementService = departementService;
    }

    @Operation(summary = "Creer un departement")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Departement cree"),
        @ApiResponse(responseCode = "400", description = "Donnees invalides")
    })
    @PostMapping
    public ResponseEntity<DepartementDto> create(@RequestBody DepartementDto dto) {
        return ResponseEntity.ok(departementService.create(dto));
    }

    @Operation(summary = "Modifier un departement")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Departement modifie"),
        @ApiResponse(responseCode = "404", description = "Departement introuvable")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DepartementDto> update(
            @Parameter(description = "ID du departement") @PathVariable Long id,
            @RequestBody DepartementDto dto) {
        return ResponseEntity.ok(departementService.update(id, dto));
    }

    @Operation(summary = "Obtenir un departement par ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detail du departement"),
        @ApiResponse(responseCode = "404", description = "Departement introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DepartementDto> getById(
            @Parameter(description = "ID du departement") @PathVariable Long id) {
        return ResponseEntity.ok(departementService.getById(id));
    }

    @Operation(summary = "Lister tous les departements")
    @ApiResponse(responseCode = "200", description = "Liste des departements")
    @GetMapping
    public ResponseEntity<List<DepartementDto>> getAll() {
        return ResponseEntity.ok(departementService.getAll());
    }

    @Operation(summary = "Departements d'une region", description = "Retourne les departements appartenant a une region.")
    @ApiResponse(responseCode = "200", description = "Liste des departements de la region")
    @GetMapping("/region/{regionId}")
    public ResponseEntity<List<DepartementDto>> getByRegionId(
            @Parameter(description = "ID de la region") @PathVariable Long regionId) {
        return ResponseEntity.ok(departementService.getByRegionId(regionId));
    }

    @Operation(summary = "Supprimer un departement")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Departement supprime"),
        @ApiResponse(responseCode = "404", description = "Departement introuvable")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID du departement") @PathVariable Long id) {
        departementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
