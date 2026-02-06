package com.digiSchool.digiSchool.config.seeder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.digiSchool.digiSchool.academic.organisation.model.Anneescolaire;
import com.digiSchool.digiSchool.academic.organisation.repository.AnneescolaireRepository;

/**
 * Seeder pour les annees scolaires.
 * Cree l'annee en cours et l'annee precedente.
 */
@Component
public class AnneeScolaireSeeder {

    private final AnneescolaireRepository anneescolaireRepository;

    private static final String TENANT = "default";

    // Stockage des annees pour les autres seeders
    private Map<String, Anneescolaire> anneesMap = new HashMap<>();

    public AnneeScolaireSeeder(AnneescolaireRepository anneescolaireRepository) {
        this.anneescolaireRepository = anneescolaireRepository;
    }

    /**
     * Cree les annees scolaires
     */
    public void seed() {
        if (anneescolaireRepository.count() > 0) {
            System.out.println("  -> Annees scolaires : deja presentes, skip");
            loadExistingAnnees();
            return;
        }

        // Annee 2024-2025 (archivee)
        Anneescolaire annee2024 = createAnneeScolaire(
            "2024-2025",
            LocalDate.of(2024, 9, 2),
            LocalDate.of(2025, 6, 30),
            false // archivee
        );
        anneesMap.put("2024-2025", annee2024);

        // Annee 2025-2026 (active)
        Anneescolaire annee2025 = createAnneeScolaire(
            "2025-2026",
            LocalDate.of(2025, 9, 1),
            LocalDate.of(2026, 6, 30),
            true // active
        );
        anneesMap.put("2025-2026", annee2025);

        System.out.println("  -> Annees scolaires : 2 annees creees");
        System.out.println("     - 2024-2025 (archivee)");
        System.out.println("     - 2025-2026 (active)");
    }

    /**
     * Recupere une annee scolaire par son libelle
     */
    public Anneescolaire getAnnee(String libelle) {
        return anneesMap.get(libelle);
    }

    private void loadExistingAnnees() {
        anneescolaireRepository.findAll().forEach(a -> anneesMap.put(a.getLibelle(), a));
    }

    private Anneescolaire createAnneeScolaire(String libelle, LocalDate debut, LocalDate fin, Boolean statut) {
        Anneescolaire a = new Anneescolaire();
        a.setLibelle(libelle);
        a.setDateDebut(debut);
        a.setDateFin(fin);
        a.setStatut(statut);
        a.setTenant(TENANT);
        return anneescolaireRepository.save(a);
    }
}
