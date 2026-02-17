package com.digiSchool.digiSchool.academic.organisation.serviceimp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.digiSchool.digiSchool.academic.organisation.dto.ClasseDto;
import com.digiSchool.digiSchool.academic.organisation.model.Classe;
import com.digiSchool.digiSchool.academic.organisation.model.Ecole;
import com.digiSchool.digiSchool.academic.organisation.model.Anneescolaire;
import com.digiSchool.digiSchool.academic.organisation.model.StatutClasse;
import com.digiSchool.digiSchool.academic.organisation.repository.ClasseRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.EcoleRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.AnneescolaireRepository;
import com.digiSchool.digiSchool.academic.organisation.service.ClasseService;
import com.digiSchool.digiSchool.auth.model.User;
import com.digiSchool.digiSchool.auth.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ClasseServiceImpl implements ClasseService {

    private final ClasseRepository classeRepository;
    private final EcoleRepository ecoleRepository;
    private final AnneescolaireRepository anneescolaireRepository;
    private final UserRepository userRepository;

    public ClasseServiceImpl(ClasseRepository classeRepository,
                             EcoleRepository ecoleRepository,
                             AnneescolaireRepository anneescolaireRepository,
                             UserRepository userRepository) {
        this.classeRepository = classeRepository;
        this.ecoleRepository = ecoleRepository;
        this.anneescolaireRepository = anneescolaireRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Deprecated
    public ClasseDto create(ClasseDto dto) {
        return create(dto, dto.getEcoleId());
    }

    @Override
    public ClasseDto create(ClasseDto dto, Long ecoleIdOverride) {
        Long ecoleId = ecoleIdOverride != null ? ecoleIdOverride : dto.getEcoleId();

        if (ecoleId != null && classeRepository.existsByNomClasseAndEcoleIdEcole(dto.getNomClasse(), ecoleId)) {
            throw new RuntimeException("Une classe avec ce nom existe deja dans cette ecole");
        }

        Classe classe = new Classe();
        classe.setNomClasse(dto.getNomClasse());
        classe.setNiveau(dto.getNiveau());
        classe.setSousSysteme(dto.getSousSysteme());
        classe.setSection(dto.getSection());
        classe.setCapacite(dto.getCapacite());
        classe.setFraisScolarite(dto.getFraisScolarite());
        classe.setDescription(dto.getDescription());
        classe.setStatut(dto.getStatut() != null ? dto.getStatut() : StatutClasse.ACTIVE);

        if (ecoleId != null) {
            Ecole ecole = ecoleRepository.findById(ecoleId)
                    .orElseThrow(() -> new RuntimeException("Ecole introuvable"));
            classe.setEcole(ecole);
            classe.setTenant(ecole.getTenant());
        }

        if (dto.getAnneeScolaireId() != null) {
            Anneescolaire annee = anneescolaireRepository.findById(dto.getAnneeScolaireId())
                    .orElseThrow(() -> new RuntimeException("Annee scolaire introuvable"));
            classe.setAnneeScolaire(annee);
        }

        if (dto.getTitulaireId() != null) {
            User titulaire = userRepository.findById(dto.getTitulaireId())
                    .orElseThrow(() -> new RuntimeException("Titulaire introuvable"));
            classe.setTitulaire(titulaire);
        }

        Classe saved = classeRepository.save(classe);
        return toDto(saved);
    }

    @Override
    @Deprecated
    public ClasseDto update(Long id, ClasseDto dto) {
        return update(id, dto, null);
    }

    @Override
    public ClasseDto update(Long id, ClasseDto dto, Long userEcoleId) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classe introuvable"));

        if (userEcoleId != null && classe.getEcole() != null
                && !userEcoleId.equals(classe.getEcole().getIdEcole())) {
            throw new RuntimeException("Acces non autorise a cette classe");
        }

        if (dto.getNomClasse() != null) classe.setNomClasse(dto.getNomClasse());
        if (dto.getNiveau() != null) classe.setNiveau(dto.getNiveau());
        if (dto.getSousSysteme() != null) classe.setSousSysteme(dto.getSousSysteme());
        if (dto.getSection() != null) classe.setSection(dto.getSection());
        if (dto.getCapacite() != null) classe.setCapacite(dto.getCapacite());
        if (dto.getFraisScolarite() != null) classe.setFraisScolarite(dto.getFraisScolarite());
        if (dto.getDescription() != null) classe.setDescription(dto.getDescription());
        if (dto.getStatut() != null) classe.setStatut(dto.getStatut());

        if (dto.getAnneeScolaireId() != null) {
            Anneescolaire annee = anneescolaireRepository.findById(dto.getAnneeScolaireId())
                    .orElseThrow(() -> new RuntimeException("Annee scolaire introuvable"));
            classe.setAnneeScolaire(annee);
        }

        if (dto.getTitulaireId() != null) {
            User titulaire = userRepository.findById(dto.getTitulaireId())
                    .orElseThrow(() -> new RuntimeException("Titulaire introuvable"));
            classe.setTitulaire(titulaire);
        }

        Classe saved = classeRepository.save(classe);
        return toDto(saved);
    }

    @Override
    public ClasseDto getById(Long id) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classe introuvable"));
        return toDto(classe);
    }

    @Override
    public List<ClasseDto> getAll() {
        return classeRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClasseDto> getByEcoleId(Long ecoleId) {
        return classeRepository.findByEcoleIdEcole(ecoleId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Deprecated
    public void delete(Long id) {
        delete(id, null);
    }

    @Override
    public void delete(Long id, Long userEcoleId) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classe introuvable"));

        if (userEcoleId != null && classe.getEcole() != null
                && !userEcoleId.equals(classe.getEcole().getIdEcole())) {
            throw new RuntimeException("Acces non autorise a cette classe");
        }

        if (classe.getInscriptions() != null && !classe.getInscriptions().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer: eleves inscrits dans cette classe");
        }

        classeRepository.delete(classe);
    }

    @Override
    public void verifierCapacite(Long classeId) {
        Classe classe = classeRepository.findById(classeId)
                .orElseThrow(() -> new RuntimeException("Classe introuvable"));

        if (classe.getStatut() != StatutClasse.ACTIVE) {
            throw new RuntimeException("La classe n'est pas active");
        }

        int effectif = classe.getInscriptions() != null ? classe.getInscriptions().size() : 0;
        if (classe.getCapacite() != null && effectif >= classe.getCapacite()) {
            throw new RuntimeException("La capacite maximale de la classe est atteinte");
        }
    }

    private ClasseDto toDto(Classe classe) {
        ClasseDto dto = new ClasseDto();
        dto.setId(classe.getIdClasse());
        dto.setNomClasse(classe.getNomClasse());
        dto.setNiveau(classe.getNiveau());
        dto.setSousSysteme(classe.getSousSysteme());
        dto.setSection(classe.getSection());
        dto.setCapacite(classe.getCapacite());
        dto.setStatut(classe.getStatut());
        dto.setFraisScolarite(classe.getFraisScolarite());
        dto.setDescription(classe.getDescription());

        int effectif = classe.getInscriptions() != null ? classe.getInscriptions().size() : 0;
        dto.setEffectifActuel(effectif);

        if (classe.getEcole() != null) {
            dto.setEcoleId(classe.getEcole().getIdEcole());
            dto.setEcoleNom(classe.getEcole().getNom());
        }

        if (classe.getAnneeScolaire() != null) {
            dto.setAnneeScolaireId(classe.getAnneeScolaire().getIdAnnee());
            dto.setAnneeScolaireLibelle(classe.getAnneeScolaire().getLibelle());
        }

        if (classe.getTitulaire() != null) {
            dto.setTitulaireId(classe.getTitulaire().getId());
            dto.setTitulaireNom(classe.getTitulaire().getNom() + " " + classe.getTitulaire().getPrenom());
        }

        return dto;
    }
}