package com.digiSchool.digiSchool.user.service;

import java.util.List;

import com.digiSchool.digiSchool.user.dto.ParentDto;

public interface ParentService {

    ParentDto create(ParentDto dto);

    ParentDto update(Long id, ParentDto dto);

    ParentDto getById(Long id);

    ParentDto getByMatricule(String matricule);

    List<ParentDto> getAll();

    List<ParentDto> search(String query);

    void delete(Long id);
}
