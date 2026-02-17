package com.digiSchool.digiSchool.academic.organisation.serviceimp;

import java.util.Collections;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.digiSchool.digiSchool.academic.organisation.repository.AnneescolaireRepository;
import com.digiSchool.digiSchool.academic.organisation.service.AnneeScolaireService;

import jakarta.persistence.EntityManager;

@Service
@Transactional
public class AnneeScolaireServiceImp implements AnneeScolaireService {

    private final AnneescolaireRepository repository;
    private final EntityManager entityManager;

    // 🔹 Injection par constructeur (manuelle)
    public <AnneeScolaireRepository> AnneeScolaireServiceImp(AnneescolaireRepository repository , EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Override
    public Anneescolaire create(AnneeScolaireDTO dto) {

        // 🔹 Si current = true → désactiver l’ancienne année active
//        if (Boolean.TRUE.equals(dto.getCurrent())) {
//            Optional<Anneescolaire> existing = repository.findByStatutTrue();
//            if (existing.isPresent()) {
//                AnneeScolaire active = existing.get();
//                active.setStatut(false);
//                repository.save(active);
//            }
//        }
    	

        // 🔹 Création de l'entité principale
        Anneescolaire annee = new Anneescolaire();
        annee.setLibelle(dto.getLabel());
        annee.setDateDebut(dto.getFrom());
        annee.setDateFin(dto.getTo());
        annee.setStatut(dto.getCurrent());
        annee.setTenant(dto.getTenantId());

        // 🔹 Mapping périodes
        if (dto.getPeriods() != null) {

            List<Periode> periodes = dto.getPeriods().stream().map(p -> {

                Periode periode = new Periode();
                periode.setNom(p.getName());
                periode.setDateDebut(p.getStart());
                periode.setDateFin(p.getEnd());
                periode.setAnneeScolaire(annee);

                return periode;

            }).toList();

            annee.setPeriodes(periodes);
        }

        // 🔹 Mapping examens
        if (dto.getExams() != null) {

            List<Examen> examens = dto.getExams().stream().map(e -> {

                Examen examen = new Examen();
                examen.setNom(e.getName());
                examen.setDateDebut(e.getStart());
                examen.setDateFin(e.getEnd());
                examen.setAnneeScolaire(annee);

                return examen;

            }).toList();

            annee.setExamens(examens);
        }

        // 🔹 Mapping vacances
        if (dto.getHolidays() != null) {

            List<Vacance> vacances = dto.getHolidays().stream().map(v -> {

                Vacance vacance = new Vacance();
                vacance.setNom(v.getName());
                vacance.setDateDebut(v.getStart());
                vacance.setDateFin(v.getEnd());
                vacance.setAnneeScolaire(annee);

                return vacance;

            }).toList();

            annee.setVacances(vacances);
        }

        // 🔹 Mapping jours fériés
        if (dto.getPublicHolidays() != null) {

            List<JourFerie> joursFeries = dto.getPublicHolidays().stream().map(j -> {

                JourFerie jf = new JourFerie();
                jf.setNom(j.getName());
                jf.setDate(j.getDate());
                jf.setAnneeScolaire(annee);

                return jf;

            }).toList();

            annee.setJoursFeries(joursFeries);
        }

        // 🔥 Sauvegarde finale
        return repository.save(annee);
    }
    

    @Override
	public List<AnneeScolaireResponseDTO> getAll(String tenant) {

        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("tenantFilter")
               .setParameter("tenant", tenant);

        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    
    
    
    private AnneeScolaireResponseDTO mapToDTO(Anneescolaire entity) {

        AnneeScolaireResponseDTO dto = new AnneeScolaireResponseDTO();

        dto.setId(entity.getIdAnnee());
        dto.setLibelle(entity.getLibelle());
        dto.setDateDebut(entity.getDateDebut());
        dto.setDateFin(entity.getDateFin());
        dto.setStatut(entity.getStatut());

        // 🔹 Examens
        dto.setExams(
        	    entity.getExamens() != null
        	        ? entity.getExamens()
        	                .stream()
        	                .map(e -> {
        	                    ExamenDTO examenDTO = new ExamenDTO();
        	                    examenDTO.setName(e.getNom());
        	                    examenDTO.setStart(e.getDateDebut());
        	                    examenDTO.setEnd(e.getDateFin());
        	                    return examenDTO;
        	                })
        	                .toList()
        	        : Collections.emptyList()
        	);



        // 🔹 Vacances
        dto.setHolidays(
        	    entity.getVacances() != null
        	        ? entity.getVacances()
        	                .stream()
        	                .map(v -> {
        	                    VacanceDTO vacanceDTO = new VacanceDTO();
        	                    vacanceDTO.setName(v.getNom());
        	                    vacanceDTO.setStart(v.getDateDebut());
        	                    vacanceDTO.setEnd(v.getDateFin());
        	                    return vacanceDTO;
        	                })
        	                .toList()
        	        : Collections.emptyList()
        	);


        // 🔹 Jours fériés
        dto.setPublicHolidays(
        	    entity.getJoursFeries() != null
        	        ? entity.getJoursFeries()
        	                .stream()
        	                .map(j -> {
        	                    JourFerieDTO jourFerieDTO = new JourFerieDTO();
        	                    jourFerieDTO.setName(j.getNom());
        	                    jourFerieDTO.setDate(j.getDate());
        	                    return jourFerieDTO;
        	                })
        	                .toList()
        	        : Collections.emptyList()
        	);


        // 🔹 Périodes
        dto.setPeriods(
        	    entity.getPeriodes() != null
        	        ? entity.getPeriodes()
        	                .stream()
        	                .map(p -> {
        	                    PeriodeDTO periodeDTO = new PeriodeDTO();
        	                    periodeDTO.setName(p.getNom());
        	                    periodeDTO.setStart(p.getDateDebut());
        	                    periodeDTO.setEnd(p.getDateFin());
        	                    return periodeDTO;
        	                })
        	                .toList()
        	        : Collections.emptyList()
        	);


        return dto;
    }

    @Override
    public AnneeScolaireResponseDTO findById(Long id, String tenant) {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("tenantFilter")
               .setParameter("tenant", tenant);

        Anneescolaire entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Année scolaire introuvable"));

        return mapToDTO(entity);
    }



}
