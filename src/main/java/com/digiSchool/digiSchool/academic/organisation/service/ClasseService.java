package com.digiSchool.digiSchool.academic.organisation.service;

import java.util.List;

import com.digiSchool.digiSchool.academic.organisation.dto.ClasseDto;

public interface ClasseService {

    ClasseDto create(ClasseDto dto);

    ClasseDto update(Long id, ClasseDto dto);

    ClasseDto getById(Long id);

    List<ClasseDto> getAll();

    List<ClasseDto> getByEcoleId(Long ecoleId);

    void delete(Long id);

    /**
     * Vérifie si la classe peut encore accepter des élèves.
     * Lance une exception si la classe est inactive/archivée ou si la capacité est atteinte.
     */
    void verifierCapacite(Long classeId);
}
