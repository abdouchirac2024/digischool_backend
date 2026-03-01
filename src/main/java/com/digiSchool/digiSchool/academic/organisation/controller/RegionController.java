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

import com.digiSchool.digiSchool.academic.organisation.dto.RegionDto;
import com.digiSchool.digiSchool.academic.organisation.service.RegionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/regions")
@CrossOrigin
@Tag(name = "Regions")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @Operation(summary = "Creer une region")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Region creee"),
        @ApiResponse(responseCode = "400", description = "Donnees invalides")
    })
    @PostMapping
    public ResponseEntity<RegionDto> create(@RequestBody RegionDto dto) {
        return ResponseEntity.ok(regionService.create(dto));
    }

    @Operation(summary = "Modifier une region")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Region modifiee"),
        @ApiResponse(responseCode = "404", description = "Region introuvable")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RegionDto> update(
            @Parameter(description = "ID de la region") @PathVariable Long id,
            @RequestBody RegionDto dto
    ) {
        return ResponseEntity.ok(regionService.update(id, dto));
    }

    @Operation(summary = "Obtenir une region par ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detail de la region"),
        @ApiResponse(responseCode = "404", description = "Region introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RegionDto> getById(
            @Parameter(description = "ID de la region") @PathVariable Long id) {
        return ResponseEntity.ok(regionService.getById(id));
    }

    @Operation(summary = "Lister toutes les regions", description = "Retourne les 10 regions du Cameroun.")
    @ApiResponse(responseCode = "200", description = "Liste des regions")
    @GetMapping
    public ResponseEntity<List<RegionDto>> getAll() {
        return ResponseEntity.ok(regionService.getAll());
    }

    @Operation(summary = "Supprimer une region")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Region supprimee"),
        @ApiResponse(responseCode = "404", description = "Region introuvable")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la region") @PathVariable Long id) {
        regionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
