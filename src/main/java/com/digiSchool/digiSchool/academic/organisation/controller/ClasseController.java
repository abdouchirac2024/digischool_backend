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
import com.digiSchool.digiSchool.auth.service.UserContextService;

/**
 * Contrôleur REST pour la gestion des classes.
 * L'ecoleId est automatiquement déterminé depuis le JWT token.
 * L'utilisateur ne peut créer/modifier/supprimer que les classes de SA propre école.
 */
@RestController
@RequestMapping("/api/classes")
@CrossOrigin
public class ClasseController {

    private final ClasseService classeService;
    private final UserContextService userContextService;

    public ClasseController(ClasseService classeService, UserContextService userContextService) {
        this.classeService = classeService;
        this.userContextService = userContextService;
    }

    /**
     * Créer une nouvelle classe.
     * L'ecoleId est automatiquement défini depuis le contexte utilisateur.
     * Si ecoleId est fourni dans le DTO, il est ignoré et remplacé par celui de l'utilisateur.
     */
    @PostMapping
    public ResponseEntity<ClasseDto> create(@RequestBody ClasseDto dto) {
        Long userEcoleId = userContextService.getCurrentUserEcoleId();
        return ResponseEntity.ok(classeService.create(dto, userEcoleId));
    }

    /**
     * Modifier une classe existante.
     * Vérifie que la classe appartient à l'école de l'utilisateur.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClasseDto> update(
            @PathVariable Long id,
            @RequestBody ClasseDto dto
    ) {
        Long userEcoleId = userContextService.getCurrentUserEcoleId();
        return ResponseEntity.ok(classeService.update(id, dto, userEcoleId));
    }

    /**
     * Récupérer une classe par ID.
     * Vérifie que la classe appartient à l'école de l'utilisateur.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClasseDto> getById(@PathVariable Long id) {
        ClasseDto classe = classeService.getById(id);
        // Vérifier l'accès
        userContextService.checkAccessToEcole(classe.getEcoleId());
        return ResponseEntity.ok(classe);
    }

    /**
     * Récupérer toutes les classes de l'école de l'utilisateur.
     * Si pas d'ecoleId dans le contexte (mode dev), retourne toutes les classes.
     */
    @GetMapping
    public ResponseEntity<List<ClasseDto>> getAll() {
        Long userEcoleId = userContextService.getCurrentUserEcoleId();
        if (userEcoleId != null) {
            return ResponseEntity.ok(classeService.getByEcoleId(userEcoleId));
        }
        return ResponseEntity.ok(classeService.getAll());
    }

    /**
     * Récupérer les classes d'une école spécifique.
     * Vérifie que l'utilisateur a accès à cette école.
     */
    @GetMapping("/ecole/{ecoleId}")
    public ResponseEntity<List<ClasseDto>> getByEcoleId(@PathVariable Long ecoleId) {
        userContextService.checkAccessToEcole(ecoleId);
        return ResponseEntity.ok(classeService.getByEcoleId(ecoleId));
    }

    /**
     * Supprimer une classe.
     * Vérifie que la classe appartient à l'école de l'utilisateur.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Long userEcoleId = userContextService.getCurrentUserEcoleId();
        classeService.delete(id, userEcoleId);
        return ResponseEntity.noContent().build();
    }
}
