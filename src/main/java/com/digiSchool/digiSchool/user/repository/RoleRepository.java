package com.digiSchool.digiSchool.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.user.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByNomRole(String nomRole);

    boolean existsByNomRole(String nomRole);
}
