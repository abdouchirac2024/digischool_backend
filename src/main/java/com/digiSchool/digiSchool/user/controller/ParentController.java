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

import com.digiSchool.digiSchool.auth.model.RoleType;
import com.digiSchool.digiSchool.auth.security.RequireRole;
import com.digiSchool.digiSchool.user.dto.ParentDto;
import com.digiSchool.digiSchool.user.service.ParentService;

@RestController
@RequestMapping("/api/parents")
@CrossOrigin
public class ParentController {

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @PostMapping
    @RequireRole({RoleType.ADMIN_ECOLE, RoleType.SECRETAIRE})
    public ResponseEntity<ParentDto> create(@RequestBody ParentDto dto) {
        return ResponseEntity.ok(parentService.create(dto));
    }

    @PutMapping("/{id}")
    @RequireRole({RoleType.ADMIN_ECOLE, RoleType.SECRETAIRE})
    public ResponseEntity<ParentDto> update(
            @PathVariable Long id,
            @RequestBody ParentDto dto) {
        return ResponseEntity.ok(parentService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParentDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(parentService.getById(id));
    }

    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<ParentDto> getByMatricule(@PathVariable String matricule) {
        return ResponseEntity.ok(parentService.getByMatricule(matricule));
    }

    @GetMapping
    public ResponseEntity<List<ParentDto>> getAll() {
        return ResponseEntity.ok(parentService.getAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ParentDto>> search(@RequestParam String query) {
        return ResponseEntity.ok(parentService.search(query));
    }

    @DeleteMapping("/{id}")
    @RequireRole(RoleType.ADMIN_ECOLE)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        parentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
