package com.digiSchool.digiSchool.academic.organisation.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digiSchool.digiSchool.academic.organisation.dto.AnneeScolaireDTO;
import com.digiSchool.digiSchool.academic.organisation.dto.AnneeScolaireResponseDTO;
import com.digiSchool.digiSchool.academic.organisation.model.Anneescolaire;
import com.digiSchool.digiSchool.academic.organisation.service.AnneeScolaireService;

@RestController
@CrossOrigin
@RequestMapping("/api/annees-scolaires")
public class AnneeScolaireController {

    private final AnneeScolaireService service;

    public AnneeScolaireController(AnneeScolaireService service) {
        this.service = service;
    }

    @GetMapping
    public List<AnneeScolaireResponseDTO> getAll(@RequestParam String tenant) {
        return service.getAll(tenant);
    }

    // @GetMapping("/{id}")
    // public AnneeScolaireDTO getById(@PathVariable Long id) {
    // return service.getById(id);
    // }

    // 🔹 Détail d'une année scolaire
    @GetMapping("/{id}")
    public AnneeScolaireResponseDTO getById(
            @PathVariable Long id,
            @RequestParam String tenant) { // tenant obligatoire
        return service.findById(id, tenant);
    }

    @PostMapping
    public Anneescolaire create(@RequestBody @Validated AnneeScolaireDTO dto) {
        return service.create(dto);
    }

    // @PutMapping("/{id}")
    // public AnneeScolaireDTO update(@PathVariable Long id, @RequestBody @Valid
    // AnneeScolaireDTO dto) {
    // return service.update(id, dto);
    // }
    //
    // @DeleteMapping("/{id}")
    // public void delete(@PathVariable Long id) {
    // service.delete(id);
    // }
}
