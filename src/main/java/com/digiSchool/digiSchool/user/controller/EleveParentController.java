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
import org.springframework.web.bind.annotation.RestController;

import com.digiSchool.digiSchool.auth.model.RoleType;
import com.digiSchool.digiSchool.auth.security.RequireRole;
import com.digiSchool.digiSchool.user.dto.EleveParentDto;
import com.digiSchool.digiSchool.user.service.EleveParentService;

@RestController
@RequestMapping("/api/eleve-parents")
@CrossOrigin
public class EleveParentController {

    private final EleveParentService eleveParentService;

    public EleveParentController(EleveParentService eleveParentService) {
        this.eleveParentService = eleveParentService;
    }

    @PostMapping
    @RequireRole({ RoleType.ADMIN_ECOLE, RoleType.SECRETAIRE })
    public ResponseEntity<EleveParentDto> create(@RequestBody EleveParentDto dto) {
        return ResponseEntity.ok(eleveParentService.create(dto));
    }

    @PutMapping("/{id}")
    @RequireRole({ RoleType.ADMIN_ECOLE, RoleType.SECRETAIRE })
    public ResponseEntity<EleveParentDto> update(
            @PathVariable Long id,
            @RequestBody EleveParentDto dto) {
        return ResponseEntity.ok(eleveParentService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EleveParentDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(eleveParentService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<EleveParentDto>> getAll() {
        return ResponseEntity.ok(eleveParentService.getAll());
    }

    @GetMapping("/eleve/{eleveId}")
    public ResponseEntity<List<EleveParentDto>> getByEleve(@PathVariable Long eleveId) {
        return ResponseEntity.ok(eleveParentService.getByEleve(eleveId));
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<EleveParentDto>> getByParent(@PathVariable Long parentId) {
        return ResponseEntity.ok(eleveParentService.getByParent(parentId));
    }

    @GetMapping("/eleve/{eleveId}/contacts-urgence")
    public ResponseEntity<List<EleveParentDto>> getContactsUrgence(@PathVariable Long eleveId) {
        return ResponseEntity.ok(eleveParentService.getContactsUrgenceByEleve(eleveId));
    }

    @GetMapping("/eleve/{eleveId}/responsables-legaux")
    public ResponseEntity<List<EleveParentDto>> getResponsablesLegaux(@PathVariable Long eleveId) {
        return ResponseEntity.ok(eleveParentService.getResponsablesLegauxByEleve(eleveId));
    }

    @GetMapping("/eleve/{eleveId}/principal")
    public ResponseEntity<EleveParentDto> getPrincipal(@PathVariable Long eleveId) {
        EleveParentDto principal = eleveParentService.getPrincipalByEleve(eleveId);
        if (principal == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(principal);
    }

    @DeleteMapping("/{id}")
    @RequireRole({ RoleType.ADMIN_ECOLE, RoleType.SECRETAIRE })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eleveParentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
