package com.digiSchool.digiSchool.academic.organisation.service;

import java.util.List;

import com.digiSchool.digiSchool.academic.organisation.dto.AnneeScolaireDTO;
import com.digiSchool.digiSchool.academic.organisation.dto.AnneeScolaireResponseDTO;
import com.digiSchool.digiSchool.academic.organisation.model.Anneescolaire;

public interface AnneeScolaireService {
	List<AnneeScolaireResponseDTO> getAll(String tenant);
//
	AnneeScolaireResponseDTO findById(Long id, String tenant);

	Anneescolaire create(AnneeScolaireDTO dto);

//	AnneeScolaireDTO update(Long id, AnneeScolaireDTO dto);
//
//    void delete(Long id);
}
