package com.digiSchool.digiSchool.academic.organisation.serviceimp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.digiSchool.digiSchool.Exceptionconfig.model.Region;
import com.digiSchool.digiSchool.academic.organisation.dto.RegionDto;
import com.digiSchool.digiSchool.academic.organisation.repository.RegionRepository;
import com.digiSchool.digiSchool.academic.organisation.service.RegionService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    public RegionServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    public RegionDto create(RegionDto dto) {

        if (regionRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Une région avec ce code existe déjà");
        }

        Region region = toEntity(dto);
        Region saved = regionRepository.save(region);
        return toDto(saved);
    }

    @Override
    public RegionDto update(Long id, RegionDto dto) {

        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Région introuvable"));

        region.setNom(dto.getNom());
        region.setCode(dto.getCode());

        return toDto(regionRepository.save(region));
    }

    @Override
    public RegionDto getById(Long id) {
        return regionRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Région introuvable"));
    }

    @Override
    public List<RegionDto> getAll() {
        return regionRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!regionRepository.existsById(id)) {
            throw new RuntimeException("Région introuvable");
        }
        regionRepository.deleteById(id);
    }

    // =====================
    // Mapping DTO <-> Entity
    // =====================

    private Region toEntity(RegionDto dto) {
        Region region = new Region();
        region.setId(dto.getId());
        region.setNom(dto.getNom());
        region.setCode(dto.getCode());
        return region;
    }

    private RegionDto toDto(Region region) {
        RegionDto dto = new RegionDto();
        dto.setId(region.getId());
        dto.setNom(region.getNom());
        dto.setCode(region.getCode());
        return dto;
    }
}

