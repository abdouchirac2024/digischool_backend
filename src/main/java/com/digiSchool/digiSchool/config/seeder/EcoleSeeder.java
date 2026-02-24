package com.digiSchool.digiSchool.config.seeder;

import org.springframework.stereotype.Component;

import com.digiSchool.digiSchool.Exceptionconfig.model.Quartier;
import com.digiSchool.digiSchool.academic.organisation.model.Ecole;
import com.digiSchool.digiSchool.academic.organisation.repository.EcoleRepository;

import java.util.HashMap;
import java.util.Map;

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
                bastos);
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
                bonamoussadi);
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
                tamdja);
        ecolesMap.put("ECF-001", ecoleFranco);

        System.out.println("  -> Ecoles : 3 ecoles creees");
        System.out.println("     - Ecole Bilingue La Victoire (Yaounde)");
        System.out.println("     - Progressive Comprehensive College (Douala)");
        System.out.println("     - Groupe Scolaire Les Champions (Bafoussam)");
    }

    /**
     * Recupere une ecole par son code.
     * Tente d'abord dans la map, puis en base de données.
     */
    public Ecole getEcole(String codeEcole) {
        if (ecolesMap.containsKey(codeEcole)) {
            return ecolesMap.get(codeEcole);
        }

        // Fallback: Recherche en base si non présent dans la map
        return ecoleRepository.findByCodeEcole(codeEcole)
                .map(e -> {
                    ecolesMap.put(codeEcole, e);
                    return e;
                })
                .orElse(null);
    }

    private void loadExistingEcoles() {
        ecoleRepository.findAll().forEach(e -> ecolesMap.put(e.getCodeEcole(), e));
    }

    private Ecole createEcole(String codeEcole, String nom, String adresse,
            String telephone, String email, Boolean statut, Quartier quartier) {
        Ecole e = new Ecole();
        e.setCodeEcole(codeEcole);
        e.setNom(nom);
        e.setAdresse(adresse);
        e.setTelephone(telephone);
        e.setEmail(email);
        e.setStatut(statut);
        e.setQuartier(quartier);

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
}
