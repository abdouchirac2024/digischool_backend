package com.digiSchool.digiSchool.academic.organisation.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digiSchool.digiSchool.academic.organisation.dto.AnnulationRequest;
import com.digiSchool.digiSchool.academic.organisation.dto.InscriptionCreateRequest;
import com.digiSchool.digiSchool.academic.organisation.dto.InscriptionDto;
import com.digiSchool.digiSchool.academic.organisation.service.InscriptionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inscriptions")
@CrossOrigin
@Tag(name = "Inscriptions")
public class InscriptionController {

    private final InscriptionService inscriptionService;

    public InscriptionController(InscriptionService inscriptionService) {
        this.inscriptionService = inscriptionService;
    }

    @Operation(summary = "Creer une inscription")
    @PostMapping
    public ResponseEntity<InscriptionDto> creerInscription(@Valid @RequestBody InscriptionCreateRequest request) {
        return ResponseEntity.ok(inscriptionService.creerInscription(request));
    }

    @Operation(summary = "Lister toutes les inscriptions")
    @GetMapping
    public ResponseEntity<List<InscriptionDto>> getAll() {
        return ResponseEntity.ok(inscriptionService.getAll());
    }

    @Operation(summary = "Obtenir une inscription par ID")
    @GetMapping("/{id}")
    public ResponseEntity<InscriptionDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(inscriptionService.getById(id));
    }

    @Operation(summary = "Annuler une inscription")
    @PutMapping("/{id}/annuler")
    public ResponseEntity<InscriptionDto> annulerInscription(
            @PathVariable Long id,
            @Valid @RequestBody AnnulationRequest request) {
        return ResponseEntity.ok(inscriptionService.annulerInscription(id, request));
    }

    @Operation(summary = "Verifier si un eleve a des parents")
    @GetMapping("/eleve/{eleveId}/has-parents")
    public ResponseEntity<Map<String, Boolean>> eleveADesParents(@PathVariable Long eleveId) {
        boolean hasParents = inscriptionService.eleveADesParents(eleveId);
        return ResponseEntity.ok(Map.of("hasParents", hasParents));
    }
}
