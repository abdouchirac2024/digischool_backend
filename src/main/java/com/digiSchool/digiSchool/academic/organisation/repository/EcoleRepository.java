package com.digiSchool.digiSchool.academic.organisation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.academic.organisation.model.Ecole;
import com.digiSchool.digiSchool.academic.organisation.model.StatutEcole;

@Repository
public interface EcoleRepository extends JpaRepository<Ecole, Long> {
    Optional<Ecole> findByCodeEcole(String codeEcole);

    boolean existsByCodeEcole(String codeEcole);

    Optional<Ecole> findByTenant(String tenant);

    List<Ecole> findByStatutEcole(StatutEcole statut);

    List<Ecole> findAllByOrderByCreatedAtDesc();

    Optional<Ecole> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsByEmail(String email);

    long countByStatutEcole(StatutEcole statut);

    @Query("SELECT DISTINCT e FROM Ecole e " +
           "LEFT JOIN FETCH e.quartier q " +
           "LEFT JOIN FETCH q.ville v " +
           "LEFT JOIN FETCH v.arrondissement a " +
           "LEFT JOIN FETCH a.departement d " +
           "LEFT JOIN FETCH d.region r " +
           "ORDER BY e.createdAt DESC")
    List<Ecole> findAllWithGeography();
}
