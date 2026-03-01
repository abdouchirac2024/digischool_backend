package com.digiSchool.digiSchool.academic.organisation.serviceimp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.digiSchool.digiSchool.Exceptionconfig.model.Arrondissement;
import com.digiSchool.digiSchool.Exceptionconfig.model.Ville;
import com.digiSchool.digiSchool.academic.organisation.dto.VilleDto;
import com.digiSchool.digiSchool.academic.organisation.repository.ArrondissementRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.VilleRepository;
import com.digiSchool.digiSchool.academic.organisation.service.VilleService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class VilleServiceImpl implements VilleService {

    private final VilleRepository villeRepository;
    private final ArrondissementRepository arrondissementRepository;

    public VilleServiceImpl(VilleRepository villeRepository, ArrondissementRepository arrondissementRepository) {
        this.villeRepository = villeRepository;
        this.arrondissementRepository = arrondissementRepository;
    }

    @Override
    public VilleDto create(VilleDto dto) {
        if (villeRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Une ville avec ce code existe déjà");
        }
        Ville entity = toEntity(dto);
        Ville saved = villeRepository.save(entity);
        return toDto(saved);
    }

    @Override
    public VilleDto update(Long id, VilleDto dto) {
        Ville entity = villeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ville introuvable"));
        entity.setNom(dto.getNom());
        entity.setCode(dto.getCode());
        entity.setCodePostal(dto.getCodePostal());
        if (dto.getArrondissementId() != null) {
            Arrondissement arrondissement = arrondissementRepository.findById(dto.getArrondissementId())
                    .orElseThrow(() -> new RuntimeException("Arrondissement introuvable"));
            entity.setArrondissement(arrondissement);
        }
        return toDto(villeRepository.save(entity));
    }

    @Override
    public VilleDto getById(Long id) {
        return villeRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Ville introuvable"));
    }

    @Override
    public List<VilleDto> getAll() {
        return villeRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<VilleDto> getByArrondissementId(Long arrondissementId) {
        return villeRepository.findByArrondissementId(arrondissementId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!villeRepository.existsById(id)) {
            throw new RuntimeException("Ville introuvable");
        }
        villeRepository.deleteById(id);
    }

    private Ville toEntity(VilleDto dto) {
        Ville entity = new Ville();
        entity.setNom(dto.getNom());
        entity.setCode(dto.getCode());
        entity.setCodePostal(dto.getCodePostal());
        if (dto.getArrondissementId() != null) {
            Arrondissement arrondissement = arrondissementRepository.findById(dto.getArrondissementId())
                    .orElseThrow(() -> new RuntimeException("Arrondissement introuvable"));
            entity.setArrondissement(arrondissement);
        }
        return entity;
    }

    private VilleDto toDto(Ville entity) {
        VilleDto dto = new VilleDto();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        dto.setCode(entity.getCode());
        dto.setCodePostal(entity.getCodePostal());
        if (entity.getArrondissement() != null) {
            dto.setArrondissementId(entity.getArrondissement().getId());
            dto.setArrondissementNom(entity.getArrondissement().getNom());
        }
        return dto;
    }
}
