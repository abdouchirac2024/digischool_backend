package com.digiSchool.digiSchool.academic.organisation.service;

import java.util.List;

import com.digiSchool.digiSchool.academic.organisation.dto.AdresseDto;

public interface AdresseService {

    AdresseDto create(AdresseDto dto);

    AdresseDto update(Long id, AdresseDto dto);

    AdresseDto getById(Long id);

    List<AdresseDto> getAll();

    List<AdresseDto> getByQuartierId(Long quartierId);

    void delete(Long id);
}
