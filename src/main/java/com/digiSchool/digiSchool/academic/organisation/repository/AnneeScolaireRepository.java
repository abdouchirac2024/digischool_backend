package com.digiSchool.digiSchool.academic.organisation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.academic.organisation.model.Anneescolaire;

@Repository      
public interface AnneeScolaireRepository extends JpaRepository<Anneescolaire, Long> {

	 Optional<Anneescolaire> findByLibelle(String libelle);

	 boolean existsByLibelle(String libelle);
	 
	 Optional<Anneescolaire> findFirstByLibelle(String libelle);
	 
	 Optional<Anneescolaire> findFirstByStatutTrue();
}
