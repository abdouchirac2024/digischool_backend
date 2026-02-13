package com.digiSchool.digiSchool.academic.organisation.service;

import java.util.List;

import com.digiSchool.digiSchool.academic.organisation.dto.QuartierDto;

public interface QuartierService {

    QuartierDto create(QuartierDto dto);

    QuartierDto update(Long id, QuartierDto dto);

    QuartierDto getById(Long id);

    List<QuartierDto> getAll();

    List<QuartierDto> getByVilleId(Long villeId);

    void delete(Long id);
}
