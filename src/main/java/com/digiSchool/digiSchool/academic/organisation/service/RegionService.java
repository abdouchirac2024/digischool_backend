package com.digiSchool.digiSchool.academic.organisation.service;

import java.util.List;

import com.digiSchool.digiSchool.academic.organisation.dto.RegionDto;

public interface RegionService {

    RegionDto create(RegionDto dto);

    RegionDto update(Long id, RegionDto dto);

    RegionDto getById(Long id);

    List<RegionDto> getAll();

    void delete(Long id);
}

