package com.digiSchool.digiSchool.user.repository;

import com.digiSchool.digiSchool.user.model.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {
    Optional<Enseignant> findByUserEmail(String email);

    List<Enseignant> findBySpecialite(String specialite);

    @Query("SELECT e FROM Enseignant e WHERE e.tenant = :tenant")
    List<Enseignant> findAllByTenant(@Param("tenant") String tenant);

    @Query("SELECT e FROM Enseignant e WHERE e.id = :id AND e.tenant = :tenant")
    Optional<Enseignant> findByIdAndTenant(@Param("id") Long id, @Param("tenant") String tenant);
}
