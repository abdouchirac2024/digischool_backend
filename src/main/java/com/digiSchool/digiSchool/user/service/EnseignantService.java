package com.digiSchool.digiSchool.user.service;

import com.digiSchool.digiSchool.user.dto.EnseignantDto;
import com.digiSchool.digiSchool.user.model.Enseignant;

import java.util.List;

public interface EnseignantService {
    Enseignant createEnseignant(EnseignantDto enseignantDto);

    Enseignant updateEnseignant(Long id, EnseignantDto enseignantDto);

    EnseignantDto getEnseignantById(Long id);

    List<EnseignantDto> getAllEnseignants();

    void deleteEnseignant(Long id);
}
