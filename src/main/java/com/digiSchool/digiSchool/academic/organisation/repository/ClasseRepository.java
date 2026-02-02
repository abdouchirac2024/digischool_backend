package com.digiSchool.digiSchool.academic.organisation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.academic.organisation.model.Classe;
import com.digiSchool.digiSchool.academic.organisation.model.StatutClasse;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {

    boolean existsByNomClasseAndEcoleIdEcole(String nomClasse, Long ecoleId);

    List<Classe> findByEcoleIdEcole(Long ecoleId);

    List<Classe> findByStatut(StatutClasse statut);

    List<Classe> findByEcoleIdEcoleAndStatut(Long ecoleId, StatutClasse statut);

    @Query("SELECT COUNT(i) FROM Inscription i WHERE i.classe.idClasse = :classeId")
    long countInscriptionsByClasseId(@Param("classeId") Long classeId);
}
