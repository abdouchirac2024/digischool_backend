package com.digiSchool.digiSchool.Exceptionconfig.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.Exceptionconfig.model.Departement;

@Repository
public interface DepartementRepository extends JpaRepository<Departement, Long> {

    boolean existsByCode(String code);
}
