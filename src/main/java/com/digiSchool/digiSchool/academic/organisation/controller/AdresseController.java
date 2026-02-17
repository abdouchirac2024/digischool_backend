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

import com.digiSchool.digiSchool.academic.organisation.dto.AdresseDto;
import com.digiSchool.digiSchool.academic.organisation.service.AdresseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/adresses")
@CrossOrigin
@Tag(name = "Adresses")
public class AdresseController {

    private final AdresseService adresseService;

    public AdresseController(AdresseService adresseService) {
        this.adresseService = adresseService;
    }

    @Operation(summary = "Creer une adresse")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Adresse creee"),
        @ApiResponse(responseCode = "400", description = "Donnees invalides")
    })
    @PostMapping
    public ResponseEntity<AdresseDto> create(@RequestBody AdresseDto dto) {
        return ResponseEntity.ok(adresseService.create(dto));
    }

    @Operation(summary = "Modifier une adresse")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Adresse modifiee"),
        @ApiResponse(responseCode = "404", description = "Adresse introuvable")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AdresseDto> update(
            @Parameter(description = "ID de l'adresse") @PathVariable Long id,
            @RequestBody AdresseDto dto
    ) {
        return ResponseEntity.ok(adresseService.update(id, dto));
    }

    @Operation(summary = "Obtenir une adresse par ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detail de l'adresse"),
        @ApiResponse(responseCode = "404", description = "Adresse introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AdresseDto> getById(
            @Parameter(description = "ID de l'adresse") @PathVariable Long id) {
        return ResponseEntity.ok(adresseService.getById(id));
    }

    @Operation(summary = "Lister toutes les adresses")
    @ApiResponse(responseCode = "200", description = "Liste des adresses")
    @GetMapping
    public ResponseEntity<List<AdresseDto>> getAll() {
        return ResponseEntity.ok(adresseService.getAll());
    }

    @Operation(summary = "Adresses d'un quartier", description = "Retourne les adresses appartenant a un quartier.")
    @ApiResponse(responseCode = "200", description = "Liste des adresses du quartier")
    @GetMapping("/quartier/{quartierId}")
    public ResponseEntity<List<AdresseDto>> getByQuartierId(
            @Parameter(description = "ID du quartier") @PathVariable Long quartierId) {
        return ResponseEntity.ok(adresseService.getByQuartierId(quartierId));
    }

    @Operation(summary = "Supprimer une adresse")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Adresse supprimee"),
        @ApiResponse(responseCode = "404", description = "Adresse introuvable")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de l'adresse") @PathVariable Long id) {
        adresseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
