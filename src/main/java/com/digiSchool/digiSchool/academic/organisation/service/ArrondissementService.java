package com.digiSchool.digiSchool.academic.organisation.service;

import java.util.List;

import com.digiSchool.digiSchool.academic.organisation.dto.ArrondissementDto;

public interface ArrondissementService {

    ArrondissementDto create(ArrondissementDto dto);

    ArrondissementDto update(Long id, ArrondissementDto dto);

    ArrondissementDto getById(Long id);

    List<ArrondissementDto> getAll();

    List<ArrondissementDto> getByDepartementId(Long departementId);

    void delete(Long id);
}
