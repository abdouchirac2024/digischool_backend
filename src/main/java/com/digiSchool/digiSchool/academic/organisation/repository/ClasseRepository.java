package com.digiSchool.digiSchool.academic.organisation.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.academic.organisation.model.Classe;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {
    boolean existsByNomClasseAndEcoleIdEcole(String nomClasse, Long idEcole);
    Optional<Classe> findByNomClasseAndTenant(String nomClasse, String tenant);
    java.util.List<Classe> findByEcoleIdEcole(Long ecoleId);
   long countByEcoleIdEcole(Long ecoleId);
    Page<Classe> findByEcoleIdEcole(Long ecoleId, Pageable pageable);
    }
