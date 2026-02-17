package com.digiSchool.digiSchool.academic.organisation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.Exceptionconfig.model.Arrondissement;

@Repository
public interface ArrondissementRepository extends JpaRepository<Arrondissement, Long> {

    boolean existsByCode(String code);

    List<Arrondissement> findByDepartementId(Long departementId);
}
