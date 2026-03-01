package com.digiSchool.digiSchool.academic.organisation.serviceimp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digiSchool.digiSchool.Exceptionconfig.service.TenantContext;
import com.digiSchool.digiSchool.academic.organisation.dto.AnneeScolaireDTO;
import com.digiSchool.digiSchool.academic.organisation.dto.AnneeScolaireResponseDTO;
import com.digiSchool.digiSchool.academic.organisation.dto.ExamenDTO;
import com.digiSchool.digiSchool.academic.organisation.dto.JourFerieDTO;
import com.digiSchool.digiSchool.academic.organisation.dto.PeriodeDTO;
import com.digiSchool.digiSchool.academic.organisation.dto.VacanceDTO;
import com.digiSchool.digiSchool.academic.organisation.model.Anneescolaire;
import com.digiSchool.digiSchool.academic.organisation.model.Examen;
import com.digiSchool.digiSchool.academic.organisation.model.JourFerie;
import com.digiSchool.digiSchool.academic.organisation.model.Periode;
import com.digiSchool.digiSchool.academic.organisation.model.Vacance;
import com.digiSchool.digiSchool.academic.organisation.repository.AnneeScolaireRepository;
import com.digiSchool.digiSchool.academic.organisation.service.AnneeScolaireService;

import jakarta.persistence.EntityManager;

@Service
@Transactional
public class AnneeScolaireServiceImp implements AnneeScolaireService {

    private final AnneeScolaireRepository repository;
    private final EntityManager entityManager;

    // 🔹 Injection par constructeur (manuelle)
    public AnneeScolaireServiceImp(AnneeScolaireRepository repository , EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Override
    public List<AnneeScolaireResponseDTO> getAll() {
        String tenant = TenantContext.getTenant();

        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("tenantFilter")
               .setParameter("tenant", tenant);

        return repository.findAll()
                         .stream()
                         .map(this::mapToDTO)
                         .toList();
    }

    @Override
    public AnneeScolaireResponseDTO findById(Long id) {
        String tenant = TenantContext.getTenant();

        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("tenantFilter")
               .setParameter("tenant", tenant);

        Anneescolaire entity = repository.findById(id)
                                         .orElseThrow(() -> new RuntimeException("Année scolaire introuvable"));
        return mapToDTO(entity);
    }

    @Override
    public Anneescolaire create(AnneeScolaireDTO dto) {
        // tenant déjà défini dans le DTO par le controller
        Anneescolaire annee = new Anneescolaire();
        annee.setLibelle(dto.getLabel());
        annee.setDateDebut(dto.getFrom());
        annee.setDateFin(dto.getTo());
        annee.setStatut(dto.getCurrent());
        annee.setTenant(dto.getTenantId()); // forcé depuis currentUser

        // Mapping des collections (periodes, examens, vacances, jours fériés)
        mapCollections(dto, annee);

        return repository.save(annee);
    }

    @Override
    @Transactional
    public AnneeScolaireResponseDTO update(Long id, AnneeScolaireDTO dto) {

        String tenant = TenantContext.getTenant();

        // 🔹 Activation filtre multi-tenant
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("tenantFilter")
               .setParameter("tenant", tenant);

        // 🔹 Recherche simple (le filtre applique le tenant)
        Anneescolaire existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Année scolaire introuvable"));
        
        System.out.println("===================================================");
        System.out.println("===================================================");
        System.out.println("===================================================");
        System.out.println("===================================================");
        System.out.println("===================================================");
        System.out.println("label = " + dto.getLabel());
        System.out.println("from = " + dto.getFrom());
        System.out.println("to = " + dto.getTo());
        System.out.println("current = " + dto.getCurrent());
        System.out.println("periods = " + dto.getPeriods());
        System.out.println("===================================================");
        System.out.println("===================================================");
        System.out.println("===================================================");
        System.out.println("===================================================");

        // 🔹 Mise à jour directe (sans conditions)
        existing.setLibelle(dto.getLabel());
        existing.setDateDebut(dto.getFrom());
        existing.setDateFin(dto.getTo());
        existing.setStatut(dto.getCurrent());

        // 🔹 Sécurité tenant
        if (existing.getTenant() == null) {
            existing.setTenant(tenant);
        }

        // 🔹 Mapping des collections
        mapCollections(dto, existing);

        Anneescolaire saved = repository.save(existing);

        return mapToDTO(saved);
    }

    private void mapCollections(AnneeScolaireDTO dto, Anneescolaire entity) {

        // 🔹 Initialisation des collections si elles sont null
        if (entity.getPeriodes() == null) entity.setPeriodes(new ArrayList<>());
        if (entity.getExamens() == null) entity.setExamens(new ArrayList<>());
        if (entity.getVacances() == null) entity.setVacances(new ArrayList<>());
        if (entity.getJoursFeries() == null) entity.setJoursFeries(new ArrayList<>());

        // 🔹 Périodes
        if (dto.getPeriods() != null) {
            entity.getPeriodes().clear();
            dto.getPeriods().forEach(p -> {
                Periode periode = new Periode();
                periode.setNom(p.getName());
                periode.setDateDebut(p.getStart());
                periode.setDateFin(p.getEnd());
                periode.setAnneeScolaire(entity);
                entity.getPeriodes().add(periode);
            });
        }

        // 🔹 Examens
        if (dto.getExams() != null) {
            entity.getExamens().clear();
            dto.getExams().forEach(e -> {
                Examen examen = new Examen();
                examen.setNom(e.getName());
                examen.setDateDebut(e.getStart());
                examen.setDateFin(e.getEnd());
                examen.setAnneeScolaire(entity);
                entity.getExamens().add(examen);
            });
        }

        // 🔹 Vacances
        if (dto.getHolidays() != null) {
            entity.getVacances().clear();
            dto.getHolidays().forEach(v -> {
                Vacance vacance = new Vacance();
                vacance.setNom(v.getName());
                vacance.setDateDebut(v.getStart());
                vacance.setDateFin(v.getEnd());
                vacance.setAnneeScolaire(entity);
                entity.getVacances().add(vacance);
            });
        }

        // 🔹 Jours fériés
        if (dto.getPublicHolidays() != null) {
            entity.getJoursFeries().clear();
            dto.getPublicHolidays().forEach(j -> {
                JourFerie jf = new JourFerie();
                jf.setNom(j.getName());
                jf.setDate(j.getDate());
                jf.setAnneeScolaire(entity);
                entity.getJoursFeries().add(jf);
            });
        }
    }
    
    private AnneeScolaireResponseDTO mapToDTO(Anneescolaire entity) {
        if (entity == null) return null;

        AnneeScolaireResponseDTO dto = new AnneeScolaireResponseDTO();
        dto.setId(entity.getIdAnnee());
        dto.setLibelle(entity.getLibelle());
        dto.setDateDebut(entity.getDateDebut());
        dto.setDateFin(entity.getDateFin());
        dto.setStatut(entity.getStatut());

        // 🔹 Mapping des périodes
        if (entity.getPeriodes() != null) {
            dto.setPeriods(entity.getPeriodes().stream()
                .map(p -> new PeriodeDTO(p.getNom(), p.getDateDebut(), p.getDateFin()))
                .toList());
        } else {
            dto.setPeriods(Collections.emptyList());
        }

        // 🔹 Mapping des examens
        if (entity.getExamens() != null) {
            dto.setExams(entity.getExamens().stream()
                .map(e -> new ExamenDTO(e.getNom(), e.getDateDebut(), e.getDateFin()))
                .toList());
        } else {
            dto.setExams(Collections.emptyList());
        }

        // 🔹 Mapping des vacances
        if (entity.getVacances() != null) {
            dto.setHolidays(entity.getVacances().stream()
                .map(v -> new VacanceDTO(v.getNom(), v.getDateDebut(), v.getDateFin()))
                .toList());
        } else {
            dto.setHolidays(Collections.emptyList());
        }

        // 🔹 Mapping des jours fériés
        if (entity.getJoursFeries() != null) {
            dto.setPublicHolidays(entity.getJoursFeries().stream()
                .map(j -> new JourFerieDTO(j.getNom(), j.getDate()))
                .toList());
        } else {
            dto.setPublicHolidays(Collections.emptyList());
        }

        return dto;
    }
}
