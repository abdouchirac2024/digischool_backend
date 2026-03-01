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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/classes")
@CrossOrigin
@Tag(name = "Classes")
public class ClasseController {

    private final ClasseService classeService;
    private final UserContextService userContextService;

    public ClasseController(ClasseService classeService, UserContextService userContextService) {
        this.classeService = classeService;
        this.userContextService = userContextService;
    }

    @Operation(summary = "Creer une classe", description = "Cree une nouvelle classe. L'ecoleId est automatiquement defini depuis le JWT de l'utilisateur.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Classe creee"),
        @ApiResponse(responseCode = "400", description = "Donnees invalides"),
        @ApiResponse(responseCode = "401", description = "Non authentifie")
    })
    @PostMapping
    public ResponseEntity<ClasseDto> create(@RequestBody ClasseDto dto) {
        Long userEcoleId = userContextService.getCurrentUserEcoleId();
        return ResponseEntity.ok(classeService.create(dto, userEcoleId));
    }

    @Operation(summary = "Modifier une classe", description = "Met a jour une classe existante. Verifie que la classe appartient a l'ecole de l'utilisateur.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Classe modifiee"),
        @ApiResponse(responseCode = "403", description = "Classe hors ecole"),
        @ApiResponse(responseCode = "404", description = "Classe introuvable")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClasseDto> update(
            @Parameter(description = "ID de la classe") @PathVariable Long id,
            @RequestBody ClasseDto dto
    ) {
        Long userEcoleId = userContextService.getCurrentUserEcoleId();
        return ResponseEntity.ok(classeService.update(id, dto, userEcoleId));
    }

    @Operation(summary = "Obtenir une classe par ID", description = "Retourne le detail d'une classe. Verifie l'acces a l'ecole.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detail de la classe"),
        @ApiResponse(responseCode = "403", description = "Acces interdit"),
        @ApiResponse(responseCode = "404", description = "Classe introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClasseDto> getById(
            @Parameter(description = "ID de la classe") @PathVariable Long id) {
        ClasseDto classe = classeService.getById(id);
        // Vérifier l'accès
        userContextService.checkAccessToEcole(classe.getEcoleId());
        return ResponseEntity.ok(classe);
    }

    @Operation(summary = "Lister les classes", description = "Retourne toutes les classes de l'ecole de l'utilisateur connecte.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste des classes")
    })
    @GetMapping
    public ResponseEntity<List<ClasseDto>> getAll() {
        Long userEcoleId = userContextService.getCurrentUserEcoleId();
        if (userEcoleId != null) {
            return ResponseEntity.ok(classeService.getByEcoleId(userEcoleId));
        }
        return ResponseEntity.ok(classeService.getAll());
    }

    @Operation(summary = "Lister les classes d'une ecole", description = "Retourne les classes d'une ecole specifique. Verifie que l'utilisateur a acces a cette ecole.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste des classes de l'ecole"),
        @ApiResponse(responseCode = "403", description = "Acces interdit a cette ecole")
    })
    @GetMapping("/ecole/{ecoleId}")
    public ResponseEntity<List<ClasseDto>> getByEcoleId(
            @Parameter(description = "ID de l'ecole") @PathVariable Long ecoleId) {
        userContextService.checkAccessToEcole(ecoleId);
        return ResponseEntity.ok(classeService.getByEcoleId(ecoleId));
    }

    @Operation(summary = "Supprimer une classe", description = "Supprime une classe. Verifie qu'elle appartient a l'ecole de l'utilisateur.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Classe supprimee"),
        @ApiResponse(responseCode = "403", description = "Classe hors ecole"),
        @ApiResponse(responseCode = "404", description = "Classe introuvable")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la classe") @PathVariable Long id) {
        Long userEcoleId = userContextService.getCurrentUserEcoleId();
        classeService.delete(id, userEcoleId);
        return ResponseEntity.noContent().build();
    }
}
