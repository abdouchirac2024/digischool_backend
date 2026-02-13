package com.digiSchool.digiSchool.user.service;

import java.util.List;

import com.digiSchool.digiSchool.user.dto.EleveDto;

public interface EleveService {
    List<EleveDto> getAll();

    EleveDto getById(Long id);

    EleveDto getByMatricule(String matricule);

    List<EleveDto> search(String query);

    EleveDto create(EleveDto dto);
}
