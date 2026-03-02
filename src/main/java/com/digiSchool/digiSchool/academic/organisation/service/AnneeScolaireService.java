package com.digiSchool.digiSchool.academic.organisation.service;

import java.util.List;

import com.digiSchool.digiSchool.academic.organisation.dto.AnneeScolaireDTO;
import com.digiSchool.digiSchool.academic.organisation.dto.AnneeScolaireResponseDTO;
import com.digiSchool.digiSchool.academic.organisation.model.Anneescolaire;

public interface AnneeScolaireService {
	List<AnneeScolaireResponseDTO> getAll();
	AnneeScolaireResponseDTO findById(Long id);
	Anneescolaire create(AnneeScolaireDTO dto);
	AnneeScolaireResponseDTO update(Long id, AnneeScolaireDTO dto);
}
