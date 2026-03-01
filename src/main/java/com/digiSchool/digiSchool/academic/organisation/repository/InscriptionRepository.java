package com.digiSchool.digiSchool.academic.organisation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.academic.organisation.model.Inscription;
import com.digiSchool.digiSchool.academic.organisation.model.StatutInscription;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {

    @Query("SELECT i FROM Inscription i JOIN FETCH i.classe WHERE i.eleve.idEleve = :eleveId AND i.statut = true ORDER BY i.dateInscription DESC")
    Optional<Inscription> findCurrentByEleveId(@Param("eleveId") Long eleveId);

    @Query("SELECT i FROM Inscription i WHERE i.tenant = :tenant ORDER BY i.createdAt DESC")
    List<Inscription> findAllByTenant(@Param("tenant") String tenant);

    @Query("SELECT i FROM Inscription i WHERE i.idInscription = :id AND i.tenant = :tenant")
    Optional<Inscription> findByIdAndTenant(@Param("id") Long id, @Param("tenant") String tenant);

    boolean existsByEleveIdEleveAndClasseIdClasseAndAnneeScolaireIdAnnee(Long eleveId, Long classeId, Long anneeId);

    @Query("SELECT MAX(CAST(SUBSTRING(i.numeroInscription, LENGTH(:prefix) + 1) AS integer)) FROM Inscription i WHERE i.tenant = :tenant AND i.numeroInscription LIKE CONCAT(:prefix, '%')")
    Integer findMaxNumeroByTenantAndPrefix(@Param("tenant") String tenant, @Param("prefix") String prefix);

    long countByClasseIdClasseAndStatutInscription(Long classeId, StatutInscription statut);
}
