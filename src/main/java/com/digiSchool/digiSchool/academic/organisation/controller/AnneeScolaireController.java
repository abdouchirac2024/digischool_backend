package com.digiSchool.digiSchool.academic.organisation.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digiSchool.digiSchool.academic.organisation.dto.AnneeScolaireDTO;
import com.digiSchool.digiSchool.academic.organisation.dto.AnneeScolaireResponseDTO;
import com.digiSchool.digiSchool.academic.organisation.model.Anneescolaire;
import com.digiSchool.digiSchool.academic.organisation.service.AnneeScolaireService;
import com.digiSchool.digiSchool.auth.model.User;
import com.digiSchool.digiSchool.auth.security.CurrentUser;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@CrossOrigin
@RequestMapping("/api/annees-scolaires")
public class AnneeScolaireController {

    private final AnneeScolaireService service;

    public AnneeScolaireController(AnneeScolaireService service) {
        this.service = service;
    }

    @GetMapping
    public List<AnneeScolaireResponseDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public AnneeScolaireResponseDTO getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Anneescolaire create(
            @RequestBody @Validated AnneeScolaireDTO dto,
            @Parameter(hidden = true) @CurrentUser User currentUser) {

        // On force le tenant de l'année scolaire au tenant du currentUser
        dto.setTenantId(currentUser.getTenantId());
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public AnneeScolaireResponseDTO update(
            @PathVariable Long id,
            @RequestBody @Validated AnneeScolaireDTO dto,
            @Parameter(hidden = true) @CurrentUser User currentUser) {
        System.out.println("====== UPDATE CONTROLLER ======");
        dto.setTenantId(currentUser.getTenantId());
        return service.update(id, dto);
    }
}