package com.digiSchool.digiSchool.academic.organisation.service;

import java.util.List;

import com.digiSchool.digiSchool.academic.organisation.dto.VilleDto;

public interface VilleService {

    VilleDto create(VilleDto dto);

    VilleDto update(Long id, VilleDto dto);

    VilleDto getById(Long id);

    List<VilleDto> getAll();

    List<VilleDto> getByArrondissementId(Long arrondissementId);

    void delete(Long id);
}
