package com.digiSchool.digiSchool.config.seeder;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.digiSchool.digiSchool.Exceptionconfig.model.Quartier;
import com.digiSchool.digiSchool.academic.organisation.model.Ecole;
import com.digiSchool.digiSchool.user.model.Parent;
import com.digiSchool.digiSchool.user.repository.ParentRepository;

/**
 * Seeder pour les parents d'eleves.
 * Cree des parents de demonstration pour chaque ecole.
 * Tenant = ecole.getTenant() → format CM-{REGION}-ECOLE-{ID}
 */
@Component
public class ParentSeeder {

    private final ParentRepository parentRepository;
    private final EcoleSeeder ecoleSeeder;
    private final RegionSeeder regionSeeder;

    private Map<String, Parent> parentsMap = new HashMap<>();

    public ParentSeeder(ParentRepository parentRepository,
                        EcoleSeeder ecoleSeeder,
                        RegionSeeder regionSeeder) {
        this.parentRepository = parentRepository;
        this.ecoleSeeder = ecoleSeeder;
        this.regionSeeder = regionSeeder;
    }

    public void seed() {
        if (parentRepository.count() > 0) {
            System.out.println("  -> Parents : deja presents, skip");
            loadExisting();
            return;
        }

        Ecole ecoleBilingue = ecoleSeeder.getEcole("ECB-001");
        Ecole ecoleAnglo = ecoleSeeder.getEcole("ECA-001");
        Ecole ecoleFranco = ecoleSeeder.getEcole("ECF-001");

        Quartier bastos = regionSeeder.getQuartier("QBS");
        Quartier nlongkak = regionSeeder.getQuartier("QNL");
        Quartier bonamoussadi = regionSeeder.getQuartier("QBN");
        Quartier tamdja = regionSeeder.getQuartier("QTD");
        Quartier essos = regionSeeder.getQuartier("QES");

        // Parents Ecole Bilingue La Victoire (Yaounde)
        createParent("PAR-ECB-001", "Nkoulou", "Francois", "fnkoulou@gmail.com",
                "+237691666777", "Bastos, Yaounde", bastos, "Ingenieur Informatique",
                "Orange Cameroun", ecoleBilingue);

        createParent("PAR-ECB-002", "Manga", "Beatrice", "bmanga@yahoo.fr",
                "+237682777888", "Nlongkak, Yaounde", nlongkak, "Medecin",
                "Hopital Central", ecoleBilingue);

        createParent("PAR-ECB-003", "Ateba", "Georges", "gateba@gmail.com",
                "+237677444555", "Essos, Yaounde", essos, "Comptable",
                "SABC", ecoleBilingue);

        // Parents Progressive College (Douala)
        createParent("PAR-ECA-001", "Ebogo", "Martin", "mebogo@gmail.com",
                "+237673888999", "Bonamoussadi, Douala", bonamoussadi, "Avocat",
                "Cabinet Ebogo & Associes", ecoleAnglo);

        createParent("PAR-ECA-002", "Njock", "Sylvie", "snjock@hotmail.com",
                "+237699123789", "Bonamoussadi, Douala", bonamoussadi, "Pharmacienne",
                "Pharmacie du Littoral", ecoleAnglo);

        // Parents Groupe Scolaire Les Champions (Bafoussam)
        createParent("PAR-ECF-001", "Wabo", "Claude", "cwabo@gmail.com",
                "+237655778899", "Tamdja, Bafoussam", tamdja, "Commercant",
                "Ets Wabo & Fils", ecoleFranco);

        createParent("PAR-ECF-002", "Tchinda", "Rose", "rtchinda@yahoo.fr",
                "+237699887766", "Tamdja, Bafoussam", tamdja, "Enseignante",
                "Lycee de Bafoussam", ecoleFranco);

        System.out.println("  -> Parents : 7 parents crees (tenant = ecole.getTenant())");
    }

    public Parent getParent(String matricule) {
        return parentsMap.get(matricule);
    }

    private void loadExisting() {
        parentRepository.findAll().forEach(p -> parentsMap.put(p.getMatriculeParent(), p));
    }

    private void createParent(String matricule, String nom, String prenom, String email,
                              String telephone, String adresse, Quartier quartier,
                              String profession, String employeur, Ecole ecole) {
        Parent parent = new Parent();
        parent.setMatriculeParent(matricule);
        parent.setNom(nom);
        parent.setPrenom(prenom);
        parent.setEmail(email);
        parent.setTelephone(telephone);
        parent.setAdresse(adresse);
        parent.setQuartier(quartier);
        parent.setProfession(profession);
        parent.setEmployeur(employeur);
        parent.setActif(true);
        parent.setTenant(ecole.getTenant());

        Parent saved = parentRepository.save(parent);
        parentsMap.put(matricule, saved);
    }
}
