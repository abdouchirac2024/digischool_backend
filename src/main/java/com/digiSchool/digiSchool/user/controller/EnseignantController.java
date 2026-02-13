package com.digiSchool.digiSchool.user.controller;

import com.digiSchool.digiSchool.user.dto.EnseignantDto;
import com.digiSchool.digiSchool.user.model.Enseignant;
import com.digiSchool.digiSchool.user.service.EnseignantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class EnseignantController {

    @Autowired
    private EnseignantService enseignantService;

    @GetMapping
    public ResponseEntity<List<EnseignantDto>> getAllEnseignants() {
        return ResponseEntity.ok(enseignantService.getAllEnseignants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnseignantDto> getEnseignantById(@PathVariable Long id) {
        return ResponseEntity.ok(enseignantService.getEnseignantById(id));
    }

    @PostMapping
    public ResponseEntity<Enseignant> createEnseignant(@RequestBody EnseignantDto dto) {
        return ResponseEntity.ok(enseignantService.createEnseignant(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Enseignant> updateEnseignant(@PathVariable Long id, @RequestBody EnseignantDto dto) {
        return ResponseEntity.ok(enseignantService.updateEnseignant(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnseignant(@PathVariable Long id) {
        enseignantService.deleteEnseignant(id);
        return ResponseEntity.noContent().build();
    }
}
