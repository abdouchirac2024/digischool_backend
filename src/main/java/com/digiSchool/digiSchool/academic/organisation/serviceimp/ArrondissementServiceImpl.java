package com.digiSchool.digiSchool.academic.organisation.serviceimp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.digiSchool.digiSchool.Exceptionconfig.model.Arrondissement;
import com.digiSchool.digiSchool.Exceptionconfig.model.Departement;
import com.digiSchool.digiSchool.academic.organisation.dto.ArrondissementDto;
import com.digiSchool.digiSchool.academic.organisation.repository.ArrondissementRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.DepartementRepository;
import com.digiSchool.digiSchool.academic.organisation.service.ArrondissementService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ArrondissementServiceImpl implements ArrondissementService {

    private final ArrondissementRepository arrondissementRepository;
    private final DepartementRepository departementRepository;

    public ArrondissementServiceImpl(ArrondissementRepository arrondissementRepository, DepartementRepository departementRepository) {
        this.arrondissementRepository = arrondissementRepository;
        this.departementRepository = departementRepository;
    }

    @Override
    public ArrondissementDto create(ArrondissementDto dto) {
        if (arrondissementRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Un arrondissement avec ce code existe déjà");
        }
        Arrondissement entity = toEntity(dto);
        Arrondissement saved = arrondissementRepository.save(entity);
        return toDto(saved);
    }

    @Override
    public ArrondissementDto update(Long id, ArrondissementDto dto) {
        Arrondissement entity = arrondissementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arrondissement introuvable"));
        entity.setNom(dto.getNom());
        entity.setCode(dto.getCode());
        if (dto.getDepartementId() != null) {
            Departement departement = departementRepository.findById(dto.getDepartementId())
                    .orElseThrow(() -> new RuntimeException("Département introuvable"));
            entity.setDepartement(departement);
        }
        return toDto(arrondissementRepository.save(entity));
    }

    @Override
    public ArrondissementDto getById(Long id) {
        return arrondissementRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Arrondissement introuvable"));
    }

    @Override
    public List<ArrondissementDto> getAll() {
        return arrondissementRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<ArrondissementDto> getByDepartementId(Long departementId) {
        return arrondissementRepository.findByDepartementId(departementId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!arrondissementRepository.existsById(id)) {
            throw new RuntimeException("Arrondissement introuvable");
        }
        arrondissementRepository.deleteById(id);
    }

    private Arrondissement toEntity(ArrondissementDto dto) {
        Arrondissement entity = new Arrondissement();
        entity.setNom(dto.getNom());
        entity.setCode(dto.getCode());
        if (dto.getDepartementId() != null) {
            Departement departement = departementRepository.findById(dto.getDepartementId())
                    .orElseThrow(() -> new RuntimeException("Département introuvable"));
            entity.setDepartement(departement);
        }
        return entity;
    }

    private ArrondissementDto toDto(Arrondissement entity) {
        ArrondissementDto dto = new ArrondissementDto();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        dto.setCode(entity.getCode());
        if (entity.getDepartement() != null) {
            dto.setDepartementId(entity.getDepartement().getId());
            dto.setDepartementNom(entity.getDepartement().getNom());
        }
        return dto;
    }
}
