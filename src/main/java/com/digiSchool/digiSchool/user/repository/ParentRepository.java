package com.digiSchool.digiSchool.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.user.model.Parent;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {

    boolean existsByMatriculeParent(String matriculeParent);

    boolean existsByEmail(String email);

    boolean existsByEmailAndTenant(String email, String tenant);

    boolean existsByTelephone(String telephone);

    boolean existsByTelephoneAndTenant(String telephone, String tenant);

    Optional<Parent> findByMatriculeParent(String matriculeParent);

    Optional<Parent> findByEmail(String email);

    Optional<Parent> findByTelephone(String telephone);

    // Méthodes tenant-aware pour isolation multi-tenant
    @Query("SELECT p FROM Parent p WHERE p.idParent = :id AND p.tenant = :tenant AND p.deletedAt IS NULL")
    Optional<Parent> findByIdAndTenant(@Param("id") Long id, @Param("tenant") String tenant);

    @Query("SELECT p FROM Parent p WHERE p.matriculeParent = :matricule AND p.tenant = :tenant AND p.deletedAt IS NULL")
    Optional<Parent> findByMatriculeParentAndTenant(@Param("matricule") String matricule, @Param("tenant") String tenant);

    List<Parent> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);

    List<Parent> findByEmailContainingIgnoreCaseOrTelephoneContaining(String email, String telephone);

    List<Parent> findByActifTrue();

    List<Parent> findByDeletedAtIsNull();

    @Query("SELECT p FROM Parent p WHERE p.tenant = :tenant AND p.deletedAt IS NULL")
    List<Parent> findAllByTenant(@Param("tenant") String tenant);

    @Query("SELECT MAX(CAST(SUBSTRING(p.matriculeParent, 5) AS integer)) FROM Parent p WHERE p.tenant = :tenant")
    Integer findMaxMatriculeNumber(@Param("tenant") String tenant);

    @Query("SELECT p FROM Parent p WHERE p.deletedAt IS NULL AND " +
           "(LOWER(p.nom) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.prenom) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "p.telephone LIKE CONCAT('%', :query, '%'))")
    List<Parent> searchByQuery(@Param("query") String query);

    @Query("SELECT COUNT(p) FROM Parent p WHERE p.tenant = :tenant AND p.deletedAt IS NULL")
    Long countActiveByTenant(@Param("tenant") String tenant);
}
