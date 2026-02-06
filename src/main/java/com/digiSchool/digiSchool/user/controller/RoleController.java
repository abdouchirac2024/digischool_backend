package com.digiSchool.digiSchool.user.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digiSchool.digiSchool.user.model.Role;
import com.digiSchool.digiSchool.user.repository.RoleRepository;

/**
 * Contrôleur pour la gestion des rôles.
 * Permet de récupérer dynamiquement les rôles disponibles.
 */
@RestController
@RequestMapping("/api/roles")
@CrossOrigin
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Récupérer tous les rôles disponibles.
     *
     * @return Liste des rôles avec id et nom
     */
    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<Role> roles = roleRepository.findAll();

        List<RoleDto> roleDtos = roles.stream()
                .map(role -> new RoleDto(role.getIdRole(), role.getNomRole()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(roleDtos);
    }

    /**
     * DTO pour les rôles.
     */
    public static class RoleDto {
        private Long id;
        private String name;

        public RoleDto(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}