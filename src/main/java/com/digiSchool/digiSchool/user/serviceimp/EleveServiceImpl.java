package com.digiSchool.digiSchool.user.serviceimp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.digiSchool.digiSchool.Exceptionconfig.model.Quartier;
import com.digiSchool.digiSchool.Exceptionconfig.service.TenantContext;
import com.digiSchool.digiSchool.academic.organisation.model.Anneescolaire;
import com.digiSchool.digiSchool.academic.organisation.model.Ecole;
import com.digiSchool.digiSchool.academic.organisation.repository.AnneescolaireRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.EcoleRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.QuartierRepository;
import com.digiSchool.digiSchool.user.dto.EleveDto;
import com.digiSchool.digiSchool.user.model.Eleve;
import com.digiSchool.digiSchool.user.model.Sexe;
import com.digiSchool.digiSchool.user.repository.EleveRepository;
import com.digiSchool.digiSchool.user.service.EleveService;
import com.digiSchool.digiSchool.user.service.IdentifierGeneratorService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EleveServiceImpl implements EleveService {

    private final EleveRepository eleveRepository;
    private final QuartierRepository quartierRepository;
    private final IdentifierGeneratorService identifierGeneratorService;
    private final EcoleRepository ecoleRepository;
    private final AnneescolaireRepository anneescolaireRepository;

    public EleveServiceImpl(EleveRepository eleveRepository,
            QuartierRepository quartierRepository,
            IdentifierGeneratorService identifierGeneratorService,
            EcoleRepository ecoleRepository,
            AnneescolaireRepository anneescolaireRepository) {
        this.eleveRepository = eleveRepository;
        this.quartierRepository = quartierRepository;
        this.identifierGeneratorService = identifierGeneratorService;
        this.ecoleRepository = ecoleRepository;
        this.anneescolaireRepository = anneescolaireRepository;
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
        eleve.setNationalite(dto.getNationalite());
        if (dto.getSexe() != null) {
            eleve.setSexe(Sexe.valueOf(dto.getSexe()));
        }
        eleve.setPhotoUrl(dto.getPhotoUrl());
        if (dto.getQuartierId() != null) {
            Quartier quartier = quartierRepository.findById(dto.getQuartierId())
                    .orElseThrow(() -> new RuntimeException("Quartier introuvable"));
            eleve.setQuartier(quartier);
        }

        // Generation matricule professionnel
        String libelleAnnee = anneescolaireRepository.findFirstByStatutTrue()
                .map(Anneescolaire::getLibelle)
                .orElse(String.valueOf(java.time.LocalDate.now().getYear()));

        Ecole ecole = ecoleRepository.findByTenant(tenant)
                .orElseThrow(() -> new RuntimeException(
                        "Ecole introuvable pour ce tenant. Impossible de générer le matricule."));

        String matricule = identifierGeneratorService.generateEleveMatricule(ecole, libelleAnnee);
        eleve.setMatricule(matricule);

        eleve.setTenant(tenant);

        Eleve saved = eleveRepository.save(eleve);
        return toDto(saved);
    }

    @Override
    public EleveDto update(Long id, EleveDto dto) {
        String tenant = TenantContext.getTenant();
        Eleve eleve = eleveRepository.findByIdAndTenant(id, tenant)
                .orElseThrow(() -> new RuntimeException("Élève introuvable"));

        if (dto.getNom() != null) {
            eleve.setNom(dto.getNom());
        }
        if (dto.getPrenom() != null) {
            eleve.setPrenom(dto.getPrenom());
        }
        if (dto.getDateNaissance() != null) {
            eleve.setDateNaissance(dto.getDateNaissance());
        }
        if (dto.getLieuNaissance() != null) {
            eleve.setLieuNaissance(dto.getLieuNaissance());
        }
        if (dto.getSexe() != null) {
            eleve.setSexe(Sexe.valueOf(dto.getSexe()));
        }
        if (dto.getNationalite() != null) {
            eleve.setNationalite(dto.getNationalite());
        }
        if (dto.getPhotoUrl() != null) {
            eleve.setPhotoUrl(dto.getPhotoUrl());
        }
        if (dto.getQuartierId() != null) {
            Quartier quartier = quartierRepository.findById(dto.getQuartierId())
                    .orElseThrow(() -> new RuntimeException("Quartier introuvable"));
            eleve.setQuartier(quartier);
        }

        Eleve saved = eleveRepository.save(eleve);
        return toDto(saved);
    }

    @Override
    public void delete(Long id) {
        String tenant = TenantContext.getTenant();
        Eleve eleve = eleveRepository.findByIdAndTenant(id, tenant)
                .orElseThrow(() -> new RuntimeException("Élève introuvable"));
        eleveRepository.delete(eleve);
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
        dto.setNationalite(eleve.getNationalite());
        dto.setPhotoUrl(eleve.getPhotoUrl());

        if (eleve.getQuartier() != null) {
            dto.setQuartierId(eleve.getQuartier().getId());
            dto.setQuartierNom(eleve.getQuartier().getNom());
        }

        return dto;
    }
}
