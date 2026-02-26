package com.digiSchool.digiSchool.academic.organisation.serviceimp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.digiSchool.digiSchool.Exceptionconfig.service.TenantContext;
import com.digiSchool.digiSchool.academic.organisation.dto.AnnulationRequest;
import com.digiSchool.digiSchool.academic.organisation.dto.EcheanceDto;
import com.digiSchool.digiSchool.academic.organisation.dto.InscriptionCreateRequest;
import com.digiSchool.digiSchool.academic.organisation.dto.InscriptionDto;
import com.digiSchool.digiSchool.academic.organisation.model.Anneescolaire;
import com.digiSchool.digiSchool.academic.organisation.model.Classe;
import com.digiSchool.digiSchool.academic.organisation.model.Echeance;
import com.digiSchool.digiSchool.academic.organisation.model.Ecole;
import com.digiSchool.digiSchool.academic.organisation.model.Inscription;
import com.digiSchool.digiSchool.academic.organisation.model.StatutEcheance;
import com.digiSchool.digiSchool.academic.organisation.model.StatutInscription;
import com.digiSchool.digiSchool.academic.organisation.repository.AnneescolaireRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.ClasseRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.EcheanceRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.EcoleRepository;
import com.digiSchool.digiSchool.academic.organisation.repository.InscriptionRepository;
import com.digiSchool.digiSchool.academic.organisation.service.ClasseService;
import com.digiSchool.digiSchool.academic.organisation.service.InscriptionService;
import com.digiSchool.digiSchool.auth.model.RoleType;
import com.digiSchool.digiSchool.auth.model.User;
import com.digiSchool.digiSchool.auth.model.UserStatus;
import com.digiSchool.digiSchool.auth.repository.UserRepository;
import com.digiSchool.digiSchool.notification.model.Notification;
import com.digiSchool.digiSchool.notification.repository.NotificationRepository;
import com.digiSchool.digiSchool.user.model.Eleve;
import com.digiSchool.digiSchool.user.model.EleveParent;
import com.digiSchool.digiSchool.user.model.StatutEleve;
import com.digiSchool.digiSchool.user.repository.EleveParentRepository;
import com.digiSchool.digiSchool.user.repository.EleveRepository;
import com.digiSchool.digiSchool.user.service.SmsService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class InscriptionServiceImpl implements InscriptionService {

    private final InscriptionRepository inscriptionRepository;
    private final EcheanceRepository echeanceRepository;
    private final EleveRepository eleveRepository;
    private final ClasseRepository classeRepository;
    private final AnneescolaireRepository anneescolaireRepository;
    private final EleveParentRepository eleveParentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EcoleRepository ecoleRepository;
    private final NotificationRepository notificationRepository;
    private final ClasseService classeService;
    private final SmsService smsService;

    public InscriptionServiceImpl(
            InscriptionRepository inscriptionRepository,
            EcheanceRepository echeanceRepository,
            EleveRepository eleveRepository,
            ClasseRepository classeRepository,
            AnneescolaireRepository anneescolaireRepository,
            EleveParentRepository eleveParentRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EcoleRepository ecoleRepository,
            NotificationRepository notificationRepository,
            ClasseService classeService,
            SmsService smsService) {
        this.inscriptionRepository = inscriptionRepository;
        this.echeanceRepository = echeanceRepository;
        this.eleveRepository = eleveRepository;
        this.classeRepository = classeRepository;
        this.anneescolaireRepository = anneescolaireRepository;
        this.eleveParentRepository = eleveParentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.ecoleRepository = ecoleRepository;
        this.notificationRepository = notificationRepository;
        this.classeService = classeService;
        this.smsService = smsService;
    }

    @Override
    public InscriptionDto creerInscription(InscriptionCreateRequest request) {
        String tenant = TenantContext.getTenant();

        // 1. Charger l'eleve
        Eleve eleve = eleveRepository.findByIdAndTenant(request.getEleveId(), tenant)
                .orElseThrow(() -> new RuntimeException("Eleve introuvable"));

        // 2. Verifier que l'eleve a au moins 1 parent
        long nbParents = eleveParentRepository.countByEleveIdEleve(request.getEleveId());
        if (nbParents == 0) {
            throw new RuntimeException("L'eleve n'a aucun parent enregistre. Veuillez d'abord associer un parent.");
        }

        // 3. Charger la classe et verifier la capacite
        Classe classe = classeRepository.findById(request.getClasseId())
                .orElseThrow(() -> new RuntimeException("Classe introuvable"));
        classeService.verifierCapacite(request.getClasseId());

        // 4. Charger l'annee scolaire
        Anneescolaire annee;
        if (request.getAnneeScolaireId() != null) {
            annee = anneescolaireRepository.findById(request.getAnneeScolaireId())
                    .orElseThrow(() -> new RuntimeException("Annee scolaire introuvable"));
        } else {
            annee = anneescolaireRepository.findFirstByStatutTrue()
                    .orElseThrow(() -> new RuntimeException("Aucune annee scolaire active trouvee"));
        }

        // 5. Verifier le doublon
        if (inscriptionRepository.existsByEleveIdEleveAndClasseIdClasseAndAnneeScolaireIdAnnee(
                request.getEleveId(), request.getClasseId(), annee.getIdAnnee())) {
            throw new RuntimeException("Cet eleve est deja inscrit dans cette classe pour cette annee scolaire");
        }

        // 6. Generer le numero d'inscription
        String numeroInscription = generateNumeroInscription(tenant, annee.getLibelle());

        // 7. Date d'inscription
        LocalDate dateInscription = request.getDateInscription() != null
                ? request.getDateInscription()
                : LocalDate.now();

        // 8. Creer l'inscription
        Double fraisScolarite = classe.getFraisScolarite() != null ? classe.getFraisScolarite() : 0.0;
        Double remise = request.getRemise() != null ? request.getRemise() : 0.0;
        Double transport = request.getFraisTransport() != null ? request.getFraisTransport() : 0.0;
        Double cantine = request.getFraisCantine() != null ? request.getFraisCantine() : 0.0;
        Double assurance = request.getFraisAssurance() != null ? request.getFraisAssurance() : 0.0;

        Double montantTotal = (fraisScolarite - remise) + transport + cantine + assurance;

        Inscription inscription = new Inscription();
        inscription.setNumeroInscription(numeroInscription);
        inscription.setDateInscription(dateInscription);
        inscription.setMontantTotal(montantTotal);
        inscription.setRemise(remise);
        inscription.setFraisTransport(transport);
        inscription.setFraisCantine(cantine);
        inscription.setFraisAssurance(assurance);
        inscription.setStatut(true);
        inscription.setStatutInscription(StatutInscription.VALIDEE);
        inscription.setEleve(eleve);
        inscription.setClasse(classe);
        inscription.setAnneeScolaire(annee);
        inscription.setTenant(tenant);

        Inscription saved = inscriptionRepository.save(inscription);

        // 9. Creer les 3 echeances
        List<Echeance> echeances = creerEcheances(saved, montantTotal, dateInscription, tenant);

        // 9b. Marquer les tranches selectionnees comme payees
        if (request.getTranchesPayees() != null && !request.getTranchesPayees().isEmpty()) {
            for (Echeance echeance : echeances) {
                if (request.getTranchesPayees().contains(echeance.getNumero())) {
                    echeance.setStatut(StatutEcheance.PAYEE);
                    echeance.setDatePaiement(dateInscription);
                }
            }
            echeanceRepository.saveAll(echeances);
        }

        // 10. Changer statut eleve -> ACTIF
        eleve.setStatut(StatutEleve.ACTIF);
        eleveRepository.save(eleve);

        // 11. Creer compte User ELEVE
        String generatedEmail = eleve.getMatricule().toLowerCase() + "@eleve.digischool.cm";
        String generatedPassword = generatePassword();
        createUserAccount(eleve, tenant, generatedEmail, generatedPassword);

        // 12. Notifier le parent principal
        notifyParentPrincipal(eleve, classe, annee, montantTotal, echeances, tenant);

        // Construire le DTO de reponse
        InscriptionDto dto = toDto(saved);
        dto.setEcheances(echeances.stream().map(this::toEcheanceDto).collect(Collectors.toList()));
        dto.setGeneratedPassword(generatedPassword);
        dto.setGeneratedEmail(generatedEmail);
        return dto;
    }

    @Override
    public InscriptionDto annulerInscription(Long id, AnnulationRequest request) {
        String tenant = TenantContext.getTenant();

        Inscription inscription = inscriptionRepository.findByIdAndTenant(id, tenant)
                .orElseThrow(() -> new RuntimeException("Inscription introuvable"));

        if (inscription.getStatutInscription() == StatutInscription.ANNULEE) {
            throw new RuntimeException("Cette inscription est deja annulee");
        }

        // Annuler l'inscription
        inscription.setStatutInscription(StatutInscription.ANNULEE);
        inscription.setStatut(false);
        inscription.setMotifAnnulation(request.getMotif());

        // Remettre statut eleve a INSCRIT
        Eleve eleve = inscription.getEleve();
        eleve.setStatut(StatutEleve.INSCRIT);
        eleveRepository.save(eleve);

        // Annuler les echeances non payees
        List<Echeance> echeances = echeanceRepository.findByInscriptionIdInscriptionAndTenant(id, tenant);
        for (Echeance echeance : echeances) {
            if (echeance.getStatut() == StatutEcheance.EN_ATTENTE) {
                echeance.setStatut(StatutEcheance.EN_ATTENTE);
            }
        }
        echeanceRepository.saveAll(echeances);

        Inscription saved = inscriptionRepository.save(inscription);
        InscriptionDto dto = toDto(saved);
        dto.setEcheances(echeances.stream().map(this::toEcheanceDto).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public InscriptionDto getById(Long id) {
        String tenant = TenantContext.getTenant();
        Inscription inscription = inscriptionRepository.findByIdAndTenant(id, tenant)
                .orElseThrow(() -> new RuntimeException("Inscription introuvable"));

        InscriptionDto dto = toDto(inscription);
        List<Echeance> echeances = echeanceRepository.findByInscriptionIdInscriptionAndTenant(id, tenant);
        dto.setEcheances(echeances.stream().map(this::toEcheanceDto).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public List<InscriptionDto> getAll() {
        String tenant = TenantContext.getTenant();
        return inscriptionRepository.findAllByTenant(tenant).stream()
                .map(inscription -> {
                    InscriptionDto dto = toDto(inscription);
                    List<Echeance> echeances = echeanceRepository
                            .findByInscriptionIdInscription(inscription.getIdInscription());
                    dto.setEcheances(echeances.stream().map(this::toEcheanceDto).collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean eleveADesParents(Long eleveId) {
        return eleveParentRepository.countByEleveIdEleve(eleveId) > 0;
    }

    // =====================
    // Methodes privees
    // =====================

    private String generateNumeroInscription(String tenant, String anneeLibelle) {
        // Format: INS-{TENANT}-{ANNEE}-{NUMERO}
        // ex: INS-DIGI-001-2025-2026-00001
        String anneeShort = anneeLibelle != null ? anneeLibelle.replace("/", "-")
                : String.valueOf(LocalDate.now().getYear());
        String prefix = "INS-" + tenant + "-" + anneeShort + "-";

        Integer maxNum = inscriptionRepository.findMaxNumeroByTenantAndPrefix(tenant, prefix);
        int nextNum = (maxNum != null ? maxNum : 0) + 1;
        return prefix + String.format("%05d", nextNum);
    }

    private List<Echeance> creerEcheances(Inscription inscription, Double montantTotal, LocalDate dateInscription,
            String tenant) {
        List<Echeance> echeances = new ArrayList<>();

        // Tranche 1: 40% - Frais d'inscription (a la date d'inscription)
        Echeance tranche1 = new Echeance();
        tranche1.setInscription(inscription);
        tranche1.setNumero(1);
        tranche1.setLibelle("Frais d'inscription");
        tranche1.setMontant(Math.round(montantTotal * 0.40 * 100.0) / 100.0);
        tranche1.setDateEcheance(dateInscription);
        tranche1.setStatut(StatutEcheance.EN_ATTENTE);
        tranche1.setTenant(tenant);
        echeances.add(tranche1);

        // Tranche 2: 30% - 1er versement trimestriel (3 mois apres)
        Echeance tranche2 = new Echeance();
        tranche2.setInscription(inscription);
        tranche2.setNumero(2);
        tranche2.setLibelle("1er versement trimestriel");
        tranche2.setMontant(Math.round(montantTotal * 0.30 * 100.0) / 100.0);
        tranche2.setDateEcheance(dateInscription.plusMonths(3));
        tranche2.setStatut(StatutEcheance.EN_ATTENTE);
        tranche2.setTenant(tenant);
        echeances.add(tranche2);

        // Tranche 3: 30% - 2eme versement trimestriel (6 mois apres)
        Echeance tranche3 = new Echeance();
        tranche3.setInscription(inscription);
        tranche3.setNumero(3);
        tranche3.setLibelle("2eme versement trimestriel");
        tranche3.setMontant(Math.round(montantTotal * 0.30 * 100.0) / 100.0);
        tranche3.setDateEcheance(dateInscription.plusMonths(6));
        tranche3.setStatut(StatutEcheance.EN_ATTENTE);
        tranche3.setTenant(tenant);
        echeances.add(tranche3);

        return echeanceRepository.saveAll(echeances);
    }

    private String generatePassword() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return "Eleve@" + code;
    }

    private void createUserAccount(Eleve eleve, String tenant, String email, String rawPassword) {
        // Verifier si un compte existe deja avec cet email
        if (userRepository.existsByEmail(email)) {
            return; // Compte deja cree (inscription precedente)
        }

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setNom(eleve.getNom());
        user.setPrenom(eleve.getPrenom());
        user.setRole(RoleType.ELEVE);
        user.setStatus(UserStatus.ACTIVE);
        user.setUsernameField(eleve.getMatricule());
        user.setTenantId(tenant);

        Ecole ecole = ecoleRepository.findByTenant(tenant).orElse(null);
        if (ecole != null) {
            user.setEcole(ecole);
        }
        if (eleve.getQuartier() != null) {
            user.setQuartier(eleve.getQuartier());
        }

        userRepository.save(user);
    }

    private void notifyParentPrincipal(Eleve eleve, Classe classe, Anneescolaire annee,
            Double montantTotal, List<Echeance> echeances, String tenant) {
        // Trouver le parent principal
        EleveParent parentPrincipal = eleveParentRepository.findPrincipalByEleve(eleve.getIdEleve())
                .orElse(null);

        if (parentPrincipal == null) {
            // Prendre le premier parent si pas de principal
            List<EleveParent> parents = eleveParentRepository.findByEleveIdEleve(eleve.getIdEleve());
            if (!parents.isEmpty()) {
                parentPrincipal = parents.get(0);
            }
        }

        if (parentPrincipal == null || parentPrincipal.getParent() == null) {
            return;
        }

        // Trouver le compte User du parent (via email)
        String parentEmail = parentPrincipal.getParent().getEmail();
        User parentUser = userRepository.findByEmail(parentEmail).orElse(null);
        if (parentUser == null) {
            return;
        }

        // Construire le message
        StringBuilder msg = new StringBuilder();
        msg.append("Votre enfant ").append(eleve.getPrenom()).append(" ").append(eleve.getNom());
        msg.append(" a ete inscrit(e) en ").append(classe.getNomClasse());
        if (annee != null) {
            msg.append(" pour l'annee ").append(annee.getLibelle());
        }
        msg.append(".\n\nEcheancier de paiement (").append(String.format("%.0f", montantTotal)).append(" FCFA):\n");
        for (Echeance e : echeances) {
            msg.append("- ").append(e.getLibelle()).append(": ")
                    .append(String.format("%.0f", e.getMontant())).append(" FCFA (echeance: ")
                    .append(e.getDateEcheance()).append(")\n");
        }

        Notification notification = new Notification();
        notification.setTenantId(tenant);
        notification.setDestinataireId(parentUser.getId());
        notification.setTitre("Inscription de votre enfant " + eleve.getPrenom() + " " + eleve.getNom());
        notification.setMessage(msg.toString());
        notification.setType("INSCRIPTION");
        notification.setLue(false);
        notificationRepository.save(notification);

        // Envoi SMS Mock
        String parentPhone = parentPrincipal.getParent().getTelephone();
        if (parentPhone != null && !parentPhone.isEmpty()) {
            String smsText = "DigiSchool: Inscription confirmee pour " + eleve.getPrenom() + " " + eleve.getNom() +
                    " en classe de " + classe.getNomClasse() + ". Montant: " + String.format("%.0f", montantTotal)
                    + " FCFA.";
            smsService.sendSms(parentPhone, smsText);
        }
    }

    // =====================
    // Mapping DTO
    // =====================

    private InscriptionDto toDto(Inscription inscription) {
        InscriptionDto dto = new InscriptionDto();
        dto.setIdInscription(inscription.getIdInscription());
        dto.setNumeroInscription(inscription.getNumeroInscription());
        dto.setDateInscription(inscription.getDateInscription());
        dto.setMontantTotal(inscription.getMontantTotal());
        dto.setRemise(inscription.getRemise());
        dto.setFraisTransport(inscription.getFraisTransport());
        dto.setFraisCantine(inscription.getFraisCantine());
        dto.setFraisAssurance(inscription.getFraisAssurance());
        dto.setMotifAnnulation(inscription.getMotifAnnulation());

        if (inscription.getStatutInscription() != null) {
            dto.setStatutInscription(inscription.getStatutInscription().name());
        }

        Eleve eleve = inscription.getEleve();
        if (eleve != null) {
            dto.setEleveId(eleve.getIdEleve());
            dto.setEleveMatricule(eleve.getMatricule());
            dto.setEleveNom(eleve.getNom());
            dto.setElevePrenom(eleve.getPrenom());
        }

        Classe classe = inscription.getClasse();
        if (classe != null) {
            dto.setClasseId(classe.getIdClasse());
            dto.setClasseNom(classe.getNomClasse());
            if (classe.getNiveau() != null) {
                dto.setClasseNiveau(classe.getNiveau().name());
            }
            dto.setFraisScolarite(classe.getFraisScolarite());
        }

        Anneescolaire annee = inscription.getAnneeScolaire();
        if (annee != null) {
            dto.setAnneeScolaireId(annee.getIdAnnee());
            dto.setAnneeScolaireLibelle(annee.getLibelle());
        }

        return dto;
    }

    private EcheanceDto toEcheanceDto(Echeance echeance) {
        EcheanceDto dto = new EcheanceDto();
        dto.setIdEcheance(echeance.getIdEcheance());
        dto.setNumero(echeance.getNumero());
        dto.setLibelle(echeance.getLibelle());
        dto.setMontant(echeance.getMontant());
        dto.setDateEcheance(echeance.getDateEcheance());
        dto.setDatePaiement(echeance.getDatePaiement());
        if (echeance.getStatut() != null) {
            dto.setStatut(echeance.getStatut().name());
        }
        return dto;
    }
}
