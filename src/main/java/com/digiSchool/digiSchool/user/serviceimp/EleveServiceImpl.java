package com.digiSchool.digiSchool.user.serviceimp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.digiSchool.digiSchool.Exceptionconfig.service.TenantContext;
import com.digiSchool.digiSchool.user.dto.EleveDto;
import com.digiSchool.digiSchool.user.model.Eleve;
import com.digiSchool.digiSchool.user.model.Sexe;
import com.digiSchool.digiSchool.user.repository.EleveRepository;
import com.digiSchool.digiSchool.user.service.EleveService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EleveServiceImpl implements EleveService {

    private final EleveRepository eleveRepository;

    public EleveServiceImpl(EleveRepository eleveRepository) {
        this.eleveRepository = eleveRepository;
    }

    @Override
    public List<EleveDto> getAll() {
        String tenant = TenantContext.getTenant();
        return eleveRepository.findAllByTenant(tenant)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public EleveDto getById(Long id) {
        String tenant = TenantContext.getTenant();
        return eleveRepository.findByIdAndTenant(id, tenant)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Élève introuvable"));
    }

    @Override
    public EleveDto getByMatricule(String matricule) {
        String tenant = TenantContext.getTenant();
        return eleveRepository.findByMatriculeAndTenant(matricule, tenant)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Élève introuvable"));
    }

    @Override
    public List<EleveDto> search(String query) {
        String tenant = TenantContext.getTenant();
        return eleveRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(query, query)
                .stream()
                .filter(e -> tenant.equals(e.getTenant()))
                .map(this::toDto)
                .toList();
    }

    @Override
    public EleveDto create(EleveDto dto) {
        String tenant = TenantContext.getTenant();

        Eleve eleve = new Eleve();
        eleve.setNom(dto.getNom());
        eleve.setPrenom(dto.getPrenom());
        eleve.setDateNaissance(dto.getDateNaissance());
        eleve.setLieuNaissance(dto.getLieuNaissance());
        if (dto.getSexe() != null) {
            eleve.setSexe(Sexe.valueOf(dto.getSexe()));
        }

        // Generation matricule simple
        String matricule = "E" + System.currentTimeMillis();
        eleve.setMatricule(matricule);

        eleve.setTenant(tenant);

        Eleve saved = eleveRepository.save(eleve);
        return toDto(saved);
    }

    // =====================
    // Mapping DTO <-> Entity
    // =====================

    private EleveDto toDto(Eleve eleve) {
        EleveDto dto = new EleveDto();
        dto.setIdEleve(eleve.getIdEleve());
        dto.setMatricule(eleve.getMatricule());
        dto.setNom(eleve.getNom());
        dto.setPrenom(eleve.getPrenom());
        dto.setDateNaissance(eleve.getDateNaissance());
        dto.setLieuNaissance(eleve.getLieuNaissance());
        if (eleve.getSexe() != null) {
            dto.setSexe(eleve.getSexe().name());
        }
        dto.setTenant(eleve.getTenant());

        if (eleve.getQuartier() != null) {
            dto.setQuartierNom(eleve.getQuartier().getNom());
        }

        return dto;
    }
}
