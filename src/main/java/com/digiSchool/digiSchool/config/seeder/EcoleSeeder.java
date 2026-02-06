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
 */
@Component
public class EcoleSeeder {

    private final EcoleRepository ecoleRepository;
    private final RegionSeeder regionSeeder;

    private static final String TENANT = "default";

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
            bastos
        );
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
            bonamoussadi
        );
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
            tamdja
        );
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
                               String telephone, String email, Boolean statut, Quartier quartier) {
        Ecole e = new Ecole();
        e.setCodeEcole(codeEcole);
        e.setNom(nom);
        e.setAdresse(adresse);
        e.setTelephone(telephone);
        e.setEmail(email);
        e.setStatut(statut);
        e.setQuartier(quartier);
        e.setTenant(TENANT);
        return ecoleRepository.save(e);
    }
}
