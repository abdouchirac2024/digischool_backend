package com.digiSchool.digiSchool.academic.organisation.serviceimp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.digiSchool.digiSchool.academic.organisation.dto.ClasseDto;
import com.digiSchool.digiSchool.academic.organisation.model.Anneescolaire;
import com.digiSchool.digiSchool.academic.organisation.model.Classe;
import com.digiSchool.digiSchool.academic.organisation.model.Ecole;
import com.digiSchool.digiSchool.academic.organisation.model.StatutClasse;
import com.digiSchool.digiSchool.academic.organisation.repository.AnneescolaireRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.ClasseRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.EcoleRepository;
import com.digiSchool.digiSchool.academic.organisation.service.ClasseService;
import com.digiSchool.digiSchool.user.model.Utilisateur;
import com.digiSchool.digiSchool.user.repository.UtilisateurRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ClasseServiceImpl implements ClasseService {

    private final ClasseRepository classeRepository;
    private final EcoleRepository ecoleRepository;
    private final AnneescolaireRepository anneescolaireRepository;
    private final UtilisateurRepository utilisateurRepository;

    public ClasseServiceImpl(ClasseRepository classeRepository,
                             EcoleRepository ecoleRepository,
                             AnneescolaireRepository anneescolaireRepository,
                             UtilisateurRepository utilisateurRepository) {
        this.classeRepository = classeRepository;
        this.ecoleRepository = ecoleRepository;
        this.anneescolaireRepository = anneescolaireRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public ClasseDto create(ClasseDto dto) {
        if (dto.getEcoleId() == null) {
            throw new RuntimeException("L'identifiant de l'école est obligatoire");
        }

        Ecole ecole = ecoleRepository.findById(dto.getEcoleId())
                .orElseThrow(() -> new RuntimeException("École introuvable"));

        if (classeRepository.existsByNomClasseAndEcoleIdEcole(dto.getNomClasse(), dto.getEcoleId())) {
            throw new RuntimeException("Une classe avec ce nom existe déjà dans cette école");
        }

        Classe classe = toEntity(dto);
        classe.setEcole(ecole);

        // Statut par défaut : ACTIVE
        classe.setStatut(StatutClasse.ACTIVE);

        // Année scolaire (optionnel)
        if (dto.getAnneeScolaireId() != null) {
            Anneescolaire annee = anneescolaireRepository.findById(dto.getAnneeScolaireId())
                    .orElseThrow(() -> new RuntimeException("Année scolaire introuvable"));
            classe.setAnneeScolaire(annee);
        }

        // Titulaire (optionnel)
        if (dto.getTitulaireId() != null) {
            Utilisateur titulaire = utilisateurRepository.findById(dto.getTitulaireId())
                    .orElseThrow(() -> new RuntimeException("Enseignant titulaire introuvable"));
            classe.setTitulaire(titulaire);
        }

        Classe saved = classeRepository.save(classe);
        return toDto(saved);
    }

    @Override
    public ClasseDto update(Long id, ClasseDto dto) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classe introuvable"));

        classe.setNomClasse(dto.getNomClasse());
        classe.setNiveau(dto.getNiveau());
        classe.setSousSysteme(dto.getSousSysteme());
        classe.setSection(dto.getSection());
        classe.setCapacite(dto.getCapacite());
        classe.setFraisScolarite(dto.getFraisScolarite());
        classe.setDescription(dto.getDescription());

        // Le statut ne change que s'il est explicitement fourni
        if (dto.getStatut() != null) {
            classe.setStatut(dto.getStatut());
        }

        // Vérifier que la nouvelle capacité n'est pas inférieure à l'effectif actuel
        if (dto.getCapacite() != null) {
            long effectif = classeRepository.countInscriptionsByClasseId(id);
            if (dto.getCapacite() < effectif) {
                throw new RuntimeException(
                        "La capacité (" + dto.getCapacite() + ") ne peut pas être inférieure "
                        + "à l'effectif actuel (" + effectif + " élèves inscrits)");
            }
        }

        if (dto.getEcoleId() != null) {
            Ecole ecole = ecoleRepository.findById(dto.getEcoleId())
                    .orElseThrow(() -> new RuntimeException("École introuvable"));
            classe.setEcole(ecole);
        }

        if (dto.getAnneeScolaireId() != null) {
            Anneescolaire annee = anneescolaireRepository.findById(dto.getAnneeScolaireId())
                    .orElseThrow(() -> new RuntimeException("Année scolaire introuvable"));
            classe.setAnneeScolaire(annee);
        }

        if (dto.getTitulaireId() != null) {
            Utilisateur titulaire = utilisateurRepository.findById(dto.getTitulaireId())
                    .orElseThrow(() -> new RuntimeException("Enseignant titulaire introuvable"));
            classe.setTitulaire(titulaire);
        }

        return toDto(classeRepository.save(classe));
    }

    @Override
    public ClasseDto getById(Long id) {
        return classeRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Classe introuvable"));
    }

    @Override
    public List<ClasseDto> getAll() {
        return classeRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<ClasseDto> getByEcoleId(Long ecoleId) {
        return classeRepository.findByEcoleIdEcole(ecoleId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classe introuvable"));

        long effectif = classeRepository.countInscriptionsByClasseId(id);
        if (effectif > 0) {
            throw new RuntimeException(
                    "Impossible de supprimer cette classe : " + effectif
                    + " élève(s) encore inscrit(s). Archivez-la plutôt.");
        }

        classeRepository.delete(classe);
    }

    /**
     * Vérifie si la classe peut encore accepter des élèves.
     * Utilisé par le service d'inscription.
     */
    public void verifierCapacite(Long classeId) {
        Classe classe = classeRepository.findById(classeId)
                .orElseThrow(() -> new RuntimeException("Classe introuvable"));

        // Classe inactive ou archivée : pas d'inscription
        if (classe.getStatut() != StatutClasse.ACTIVE) {
            throw new RuntimeException(
                    "Impossible d'inscrire un élève dans une classe "
                    + classe.getStatut().name().toLowerCase());
        }

        // Vérifier la capacité
        if (classe.getCapacite() != null) {
            long effectif = classeRepository.countInscriptionsByClasseId(classeId);
            if (effectif >= classe.getCapacite()) {
                throw new RuntimeException(
                        "Classe complète : capacité maximale atteinte ("
                        + classe.getCapacite() + "/" + classe.getCapacite() + ")");
            }
        }
    }

    private Classe toEntity(ClasseDto dto) {
        Classe classe = new Classe();
        classe.setNomClasse(dto.getNomClasse());
        classe.setNiveau(dto.getNiveau());
        classe.setSousSysteme(dto.getSousSysteme());
        classe.setSection(dto.getSection());
        classe.setCapacite(dto.getCapacite());
        classe.setFraisScolarite(dto.getFraisScolarite());
        classe.setDescription(dto.getDescription());
        return classe;
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

        // Effectif actuel (nombre d'élèves inscrits)
        long effectif = classeRepository.countInscriptionsByClasseId(classe.getIdClasse());
        dto.setEffectifActuel((int) effectif);

        if (classe.getEcole() != null) {
            dto.setEcoleId(classe.getEcole().getIdEcole());
            dto.setEcoleNom(classe.getEcole().getNom());
        }

        if (classe.getAnneeScolaire() != null) {
            dto.setAnneeScolaireId(classe.getAnneeScolaire().getIdAnnee());
            dto.setAnneeScolaireLibelle(classe.getAnneeScolaire().getLibelle());
        }

        if (classe.getTitulaire() != null) {
            dto.setTitulaireId(classe.getTitulaire().getIdUtilisateur());
            dto.setTitulaireNom(classe.getTitulaire().getPrenom() + " " + classe.getTitulaire().getNom());
        }

        return dto;
    }
}
