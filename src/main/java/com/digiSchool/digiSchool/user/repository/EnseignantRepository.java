package com.digiSchool.digiSchool.user.repository;

import com.digiSchool.digiSchool.user.model.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {
    Optional<Enseignant> findByUserEmail(String email);

    List<Enseignant> findBySpecialite(String specialite);
}
