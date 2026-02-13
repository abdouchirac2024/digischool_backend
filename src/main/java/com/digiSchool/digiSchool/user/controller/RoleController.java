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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin
@Tag(name = "Roles")
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Operation(summary = "Lister les roles disponibles", description = "Retourne tous les roles definis dans le systeme (SUPER_ADMIN, ADMIN_ECOLE, ENSEIGNANT, etc.).")
    @ApiResponse(responseCode = "200", description = "Liste des roles")
    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<Role> roles = roleRepository.findAll();

        List<RoleDto> roleDtos = roles.stream()
                .map(role -> new RoleDto(role.getIdRole(), role.getNomRole()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(roleDtos);
    }

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
