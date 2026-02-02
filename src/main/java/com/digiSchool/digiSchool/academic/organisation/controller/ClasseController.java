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

import com.digiSchool.digiSchool.academic.organisation.dto.ClasseDto;
import com.digiSchool.digiSchool.academic.organisation.service.ClasseService;

@RestController
@RequestMapping("/api/classes")
@CrossOrigin
public class ClasseController {

    private final ClasseService classeService;

    public ClasseController(ClasseService classeService) {
        this.classeService = classeService;
    }

    @PostMapping
    public ResponseEntity<ClasseDto> create(@RequestBody ClasseDto dto) {
        return ResponseEntity.ok(classeService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClasseDto> update(
            @PathVariable Long id,
            @RequestBody ClasseDto dto
    ) {
        return ResponseEntity.ok(classeService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClasseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(classeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ClasseDto>> getAll() {
        return ResponseEntity.ok(classeService.getAll());
    }

    @GetMapping("/ecole/{ecoleId}")
    public ResponseEntity<List<ClasseDto>> getByEcoleId(@PathVariable Long ecoleId) {
        return ResponseEntity.ok(classeService.getByEcoleId(ecoleId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        classeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
