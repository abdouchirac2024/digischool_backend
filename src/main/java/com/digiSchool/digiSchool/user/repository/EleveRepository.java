package com.digiSchool.digiSchool.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.user.model.Eleve;

@Repository
public interface EleveRepository extends JpaRepository<Eleve, Long> {

    Optional<Eleve> findByMatricule(String matricule);

    boolean existsByMatricule(String matricule);

    @Query("SELECT e FROM Eleve e WHERE e.tenant = :tenant")
    List<Eleve> findAllByTenant(@Param("tenant") String tenant);

    // Méthodes tenant-aware pour isolation multi-tenant
    @Query("SELECT e FROM Eleve e WHERE e.idEleve = :id AND e.tenant = :tenant")
    Optional<Eleve> findByIdAndTenant(@Param("id") Long id, @Param("tenant") String tenant);

    @Query("SELECT e FROM Eleve e WHERE e.matricule = :matricule AND e.tenant = :tenant")
    Optional<Eleve> findByMatriculeAndTenant(@Param("matricule") String matricule, @Param("tenant") String tenant);

    List<Eleve> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);

    @Query("SELECT MAX(CAST(SUBSTRING(e.matricule, LENGTH(:prefix) + 1) AS integer)) " +
            "FROM Eleve e WHERE e.tenant = :tenant AND e.matricule LIKE CONCAT(:prefix, '%')")
    Integer findMaxMatriculeNumber(@Param("tenant") String tenant, @Param("prefix") String prefix);
}
