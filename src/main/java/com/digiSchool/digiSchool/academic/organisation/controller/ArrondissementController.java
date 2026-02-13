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

@RestController
@RequestMapping("/api/arrondissements")
@CrossOrigin
public class ArrondissementController {

    private final ArrondissementService arrondissementService;

    public ArrondissementController(ArrondissementService arrondissementService) {
        this.arrondissementService = arrondissementService;
    }

    @PostMapping
    public ResponseEntity<ArrondissementDto> create(@RequestBody ArrondissementDto dto) {
        return ResponseEntity.ok(arrondissementService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArrondissementDto> update(
            @PathVariable Long id,
            @RequestBody ArrondissementDto dto
    ) {
        return ResponseEntity.ok(arrondissementService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArrondissementDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(arrondissementService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ArrondissementDto>> getAll() {
        return ResponseEntity.ok(arrondissementService.getAll());
    }

    @GetMapping("/departement/{departementId}")
    public ResponseEntity<List<ArrondissementDto>> getByDepartementId(@PathVariable Long departementId) {
        return ResponseEntity.ok(arrondissementService.getByDepartementId(departementId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        arrondissementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
