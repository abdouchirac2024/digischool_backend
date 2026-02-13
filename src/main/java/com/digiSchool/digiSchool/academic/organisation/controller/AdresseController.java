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

@RestController
@RequestMapping("/api/adresses")
@CrossOrigin
public class AdresseController {

    private final AdresseService adresseService;

    public AdresseController(AdresseService adresseService) {
        this.adresseService = adresseService;
    }

    @PostMapping
    public ResponseEntity<AdresseDto> create(@RequestBody AdresseDto dto) {
        return ResponseEntity.ok(adresseService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdresseDto> update(
            @PathVariable Long id,
            @RequestBody AdresseDto dto
    ) {
        return ResponseEntity.ok(adresseService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdresseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(adresseService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<AdresseDto>> getAll() {
        return ResponseEntity.ok(adresseService.getAll());
    }

    @GetMapping("/quartier/{quartierId}")
    public ResponseEntity<List<AdresseDto>> getByQuartierId(@PathVariable Long quartierId) {
        return ResponseEntity.ok(adresseService.getByQuartierId(quartierId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adresseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
