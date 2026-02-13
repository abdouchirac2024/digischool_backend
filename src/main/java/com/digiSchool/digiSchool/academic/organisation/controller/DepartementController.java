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

@RestController
@RequestMapping("/api/departements")
@CrossOrigin
public class DepartementController {

    private final DepartementService departementService;

    public DepartementController(DepartementService departementService) {
        this.departementService = departementService;
    }

    @PostMapping
    public ResponseEntity<DepartementDto> create(@RequestBody DepartementDto dto) {
        return ResponseEntity.ok(departementService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartementDto> update(
            @PathVariable Long id,
            @RequestBody DepartementDto dto) {
        return ResponseEntity.ok(departementService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartementDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(departementService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<DepartementDto>> getAll() {
        return ResponseEntity.ok(departementService.getAll());
    }

    @GetMapping("/region/{regionId}")
    public ResponseEntity<List<DepartementDto>> getByRegionId(@PathVariable Long regionId) {
        return ResponseEntity.ok(departementService.getByRegionId(regionId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        departementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
