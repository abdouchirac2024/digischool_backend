package com.digiSchool.digiSchool.academic.organisation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.academic.organisation.model.Inscription;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {

    @Query("SELECT i FROM Inscription i JOIN FETCH i.classe WHERE i.eleve.idEleve = :eleveId AND i.statut = true ORDER BY i.dateInscription DESC")
    Optional<Inscription> findCurrentByEleveId(@Param("eleveId") Long eleveId);
}
