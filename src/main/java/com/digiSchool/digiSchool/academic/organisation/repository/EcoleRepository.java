package com.digiSchool.digiSchool.academic.organisation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.academic.organisation.model.Ecole;

@Repository
public interface EcoleRepository extends JpaRepository<Ecole, Long> {
    Optional<Ecole> findByCodeEcole(String codeEcole);

    boolean existsByCodeEcole(String codeEcole);

    Optional<Ecole> findByTenant(String tenant);
}
