package com.digiSchool.digiSchool.Exceptionconfig.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.Exceptionconfig.model.Quartier;

@Repository
public interface QuartierRepository extends JpaRepository<Quartier, Long> {

    boolean existsByCode(String code);
}
