package com.digiSchool.digiSchool.config.seeder;

import org.springframework.stereotype.Component;

import com.digiSchool.digiSchool.user.model.Role;
import com.digiSchool.digiSchool.user.repository.RoleRepository;

/**
 * Seeder pour les roles utilisateur.
 * Cree les 5 roles : ADMIN, DIRECTEUR, ENSEIGNANT, SECRETAIRE, PARENT
 */
@Component
public class RoleSeeder {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Cree les roles s'ils n'existent pas deja
     */
    public void seed() {
        if (roleRepository.count() > 0) {
            System.out.println("  -> Roles : deja presents, skip");
            return;
        }

        createRole("ADMIN");
        createRole("DIRECTEUR");
        createRole("ENSEIGNANT");
        createRole("SECRETAIRE");
        createRole("PARENT");

        System.out.println("  -> Roles : 5 roles crees (ADMIN, DIRECTEUR, ENSEIGNANT, SECRETAIRE, PARENT)");
    }

    /**
     * Recupere un role par son nom
     */
    public Role getRole(String nomRole) {
        return roleRepository.findByNomRole(nomRole)
                .orElseThrow(() -> new RuntimeException("Role non trouve: " + nomRole));
    }

    private Role createRole(String nomRole) {
        Role r = new Role();
        r.setNomRole(nomRole);
        return roleRepository.save(r);
    }
}
