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

    /**
     * Créer une classe (ancien - utilise ecoleId du DTO)
     * @deprecated Utiliser create(dto, ecoleIdOverride) avec le contexte utilisateur
     */
    @Override
    @Deprecated
    public ClasseDto create(ClasseDto dto) {
        return create(dto, null);
    }

    /**
     * Créer une classe avec ecoleId automatique depuis le contexte utilisateur.
     * Si ecoleIdOverride est fourni (depuis JWT/header), il est utilisé à la place de dto.ecoleId.
     * @param dto Les données de la classe
     * @param ecoleIdOverride L'ecoleId de l'utilisateur connecté (peut être null en mode dev)
     */
    @Override
    public ClasseDto create(ClasseDto dto, Long ecoleIdOverride) {
        // Utiliser l'ecoleId du contexte utilisateur si disponible, sinon celui du DTO
        Long ecoleId = ecoleIdOverride != null ? ecoleIdOverride : dto.getEcoleId();

        if (ecoleId == null) {
            throw new RuntimeException("L'identifiant de l'école est obligatoire");
        }

        Ecole ecole = ecoleRepository.findById(ecoleId)
                .orElseThrow(() -> new RuntimeException("École introuvable"));

        if (classeRepository.existsByNomClasseAndEcoleIdEcole(dto.getNomClasse(), ecoleId)) {
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

    /**
     * Modifier une classe (ancien - sans validation d'accès)
     * @deprecated Utiliser update(id, dto, userEcoleId) avec le contexte utilisateur
     */
    @Override
    @Deprecated
    public ClasseDto update(Long id, ClasseDto dto) {
        return update(id, dto, null);
    }

    /**
     * Modifier une classe avec validation d'accès.
     * L'utilisateur ne peut modifier que les classes de son école.
     * @param id L'ID de la classe
     * @param dto Les nouvelles données
     * @param userEcoleId L'ecoleId de l'utilisateur pour validation (null = pas de validation, mode dev)
     */
    @Override
    public ClasseDto update(Long id, ClasseDto dto, Long userEcoleId) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classe introuvable"));

        // Validation d'accès : l'utilisateur ne peut modifier que les classes de son école
        if (userEcoleId != null && classe.getEcole() != null) {
            if (!userEcoleId.equals(classe.getEcole().getIdEcole())) {
                throw new RuntimeException("Accès non autorisé: vous ne pouvez pas modifier les classes d'une autre école");
            }
        }

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

        // Note: On ne permet plus de changer l'école d'une classe existante
        // L'ecoleId du DTO est ignoré lors de la mise à jour

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

    /**
     * Supprimer une classe (ancien - sans validation d'accès)
     * @deprecated Utiliser delete(id, userEcoleId) avec le contexte utilisateur
     */
    @Override
    @Deprecated
    public void delete(Long id) {
        delete(id, null);
    }

    /**
     * Supprimer une classe avec validation d'accès.
     * L'utilisateur ne peut supprimer que les classes de son école.
     * @param id L'ID de la classe
     * @param userEcoleId L'ecoleId de l'utilisateur pour validation (null = pas de validation, mode dev)
     */
    @Override
    public void delete(Long id, Long userEcoleId) {
        Classe classe = classeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classe introuvable"));

        // Validation d'accès : l'utilisateur ne peut supprimer que les classes de son école
        if (userEcoleId != null && classe.getEcole() != null) {
            if (!userEcoleId.equals(classe.getEcole().getIdEcole())) {
                throw new RuntimeException("Accès non autorisé: vous ne pouvez pas supprimer les classes d'une autre école");
            }
        }

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
