package com.digiSchool.digiSchool.academic.organisation.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.digiSchool.digiSchool.academic.organisation.dto.*;
import com.digiSchool.digiSchool.academic.organisation.model.StatutEcole;

public interface EcoleService {
    EcoleDto inscrireEcole(InscriptionEcoleRequest request);
    EcoleDto validerEcole(Long ecoleId, ValidationEcoleRequest request);
    EcoleDto getById(Long id);
    EcoleDto getBySlug(String slug);
    List<EcoleDto> getAll();
    Page<EcoleDto> getAllPaged(Pageable pageable);
    List<EcoleDto> getByStatut(StatutEcole statut);
    EcoleDto updateBranding(Long ecoleId, BrandingRequest request);
    Map<String, Long> getStats();
    void delete(Long id);
}
