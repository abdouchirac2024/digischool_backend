package com.digiSchool.digiSchool.academic.organisation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.Exceptionconfig.model.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    boolean existsByCode(String code);
}
