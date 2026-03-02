package com.digiSchool.digiSchool.config.seeder;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.digiSchool.digiSchool.Exceptionconfig.model.Quartier;
import com.digiSchool.digiSchool.academic.organisation.model.Ecole;
import com.digiSchool.digiSchool.academic.organisation.model.SousSysteme;
import com.digiSchool.digiSchool.academic.organisation.model.StatutEcole;
import com.digiSchool.digiSchool.academic.organisation.model.TypeEtablissement;
import com.digiSchool.digiSchool.academic.organisation.model.TypeSecteur;
import com.digiSchool.digiSchool.academic.organisation.repository.EcoleRepository;

/**
 * Seeder pour les ecoles.
 * Cree 3 ecoles de demonstration dans differentes villes.
 * 
 * NOMENCLATURE TENANT ID (Format Hierarchique):
 * Format: CM-{REGION}-ECOLE-{ID}
 * Exemples:
 * - "CM-CENTRE-ECOLE-001" (Yaoundé, Région du Centre)
 * - "CM-LITTORAL-ECOLE-002" (Douala, Région du Littoral)
 * - "CM-OUEST-ECOLE-003" (Bafoussam, Région de l'Ouest)
 */
@Component
public class EcoleSeeder {

    private final EcoleRepository ecoleRepository;
    private final RegionSeeder regionSeeder;

    // Stockage des ecoles pour les autres seeders
    private Map<String, Ecole> ecolesMap = new HashMap<>();

    public EcoleSeeder(EcoleRepository ecoleRepository, RegionSeeder regionSeeder) {
        this.ecoleRepository = ecoleRepository;
        this.regionSeeder = regionSeeder;
    }

    /**
     * Cree les ecoles de demonstration
     */
    public void seed() {
        if (ecoleRepository.count() > 0) {
            System.out.println("  -> Ecoles : deja presentes, skip");
            loadExistingEcoles();
            return;
        }

        // Ecole Bilingue a Yaounde (Bastos)
        Quartier bastos = regionSeeder.getQuartier("QBS");
        Ecole ecoleBilingue = createEcole(
                "ECB-001",
                "Ecole Bilingue La Victoire",
                "BP 1234, Yaounde",
                "+237 677 123 456",
                "contact@lavictoire.cm",
                true,
                bastos,
                TypeSecteur.PRIVE_LAIC,
                TypeEtablissement.COMPLEXE_SCOLAIRE,
                SousSysteme.BILINGUE);
        ecolesMap.put("ECB-001", ecoleBilingue);

        // Ecole Anglophone a Douala (Bonamoussadi)
        Quartier bonamoussadi = regionSeeder.getQuartier("QBN");
        Ecole ecoleAnglo = createEcole(
                "ECA-001",
                "Progressive Comprehensive College",
                "BP 5678, Douala",
                "+237 699 987 654",
                "info@progressive.cm",
                true,
                bonamoussadi,
                TypeSecteur.PRIVE_CONFESSIONNEL,
                TypeEtablissement.SECONDAIRE_GENERAL,
                SousSysteme.ANGLOPHONE);
        ecolesMap.put("ECA-001", ecoleAnglo);

        // Ecole Francophone a Bafoussam (Tamdja)
        Quartier tamdja = regionSeeder.getQuartier("QTD");
        Ecole ecoleFranco = createEcole(
                "ECF-001",
                "Groupe Scolaire Les Champions",
                "BP 9012, Bafoussam",
                "+237 655 456 789",
                "direction@leschampions.cm",
                true,
                tamdja,
                TypeSecteur.PUBLIC,
                TypeEtablissement.PRIMAIRE,
                SousSysteme.FRANCOPHONE);
        ecolesMap.put("ECF-001", ecoleFranco);

        System.out.println("  -> Ecoles : 3 ecoles creees");
        System.out.println("     - Ecole Bilingue La Victoire (Yaounde)");
        System.out.println("     - Progressive Comprehensive College (Douala)");
        System.out.println("     - Groupe Scolaire Les Champions (Bafoussam)");
    }

    /**
     * Recupere une ecole par son code
     */
    public Ecole getEcole(String codeEcole) {
        return ecolesMap.get(codeEcole);
    }

    private void loadExistingEcoles() {
        ecoleRepository.findAll().forEach(e -> ecolesMap.put(e.getCodeEcole(), e));
    }

    private Ecole createEcole(String codeEcole, String nom, String adresse,
            String telephone, String email, Boolean statut, Quartier quartier,
            TypeSecteur typeSecteur, TypeEtablissement typeEtablissement, SousSysteme sousSysteme) {
        Ecole e = new Ecole();
        e.setCodeEcole(codeEcole);
        e.setNom(nom);
        e.setAdresse(adresse);
        e.setTelephone(telephone);
        e.setEmail(email);
        e.setQuartier(quartier);
        e.setTypeSecteur(typeSecteur);
        e.setTypeEtablissement(typeEtablissement);
        e.setSousSysteme(sousSysteme);
        e.setStatutEcole(StatutEcole.VALIDEE);
        e.setSlug(generateSlug(nom));
        e.setCouleurPrimaire("#2302B3");
        e.setCouleurSecondaire("#4318FF");
        e.setDateValidation(LocalDateTime.now());

        // Set a temporary tenant to satisfy NOT NULL constraint, will be updated after
        // save
        e.setTenant("TEMP");
        Ecole saved = ecoleRepository.save(e);

        // Generate hierarchical tenant ID: CM-{REGION}-ECOLE-{ID}
        String tenantId = generateTenantId(nom, saved.getIdEcole(), quartier);
        saved.setTenant(tenantId);

        // Save again with the tenant ID
        return ecoleRepository.save(saved);
    }

    /**
     * Generate a professional hierarchical tenant ID.
     * Format: CM-{REGION}-ECOLE-{ID}
     * Examples:
     * - "CM-CENTRE-ECOLE-001" (Yaoundé)
     * - "CM-LITTORAL-ECOLE-002" (Douala)
     * - "CM-OUEST-ECOLE-003" (Bafoussam)
     */
    private String generateTenantId(String nom, Long id, Quartier quartier) {
        // Extract region from quartier hierarchy
        // Hierarchy: Quartier → Ville → Arrondissement → Departement → Region
        String region = "UNKNOWN";
        if (quartier != null &&
                quartier.getVille() != null &&
                quartier.getVille().getArrondissement() != null &&
                quartier.getVille().getArrondissement().getDepartement() != null &&
                quartier.getVille().getArrondissement().getDepartement().getRegion() != null) {
            region = quartier.getVille()
                    .getArrondissement()
                    .getDepartement()
                    .getRegion()
                    .getNom()
                    .toUpperCase()
                    .replace(" ", "-")
                    .replace("É", "E")
                    .replace("È", "E")
                    .replace("Ê", "E")
                    .replace("À", "A")
                    .replace("Ô", "O");
        }

        // Format: CM-{REGION}-ECOLE-{ID with 3-digit padding}
        String paddedId = String.format("%03d", id);
        return "CM-" + region + "-ECOLE-" + paddedId;
    }

    /**
     * Generate a URL-friendly slug from a school name.
     * Lowercases, replaces accents and spaces with hyphens, removes
     * non-alphanumeric chars.
     */
    private String generateSlug(String nom) {
        if (nom == null)
            return "";
        // Normalize accents
        String normalized = Normalizer.normalize(nom, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        // Lowercase
        normalized = normalized.toLowerCase();
        // Replace spaces and special chars with hyphens
        normalized = normalized.replaceAll("[^a-z0-9]", "-");
        // Remove consecutive hyphens
        normalized = normalized.replaceAll("-+", "-");
        // Remove leading/trailing hyphens
        normalized = normalized.replaceAll("^-|-$", "");
        return normalized;
    }
}
