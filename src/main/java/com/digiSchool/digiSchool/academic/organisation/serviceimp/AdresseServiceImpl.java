package com.digiSchool.digiSchool.academic.organisation.serviceimp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.digiSchool.digiSchool.Exceptionconfig.model.Adresse;
import com.digiSchool.digiSchool.Exceptionconfig.model.Quartier;
import com.digiSchool.digiSchool.academic.organisation.dto.AdresseDto;
import com.digiSchool.digiSchool.academic.organisation.repository.AdresseRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.QuartierRepository;
import com.digiSchool.digiSchool.academic.organisation.service.AdresseService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AdresseServiceImpl implements AdresseService {

    private final AdresseRepository adresseRepository;
    private final QuartierRepository quartierRepository;

    public AdresseServiceImpl(AdresseRepository adresseRepository, QuartierRepository quartierRepository) {
        this.adresseRepository = adresseRepository;
        this.quartierRepository = quartierRepository;
    }

    @Override
    public AdresseDto create(AdresseDto dto) {
        Adresse entity = toEntity(dto);
        Adresse saved = adresseRepository.save(entity);
        return toDto(saved);
    }

    @Override
    public AdresseDto update(Long id, AdresseDto dto) {
        Adresse entity = adresseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adresse introuvable"));
        entity.setRue(dto.getRue());
        entity.setRepere(dto.getRepere());
        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());
        if (dto.getQuartierId() != null) {
            Quartier quartier = quartierRepository.findById(dto.getQuartierId())
                    .orElseThrow(() -> new RuntimeException("Quartier introuvable"));
            entity.setQuartier(quartier);
        }
        return toDto(adresseRepository.save(entity));
    }

    @Override
    public AdresseDto getById(Long id) {
        return adresseRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Adresse introuvable"));
    }

    @Override
    public List<AdresseDto> getAll() {
        return adresseRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<AdresseDto> getByQuartierId(Long quartierId) {
        return adresseRepository.findByQuartierId(quartierId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!adresseRepository.existsById(id)) {
            throw new RuntimeException("Adresse introuvable");
        }
        adresseRepository.deleteById(id);
    }

    private Adresse toEntity(AdresseDto dto) {
        Adresse entity = new Adresse();
        entity.setRue(dto.getRue());
        entity.setRepere(dto.getRepere());
        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());
        if (dto.getQuartierId() != null) {
            Quartier quartier = quartierRepository.findById(dto.getQuartierId())
                    .orElseThrow(() -> new RuntimeException("Quartier introuvable"));
            entity.setQuartier(quartier);
        }
        return entity;
    }

    private AdresseDto toDto(Adresse entity) {
        AdresseDto dto = new AdresseDto();
        dto.setId(entity.getId());
        dto.setRue(entity.getRue());
        dto.setRepere(entity.getRepere());
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());
        if (entity.getQuartier() != null) {
            dto.setQuartierId(entity.getQuartier().getId());
            dto.setQuartierNom(entity.getQuartier().getNom());
        }
        return dto;
    }
}
