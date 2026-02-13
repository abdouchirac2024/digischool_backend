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

@RestController
@RequestMapping("/api/students")
@CrossOrigin
public class EleveController {

    private final EleveService eleveService;

    public EleveController(EleveService eleveService) {
        this.eleveService = eleveService;
    }

    @PostMapping
    public ResponseEntity<EleveDto> create(@RequestBody EleveDto dto) {
        return ResponseEntity.ok(eleveService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<EleveDto>> getAll() {
        return ResponseEntity.ok(eleveService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EleveDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(eleveService.getById(id));
    }

    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<EleveDto> getByMatricule(@PathVariable String matricule) {
        return ResponseEntity.ok(eleveService.getByMatricule(matricule));
    }

    @GetMapping("/search")
    public ResponseEntity<List<EleveDto>> search(@RequestParam String query) {
        return ResponseEntity.ok(eleveService.search(query));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EleveDto> update(@PathVariable Long id, @RequestBody EleveDto dto) {
        return ResponseEntity.ok(eleveService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eleveService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
