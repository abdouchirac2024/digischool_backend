package com.digiSchool.digiSchool.academic.organisation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.academic.organisation.model.Echeance;

@Repository
public interface EcheanceRepository extends JpaRepository<Echeance, Long> {

    List<Echeance> findByInscriptionIdInscription(Long inscriptionId);

    @Query("SELECT e FROM Echeance e WHERE e.inscription.idInscription = :inscriptionId AND e.tenant = :tenant ORDER BY e.numero ASC")
    List<Echeance> findByInscriptionIdInscriptionAndTenant(@Param("inscriptionId") Long inscriptionId, @Param("tenant") String tenant);
}
