package com.digiSchool.digiSchool.academic.organisation.serviceimp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.digiSchool.digiSchool.Exceptionconfig.model.Quartier;
import com.digiSchool.digiSchool.Exceptionconfig.model.Ville;
import com.digiSchool.digiSchool.academic.organisation.dto.QuartierDto;
import com.digiSchool.digiSchool.academic.organisation.repository.QuartierRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.VilleRepository;
import com.digiSchool.digiSchool.academic.organisation.service.QuartierService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class QuartierServiceImpl implements QuartierService {

    private final QuartierRepository quartierRepository;
    private final VilleRepository villeRepository;

    public QuartierServiceImpl(QuartierRepository quartierRepository, VilleRepository villeRepository) {
        this.quartierRepository = quartierRepository;
        this.villeRepository = villeRepository;
    }

    @Override
    public QuartierDto create(QuartierDto dto) {
        if (quartierRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Un quartier avec ce code existe déjà");
        }
        Quartier entity = toEntity(dto);
        Quartier saved = quartierRepository.save(entity);
        return toDto(saved);
    }

    @Override
    public QuartierDto update(Long id, QuartierDto dto) {
        Quartier entity = quartierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quartier introuvable"));
        entity.setNom(dto.getNom());
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());
        if (dto.getVilleId() != null) {
            Ville ville = villeRepository.findById(dto.getVilleId())
                    .orElseThrow(() -> new RuntimeException("Ville introuvable"));
            entity.setVille(ville);
        }
        return toDto(quartierRepository.save(entity));
    }

    @Override
    public QuartierDto getById(Long id) {
        return quartierRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Quartier introuvable"));
    }

    @Override
    public List<QuartierDto> getAll() {
        return quartierRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<QuartierDto> getByVilleId(Long villeId) {
        return quartierRepository.findByVilleId(villeId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!quartierRepository.existsById(id)) {
            throw new RuntimeException("Quartier introuvable");
        }
        quartierRepository.deleteById(id);
    }

    private Quartier toEntity(QuartierDto dto) {
        Quartier entity = new Quartier();
        entity.setNom(dto.getNom());
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());
        if (dto.getVilleId() != null) {
            Ville ville = villeRepository.findById(dto.getVilleId())
                    .orElseThrow(() -> new RuntimeException("Ville introuvable"));
            entity.setVille(ville);
        }
        return entity;
    }

    private QuartierDto toDto(Quartier entity) {
        QuartierDto dto = new QuartierDto();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        dto.setCode(entity.getCode());
        dto.setDescription(entity.getDescription());
        if (entity.getVille() != null) {
            dto.setVilleId(entity.getVille().getId());
            dto.setVilleNom(entity.getVille().getNom());
        }
        return dto;
    }
}
