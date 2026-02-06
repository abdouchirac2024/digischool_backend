package com.digiSchool.digiSchool.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.user.model.Utilisateur;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    /**
     * Rechercher un utilisateur par son email
     */
    Optional<Utilisateur> findByEmail(String email);

    /**
     * Rechercher un utilisateur par son téléphone
     */
    Optional<Utilisateur> findByTelephone(String telephone);

    /**
     * Rechercher un utilisateur par email OU téléphone
     * Utilisé pour le login flexible
     */
    @Query("SELECT u FROM Utilisateur u WHERE u.email = :identifier OR u.telephone = :identifier")
    Optional<Utilisateur> findByEmailOrTelephone(@Param("identifier") String identifier);

    /**
     * Vérifier si un email existe déjà
     */
    boolean existsByEmail(String email);

    /**
     * Vérifier si un téléphone existe déjà
     */
    boolean existsByTelephone(String telephone);
}
