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

import com.digiSchool.digiSchool.user.dto.EleveDto;
import com.digiSchool.digiSchool.user.service.EleveService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/students")
@CrossOrigin
@Tag(name = "Eleves")
public class EleveController {

    private final EleveService eleveService;

    public EleveController(EleveService eleveService) {
        this.eleveService = eleveService;
    }

    @Operation(summary = "Inscrire un eleve", description = "Cree un nouvel eleve avec un matricule genere automatiquement.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Eleve inscrit"),
        @ApiResponse(responseCode = "400", description = "Donnees invalides")
    })
    @PostMapping
    public ResponseEntity<EleveDto> create(@RequestBody EleveDto dto) {
        return ResponseEntity.ok(eleveService.create(dto));
    }

    @Operation(summary = "Lister tous les eleves", description = "Retourne tous les eleves de l'ecole (filtre par tenant).")
    @ApiResponse(responseCode = "200", description = "Liste des eleves")
    @GetMapping
    public ResponseEntity<List<EleveDto>> getAll() {
        return ResponseEntity.ok(eleveService.getAll());
    }

    @Operation(summary = "Obtenir un eleve par ID", description = "Retourne le detail d'un eleve.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detail de l'eleve"),
        @ApiResponse(responseCode = "404", description = "Eleve introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EleveDto> getById(
            @Parameter(description = "ID de l'eleve") @PathVariable Long id) {
        return ResponseEntity.ok(eleveService.getById(id));
    }

    @Operation(summary = "Obtenir un eleve par matricule", description = "Recherche un eleve par son matricule unique.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Eleve trouve"),
        @ApiResponse(responseCode = "404", description = "Matricule introuvable")
    })
    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<EleveDto> getByMatricule(
            @Parameter(description = "Matricule de l'eleve (ex: ECB-2025-0001)") @PathVariable String matricule) {
        return ResponseEntity.ok(eleveService.getByMatricule(matricule));
    }

    @Operation(summary = "Rechercher des eleves", description = "Recherche par nom, prenom ou matricule.")
    @ApiResponse(responseCode = "200", description = "Resultats de recherche")
    @GetMapping("/search")
    public ResponseEntity<List<EleveDto>> search(
            @Parameter(description = "Terme de recherche") @RequestParam String query) {
        return ResponseEntity.ok(eleveService.search(query));
    }

    @Operation(summary = "Modifier un eleve", description = "Met a jour les informations d'un eleve.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Eleve modifie"),
        @ApiResponse(responseCode = "404", description = "Eleve introuvable")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EleveDto> update(
            @Parameter(description = "ID de l'eleve") @PathVariable Long id,
            @RequestBody EleveDto dto) {
        return ResponseEntity.ok(eleveService.update(id, dto));
    }

    @Operation(summary = "Supprimer un eleve", description = "Supprime definitivement un eleve.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Eleve supprime"),
        @ApiResponse(responseCode = "404", description = "Eleve introuvable")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de l'eleve") @PathVariable Long id) {
        eleveService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
