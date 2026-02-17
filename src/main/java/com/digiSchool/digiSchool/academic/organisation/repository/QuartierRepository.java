package com.digiSchool.digiSchool.academic.organisation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.Exceptionconfig.model.Quartier;

@Repository
public interface QuartierRepository extends JpaRepository<Quartier, Long> {

    boolean existsByCode(String code);

    Optional<Quartier> findByCode(String code);

    List<Quartier> findByVilleId(Long villeId);
}
