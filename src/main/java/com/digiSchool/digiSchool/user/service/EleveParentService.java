package com.digiSchool.digiSchool.user.service;

import java.util.List;

import com.digiSchool.digiSchool.user.dto.EleveParentDto;

public interface EleveParentService {

    EleveParentDto create(EleveParentDto dto);

    EleveParentDto update(Long id, EleveParentDto dto);

    EleveParentDto getById(Long id);

    List<EleveParentDto> getAll();

    List<EleveParentDto> getByEleve(Long eleveId);

    List<EleveParentDto> getByParent(Long parentId);

    List<EleveParentDto> getContactsUrgenceByEleve(Long eleveId);

    List<EleveParentDto> getResponsablesLegauxByEleve(Long eleveId);

    EleveParentDto getPrincipalByEleve(Long eleveId);

    void delete(Long id);
}
