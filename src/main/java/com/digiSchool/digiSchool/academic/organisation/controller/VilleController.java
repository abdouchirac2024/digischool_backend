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

@RestController
@RequestMapping("/api/villes")
@CrossOrigin
public class VilleController {

    private final VilleService villeService;

    public VilleController(VilleService villeService) {
        this.villeService = villeService;
    }

    @PostMapping
    public ResponseEntity<VilleDto> create(@RequestBody VilleDto dto) {
        return ResponseEntity.ok(villeService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VilleDto> update(
            @PathVariable Long id,
            @RequestBody VilleDto dto
    ) {
        return ResponseEntity.ok(villeService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VilleDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(villeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<VilleDto>> getAll() {
        return ResponseEntity.ok(villeService.getAll());
    }

    @GetMapping("/arrondissement/{arrondissementId}")
    public ResponseEntity<List<VilleDto>> getByArrondissementId(@PathVariable Long arrondissementId) {
        return ResponseEntity.ok(villeService.getByArrondissementId(arrondissementId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        villeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
