package com.digiSchool.digiSchool.academic.organisation.service;

import java.util.List;

import com.digiSchool.digiSchool.academic.organisation.dto.DepartementDto;

public interface DepartementService {

    DepartementDto create(DepartementDto dto);

    DepartementDto update(Long id, DepartementDto dto);

    DepartementDto getById(Long id);

    List<DepartementDto> getAll();

    List<DepartementDto> getByRegionId(Long regionId);

    void delete(Long id);
}
