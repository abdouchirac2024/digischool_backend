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

@RestController
@RequestMapping("/api/quartiers")
@CrossOrigin
public class QuartierController {

    private final QuartierService quartierService;

    public QuartierController(QuartierService quartierService) {
        this.quartierService = quartierService;
    }

    @PostMapping
    public ResponseEntity<QuartierDto> create(@RequestBody QuartierDto dto) {
        return ResponseEntity.ok(quartierService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuartierDto> update(
            @PathVariable Long id,
            @RequestBody QuartierDto dto
    ) {
        return ResponseEntity.ok(quartierService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuartierDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(quartierService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<QuartierDto>> getAll() {
        return ResponseEntity.ok(quartierService.getAll());
    }

    @GetMapping("/ville/{villeId}")
    public ResponseEntity<List<QuartierDto>> getByVilleId(@PathVariable Long villeId) {
        return ResponseEntity.ok(quartierService.getByVilleId(villeId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        quartierService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
