package com.digiSchool.digiSchool.user.serviceimp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.digiSchool.digiSchool.Exceptionconfig.service.TenantContext;
import com.digiSchool.digiSchool.academic.organisation.repository.InscriptionRepository;
import com.digiSchool.digiSchool.user.dto.EleveParentDto;
import com.digiSchool.digiSchool.user.model.Eleve;
import com.digiSchool.digiSchool.user.model.EleveParent;
import com.digiSchool.digiSchool.user.model.EleveParentHistory;
import com.digiSchool.digiSchool.user.model.Parent;
import com.digiSchool.digiSchool.user.model.TypeRelation;
import com.digiSchool.digiSchool.user.repository.EleveParentHistoryRepository;
import com.digiSchool.digiSchool.user.repository.EleveParentRepository;
import com.digiSchool.digiSchool.user.repository.EleveRepository;
import com.digiSchool.digiSchool.user.repository.ParentRepository;
import com.digiSchool.digiSchool.user.service.EleveParentService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EleveParentServiceImpl implements EleveParentService {

    private static final int MAX_PARENTS_PER_STUDENT = 3;

    private final EleveParentRepository eleveParentRepository;
    private final EleveParentHistoryRepository historyRepository;
    private final EleveRepository eleveRepository;
    private final ParentRepository parentRepository;
    private final InscriptionRepository inscriptionRepository;

    public EleveParentServiceImpl(
            EleveParentRepository eleveParentRepository,
            EleveParentHistoryRepository historyRepository,
            EleveRepository eleveRepository,
            ParentRepository parentRepository,
            InscriptionRepository inscriptionRepository) {
        this.eleveParentRepository = eleveParentRepository;
        this.historyRepository = historyRepository;
        this.eleveRepository = eleveRepository;
        this.parentRepository = parentRepository;
        this.inscriptionRepository = inscriptionRepository;
    }

    @Override
    public EleveParentDto create(EleveParentDto dto) {
        String tenant = TenantContext.getTenant();

        // Vérifier si la relation existe déjà (eleve_id + parent_id unique)
        if (eleveParentRepository.existsByEleveIdEleveAndParentIdParent(
                dto.getEleveId(), dto.getParentId())) {
            throw new RuntimeException("Cette relation élève-parent existe déjà");
        }

        // Vérifier le nombre maximum de parents par élève (max 3)
        long parentCount = eleveParentRepository.countByEleveIdEleve(dto.getEleveId());
        if (parentCount >= MAX_PARENTS_PER_STUDENT) {
            throw new RuntimeException("Un élève ne peut avoir que maximum " + MAX_PARENTS_PER_STUDENT + " parents");
        }

        Eleve eleve = eleveRepository.findById(dto.getEleveId())
                .orElseThrow(() -> new RuntimeException("Élève introuvable"));

        Parent parent = parentRepository.findById(dto.getParentId())
                .orElseThrow(() -> new RuntimeException("Parent introuvable"));

        // Si c'est marqué comme principal, vérifier qu'il n'y en a pas déjà un
        if (Boolean.TRUE.equals(dto.getEstPrincipal())) {
            eleveParentRepository.findPrincipalByEleve(dto.getEleveId())
                    .ifPresent(existing -> {
                        throw new RuntimeException("Cet élève a déjà un parent principal. Modifiez d'abord l'autre relation.");
                    });
        }

        // Si c'est le premier parent, le marquer comme principal par défaut
        boolean estPrincipal = dto.getEstPrincipal() != null ? dto.getEstPrincipal() : (parentCount == 0);

        EleveParent eleveParent = new EleveParent();
        eleveParent.setEleve(eleve);
        eleveParent.setParent(parent);
        eleveParent.setTypeRelation(dto.getTypeRelation());
        eleveParent.setEstPrincipal(estPrincipal);
        eleveParent.setAutorisePriseEnCharge(dto.getAutorisePriseEnCharge() != null ? dto.getAutorisePriseEnCharge() : true);
        eleveParent.setAutoriseUrgence(dto.getAutoriseUrgence() != null ? dto.getAutoriseUrgence() : true);
        eleveParent.setNotes(dto.getNotes());
        eleveParent.setTenant(tenant);
        eleveParent.setCreatedBy(dto.getCreatedBy());

        EleveParent saved = eleveParentRepository.save(eleveParent);

        // Enregistrer l'historique
        saveHistory(saved, "CREATE", null, dto.getTypeRelation(), null, estPrincipal,
                dto.getCreatedBy() != null ? dto.getCreatedBy() : 1L, "Création de la relation");

        return toDto(saved);
    }

    @Override
    public EleveParentDto update(Long id, EleveParentDto dto) {
        String tenant = TenantContext.getTenant();
        EleveParent eleveParent = eleveParentRepository.findByIdAndTenant(id, tenant)
                .orElseThrow(() -> new RuntimeException("Relation élève-parent introuvable"));

        // Sauvegarder les anciennes valeurs pour l'historique
        TypeRelation ancienneRelation = eleveParent.getTypeRelation();
        Boolean ancienEstPrincipal = eleveParent.getEstPrincipal();

        // Si on veut marquer comme principal, vérifier qu'il n'y en a pas déjà un autre
        if (Boolean.TRUE.equals(dto.getEstPrincipal()) && !Boolean.TRUE.equals(ancienEstPrincipal)) {
            eleveParentRepository.findPrincipalByEleve(eleveParent.getEleve().getIdEleve())
                    .filter(existing -> !existing.getIdEleveParent().equals(id))
                    .ifPresent(existing -> {
                        throw new RuntimeException("Cet élève a déjà un parent principal. Modifiez d'abord l'autre relation.");
                    });
        }

        // Mettre à jour
        eleveParent.setTypeRelation(dto.getTypeRelation());
        eleveParent.setEstPrincipal(dto.getEstPrincipal());
        eleveParent.setAutorisePriseEnCharge(dto.getAutorisePriseEnCharge());
        eleveParent.setAutoriseUrgence(dto.getAutoriseUrgence());
        eleveParent.setNotes(dto.getNotes());
        eleveParent.setUpdatedBy(dto.getUpdatedBy());

        // S'assurer que le tenant est défini (requis pour l'update)
        if (eleveParent.getTenant() == null) {
            eleveParent.setTenant(TenantContext.getTenant());
        }

        EleveParent saved = eleveParentRepository.save(eleveParent);

        // Enregistrer l'historique
        saveHistory(saved, "UPDATE", ancienneRelation, dto.getTypeRelation(),
                ancienEstPrincipal, dto.getEstPrincipal(),
                dto.getUpdatedBy() != null ? dto.getUpdatedBy() : 1L, "Mise à jour de la relation");

        return toDto(saved);
    }

    @Override
    public EleveParentDto getById(Long id) {
        String tenant = TenantContext.getTenant();
        return eleveParentRepository.findByIdAndTenant(id, tenant)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Relation élève-parent introuvable"));
    }

    @Override
    public List<EleveParentDto> getAll() {
        String tenant = TenantContext.getTenant();
        return eleveParentRepository.findAllByTenant(tenant)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<EleveParentDto> getByEleve(Long eleveId) {
        String tenant = TenantContext.getTenant();
        return eleveParentRepository.findByEleveIdEleveAndTenant(eleveId, tenant)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<EleveParentDto> getByParent(Long parentId) {
        String tenant = TenantContext.getTenant();
        return eleveParentRepository.findByParentIdParentAndTenant(parentId, tenant)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<EleveParentDto> getContactsUrgenceByEleve(Long eleveId) {
        String tenant = TenantContext.getTenant();
        return eleveParentRepository.findContactsUrgenceByEleveAndTenant(eleveId, tenant)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<EleveParentDto> getResponsablesLegauxByEleve(Long eleveId) {
        String tenant = TenantContext.getTenant();
        return eleveParentRepository.findAutorisesPriseEnChargeByEleveAndTenant(eleveId, tenant)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public EleveParentDto getPrincipalByEleve(Long eleveId) {
        String tenant = TenantContext.getTenant();
        return eleveParentRepository.findPrincipalByEleveAndTenant(eleveId, tenant)
                .map(this::toDto)
                .orElse(null);
    }

    @Override
    public void delete(Long id) {
        String tenant = TenantContext.getTenant();
        EleveParent eleveParent = eleveParentRepository.findByIdAndTenant(id, tenant)
                .orElseThrow(() -> new RuntimeException("Relation élève-parent introuvable"));

        // Enregistrer l'historique avant suppression
        saveHistory(eleveParent, "DELETE", eleveParent.getTypeRelation(), null,
                eleveParent.getEstPrincipal(), null, 1L, "Suppression de la relation");

        eleveParentRepository.deleteById(id);
    }

    // =====================
    // Historique
    // =====================

    private void saveHistory(EleveParent eleveParent, String action,
            TypeRelation ancienneRelation, TypeRelation nouvelleRelation,
            Boolean ancienEstPrincipal, Boolean nouveauEstPrincipal,
            Long modifiePar, String motif) {

        EleveParentHistory history = new EleveParentHistory();

        // Ne pas référencer l'entité lors de la suppression (elle sera supprimée)
        if (!"DELETE".equals(action)) {
            history.setEleveParent(eleveParent);
        }

        history.setEleveId(eleveParent.getEleve().getIdEleve());
        history.setParentId(eleveParent.getParent().getIdParent());
        history.setAction(action);
        history.setAncienneRelation(ancienneRelation);
        history.setNouvelleRelation(nouvelleRelation);
        history.setAncienEstPrincipal(ancienEstPrincipal);
        history.setNouveauEstPrincipal(nouveauEstPrincipal);
        history.setModifiePar(modifiePar);
        history.setMotif(motif);
        history.setTenant(TenantContext.getTenant());

        historyRepository.save(history);
    }

    // =====================
    // Mapping DTO <-> Entity
    // =====================

    private EleveParentDto toDto(EleveParent eleveParent) {
        EleveParentDto dto = new EleveParentDto();
        dto.setIdEleveParent(eleveParent.getIdEleveParent());
        dto.setTypeRelation(eleveParent.getTypeRelation());
        dto.setEstPrincipal(eleveParent.getEstPrincipal());
        dto.setAutorisePriseEnCharge(eleveParent.getAutorisePriseEnCharge());
        dto.setAutoriseUrgence(eleveParent.getAutoriseUrgence());
        dto.setNotes(eleveParent.getNotes());
        dto.setCreatedAt(eleveParent.getCreatedAt());
        dto.setUpdatedAt(eleveParent.getUpdatedAt());
        dto.setCreatedBy(eleveParent.getCreatedBy());
        dto.setUpdatedBy(eleveParent.getUpdatedBy());

        if (eleveParent.getEleve() != null) {
            dto.setEleveId(eleveParent.getEleve().getIdEleve());
            dto.setEleveMatricule(eleveParent.getEleve().getMatricule());
            dto.setEleveNom(eleveParent.getEleve().getNom());
            dto.setElevePrenom(eleveParent.getEleve().getPrenom());

            // Récupérer la classe actuelle de l'élève
            inscriptionRepository.findCurrentByEleveId(eleveParent.getEleve().getIdEleve())
                    .ifPresent(inscription -> {
                        if (inscription.getClasse() != null) {
                            dto.setEleveClasse(inscription.getClasse().getNomClasse());
                        }
                    });
        }

        if (eleveParent.getParent() != null) {
            dto.setParentId(eleveParent.getParent().getIdParent());
            dto.setParentMatricule(eleveParent.getParent().getMatriculeParent());
            dto.setParentNom(eleveParent.getParent().getNom());
            dto.setParentPrenom(eleveParent.getParent().getPrenom());
            dto.setParentTelephone(eleveParent.getParent().getTelephone());
            dto.setParentEmail(eleveParent.getParent().getEmail());
        }

        return dto;
    }
}
