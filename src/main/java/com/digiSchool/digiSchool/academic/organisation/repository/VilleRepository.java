package com.digiSchool.digiSchool.academic.organisation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.Exceptionconfig.model.Ville;

@Repository
public interface VilleRepository extends JpaRepository<Ville, Long> {

    boolean existsByCode(String code);

    List<Ville> findByArrondissementId(Long arrondissementId);
}
