package com.digiSchool.digiSchool.academic.organisation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.academic.organisation.model.Ecole;

@Repository
public interface EcoleRepository extends JpaRepository<Ecole, Long> {

    boolean existsByCodeEcole(String codeEcole);
}
