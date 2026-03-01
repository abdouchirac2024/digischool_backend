package com.digiSchool.digiSchool.academic.organisation.serviceimp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.digiSchool.digiSchool.Exceptionconfig.model.Departement;
import com.digiSchool.digiSchool.Exceptionconfig.model.Region;
import com.digiSchool.digiSchool.academic.organisation.dto.DepartementDto;
import com.digiSchool.digiSchool.academic.organisation.repository.DepartementRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.RegionRepository;
import com.digiSchool.digiSchool.academic.organisation.service.DepartementService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DepartementServiceImpl implements DepartementService {

    private final DepartementRepository departementRepository;
    private final RegionRepository regionRepository;

    public DepartementServiceImpl(DepartementRepository departementRepository, RegionRepository regionRepository) {
        this.departementRepository = departementRepository;
        this.regionRepository = regionRepository;
    }

    @Override
    public DepartementDto create(DepartementDto dto) {
        if (departementRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Un département avec ce code existe déjà");
        }
        Departement entity = toEntity(dto);
        Departement saved = departementRepository.save(entity);
        return toDto(saved);
    }

    @Override
    public DepartementDto update(Long id, DepartementDto dto) {
        Departement entity = departementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Département introuvable"));
        entity.setNom(dto.getNom());
        entity.setCode(dto.getCode());
        entity.setChefLieu(dto.getChefLieu());
        if (dto.getRegionId() != null) {
            Region region = regionRepository.findById(dto.getRegionId())
                    .orElseThrow(() -> new RuntimeException("Région introuvable"));
            entity.setRegion(region);
        }
        return toDto(departementRepository.save(entity));
    }

    @Override
    public DepartementDto getById(Long id) {
        return departementRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Département introuvable"));
    }

    @Override
    public List<DepartementDto> getAll() {
        return departementRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<DepartementDto> getByRegionId(Long regionId) {
        return departementRepository.findByRegionId(regionId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!departementRepository.existsById(id)) {
            throw new RuntimeException("Département introuvable");
        }
        departementRepository.deleteById(id);
    }

    private Departement toEntity(DepartementDto dto) {
        Departement entity = new Departement();
        entity.setNom(dto.getNom());
        entity.setCode(dto.getCode());
        entity.setChefLieu(dto.getChefLieu());
        if (dto.getRegionId() != null) {
            Region region = regionRepository.findById(dto.getRegionId())
                    .orElseThrow(() -> new RuntimeException("Région introuvable"));
            entity.setRegion(region);
        }
        return entity;
    }

    private DepartementDto toDto(Departement entity) {
        DepartementDto dto = new DepartementDto();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        dto.setCode(entity.getCode());
        dto.setChefLieu(entity.getChefLieu());
        if (entity.getRegion() != null) {
            dto.setRegionId(entity.getRegion().getId());
            dto.setRegionNom(entity.getRegion().getNom());
        }
        return dto;
    }
}
