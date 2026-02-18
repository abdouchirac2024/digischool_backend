package com.digiSchool.digiSchool.academic.organisation.service;

import java.util.List;

import com.digiSchool.digiSchool.academic.organisation.dto.AnnulationRequest;
import com.digiSchool.digiSchool.academic.organisation.dto.InscriptionCreateRequest;
import com.digiSchool.digiSchool.academic.organisation.dto.InscriptionDto;

public interface InscriptionService {

    InscriptionDto creerInscription(InscriptionCreateRequest request);

    InscriptionDto annulerInscription(Long id, AnnulationRequest request);

    InscriptionDto getById(Long id);

    List<InscriptionDto> getAll();

    boolean eleveADesParents(Long eleveId);
}
