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

import com.digiSchool.digiSchool.academic.organisation.dto.VilleDto;
import com.digiSchool.digiSchool.academic.organisation.service.VilleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/villes")
@CrossOrigin
@Tag(name = "Villes")
public class VilleController {

    private final VilleService villeService;

    public VilleController(VilleService villeService) {
        this.villeService = villeService;
    }

    @Operation(summary = "Creer une ville")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ville creee"),
        @ApiResponse(responseCode = "400", description = "Donnees invalides")
    })
    @PostMapping
    public ResponseEntity<VilleDto> create(@RequestBody VilleDto dto) {
        return ResponseEntity.ok(villeService.create(dto));
    }

    @Operation(summary = "Modifier une ville")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ville modifiee"),
        @ApiResponse(responseCode = "404", description = "Ville introuvable")
    })
    @PutMapping("/{id}")
    public ResponseEntity<VilleDto> update(
            @Parameter(description = "ID de la ville") @PathVariable Long id,
            @RequestBody VilleDto dto
    ) {
        return ResponseEntity.ok(villeService.update(id, dto));
    }

    @Operation(summary = "Obtenir une ville par ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detail de la ville"),
        @ApiResponse(responseCode = "404", description = "Ville introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VilleDto> getById(
            @Parameter(description = "ID de la ville") @PathVariable Long id) {
        return ResponseEntity.ok(villeService.getById(id));
    }

    @Operation(summary = "Lister toutes les villes")
    @ApiResponse(responseCode = "200", description = "Liste des villes")
    @GetMapping
    public ResponseEntity<List<VilleDto>> getAll() {
        return ResponseEntity.ok(villeService.getAll());
    }

    @Operation(summary = "Villes d'un arrondissement", description = "Retourne les villes appartenant a un arrondissement.")
    @ApiResponse(responseCode = "200", description = "Liste des villes de l'arrondissement")
    @GetMapping("/arrondissement/{arrondissementId}")
    public ResponseEntity<List<VilleDto>> getByArrondissementId(
            @Parameter(description = "ID de l'arrondissement") @PathVariable Long arrondissementId) {
        return ResponseEntity.ok(villeService.getByArrondissementId(arrondissementId));
    }

    @Operation(summary = "Supprimer une ville")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Ville supprimee"),
        @ApiResponse(responseCode = "404", description = "Ville introuvable")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la ville") @PathVariable Long id) {
        villeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
