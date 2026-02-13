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

import com.digiSchool.digiSchool.academic.organisation.dto.QuartierDto;
import com.digiSchool.digiSchool.academic.organisation.service.QuartierService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/quartiers")
@CrossOrigin
@Tag(name = "Quartiers")
public class QuartierController {

    private final QuartierService quartierService;

    public QuartierController(QuartierService quartierService) {
        this.quartierService = quartierService;
    }

    @Operation(summary = "Creer un quartier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Quartier cree"),
        @ApiResponse(responseCode = "400", description = "Donnees invalides")
    })
    @PostMapping
    public ResponseEntity<QuartierDto> create(@RequestBody QuartierDto dto) {
        return ResponseEntity.ok(quartierService.create(dto));
    }

    @Operation(summary = "Modifier un quartier")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Quartier modifie"),
        @ApiResponse(responseCode = "404", description = "Quartier introuvable")
    })
    @PutMapping("/{id}")
    public ResponseEntity<QuartierDto> update(
            @Parameter(description = "ID du quartier") @PathVariable Long id,
            @RequestBody QuartierDto dto
    ) {
        return ResponseEntity.ok(quartierService.update(id, dto));
    }

    @Operation(summary = "Obtenir un quartier par ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detail du quartier"),
        @ApiResponse(responseCode = "404", description = "Quartier introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<QuartierDto> getById(
            @Parameter(description = "ID du quartier") @PathVariable Long id) {
        return ResponseEntity.ok(quartierService.getById(id));
    }

    @Operation(summary = "Lister tous les quartiers")
    @ApiResponse(responseCode = "200", description = "Liste des quartiers")
    @GetMapping
    public ResponseEntity<List<QuartierDto>> getAll() {
        return ResponseEntity.ok(quartierService.getAll());
    }

    @Operation(summary = "Quartiers d'une ville", description = "Retourne les quartiers appartenant a une ville.")
    @ApiResponse(responseCode = "200", description = "Liste des quartiers de la ville")
    @GetMapping("/ville/{villeId}")
    public ResponseEntity<List<QuartierDto>> getByVilleId(
            @Parameter(description = "ID de la ville") @PathVariable Long villeId) {
        return ResponseEntity.ok(quartierService.getByVilleId(villeId));
    }

    @Operation(summary = "Supprimer un quartier")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Quartier supprime"),
        @ApiResponse(responseCode = "404", description = "Quartier introuvable")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID du quartier") @PathVariable Long id) {
        quartierService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
