package com.digiSchool.digiSchool.config.seeder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.digiSchool.digiSchool.Exceptionconfig.model.Quartier;
import com.digiSchool.digiSchool.academic.organisation.model.Ecole;
import com.digiSchool.digiSchool.user.model.Eleve;
import com.digiSchool.digiSchool.user.model.Sexe;
import com.digiSchool.digiSchool.user.model.StatutEleve;
import com.digiSchool.digiSchool.user.repository.EleveRepository;

/**
 * Seeder pour les eleves.
 * Cree des eleves de demonstration pour chaque ecole.
 * Tenant = ecole.getTenant() → format CM-{REGION}-ECOLE-{ID}
 */
@Component
public class EleveSeeder {

        private final EleveRepository eleveRepository;
        private final EcoleSeeder ecoleSeeder;
        private final RegionSeeder regionSeeder;

        private Map<String, Eleve> elevesMap = new HashMap<>();

        public EleveSeeder(EleveRepository eleveRepository,
                        EcoleSeeder ecoleSeeder,
                        RegionSeeder regionSeeder) {
                this.eleveRepository = eleveRepository;
                this.ecoleSeeder = ecoleSeeder;
                this.regionSeeder = regionSeeder;
        }

        public void seed() {
                if (eleveRepository.count() > 0) {
                        System.out.println("  -> Eleves : deja presents, skip");
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

                // Eleves Ecole Bilingue La Victoire (Yaounde)
                createEleve("ELV-ECB-2025-0001", "Nkoulou", "Paul", LocalDate.of(2014, 3, 15),
                                "Yaounde", Sexe.M, bastos, ecoleBilingue);
                createEleve("ELV-ECB-2025-0002", "Nkoulou", "Marie", LocalDate.of(2016, 7, 22),
                                "Yaounde", Sexe.F, bastos, ecoleBilingue);
                createEleve("ELV-ECB-2025-0003", "Manga", "Thierry", LocalDate.of(2013, 11, 5),
                                "Yaounde", Sexe.M, nlongkak, ecoleBilingue);
                createEleve("ELV-ECB-2025-0004", "Manga", "Sarah", LocalDate.of(2015, 1, 18),
                                "Yaounde", Sexe.F, nlongkak, ecoleBilingue);
                createEleve("ELV-ECB-2025-0005", "Ateba", "Junior", LocalDate.of(2014, 9, 30),
                                "Yaounde", Sexe.M, bastos, ecoleBilingue);

                // Eleves Progressive College (Douala)
                createEleve("ELV-ECA-2025-0001", "Ebogo", "Samuel", LocalDate.of(2012, 8, 20),
                                "Douala", Sexe.M, bonamoussadi, ecoleAnglo);
                createEleve("ELV-ECA-2025-0002", "Ebogo", "Grace", LocalDate.of(2014, 4, 12),
                                "Douala", Sexe.F, bonamoussadi, ecoleAnglo);
                createEleve("ELV-ECA-2025-0003", "Njock", "Kevin", LocalDate.of(2013, 6, 8),
                                "Douala", Sexe.M, bonamoussadi, ecoleAnglo);

                // Eleves Groupe Scolaire Les Champions (Bafoussam)
                createEleve("ELV-ECF-2025-0001", "Wabo", "Rodrigue", LocalDate.of(2006, 11, 10),
                                "Bafoussam", Sexe.M, tamdja, ecoleFranco);
                createEleve("ELV-ECF-2025-0002", "Wabo", "Patricia", LocalDate.of(2008, 2, 25),
                                "Bafoussam", Sexe.F, tamdja, ecoleFranco);
                createEleve("ELV-ECF-2025-0003", "Tchinda", "Boris", LocalDate.of(2007, 5, 14),
                                "Bafoussam", Sexe.M, tamdja, ecoleFranco);

                System.out.println("  -> Eleves : 11 eleves crees (tenant = ecole.getTenant())");
        }

        public Eleve getEleve(String matricule) {
                return elevesMap.get(matricule);
        }

        private void loadExisting() {
                eleveRepository.findAll().forEach(e -> elevesMap.put(e.getMatricule(), e));
        }

        private void createEleve(String matricule, String nom, String prenom, LocalDate dateNaissance,
                        String lieuNaissance, Sexe sexe, Quartier quartier, Ecole ecole) {
                Eleve eleve = new Eleve();
                eleve.setMatricule(matricule);
                eleve.setNom(nom);
                eleve.setPrenom(prenom);
                eleve.setDateNaissance(dateNaissance);
                eleve.setLieuNaissance(lieuNaissance);
                eleve.setSexe(sexe);
                eleve.setStatut(StatutEleve.ACTIF);
                eleve.setQuartier(quartier);
                eleve.setTenant(ecole.getTenant());

                Eleve saved = eleveRepository.save(eleve);
                elevesMap.put(matricule, saved);
        }
}
