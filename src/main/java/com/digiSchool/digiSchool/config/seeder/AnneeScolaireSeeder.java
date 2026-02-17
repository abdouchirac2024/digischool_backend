package com.digiSchool.digiSchool.config.seeder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.digiSchool.digiSchool.academic.organisation.model.Anneescolaire;
import com.digiSchool.digiSchool.academic.organisation.model.Ecole;
import com.digiSchool.digiSchool.academic.organisation.repository.AnneescolaireRepository;

/**
 * Seeder pour les annees scolaires.
 * Cree les annees pour chaque ecole (tenant = ecole.getTenant()).
 */
@Component
public class AnneeScolaireSeeder {

    private final AnneescolaireRepository anneescolaireRepository;
    private final EcoleSeeder ecoleSeeder;

    // Stockage des annees pour les autres seeders (cle = "libelle:codeEcole")
    private Map<String, Anneescolaire> anneesMap = new HashMap<>();

    public AnneeScolaireSeeder(AnneescolaireRepository anneescolaireRepository, EcoleSeeder ecoleSeeder) {
        this.anneescolaireRepository = anneescolaireRepository;
        this.ecoleSeeder = ecoleSeeder;
    }

    /**
     * Cree les annees scolaires pour chaque ecole
     */
    public void seed() {
        if (anneescolaireRepository.count() > 0) {
            System.out.println("  -> Annees scolaires : deja presentes, skip");
            loadExistingAnnees();
            return;
        }

        String[] codeEcoles = {"ECB-001", "ECA-001", "ECF-001"};

        for (String codeEcole : codeEcoles) {
            Ecole ecole = ecoleSeeder.getEcole(codeEcole);

            // Annee 2024-2025 (archivee)
            Anneescolaire annee2024 = createAnneeScolaire(
                "2024-2025",
                LocalDate.of(2024, 9, 2),
                LocalDate.of(2025, 6, 30),
                false,
                ecole.getTenant()
            );
            anneesMap.put("2024-2025:" + codeEcole, annee2024);

            // Annee 2025-2026 (active)
            Anneescolaire annee2025 = createAnneeScolaire(
                "2025-2026",
                LocalDate.of(2025, 9, 1),
                LocalDate.of(2026, 6, 30),
                true,
                ecole.getTenant()
            );
            anneesMap.put("2025-2026:" + codeEcole, annee2025);
        }

        System.out.println("  -> Annees scolaires : 6 annees creees (2 par ecole)");
        System.out.println("     - 2024-2025 (archivee) x3 ecoles");
        System.out.println("     - 2025-2026 (active) x3 ecoles");
    }

    /**
     * Recupere une annee scolaire par son libelle et le code ecole
     */
    public Anneescolaire getAnnee(String libelle, String codeEcole) {
        return anneesMap.get(libelle + ":" + codeEcole);
    }

    private void loadExistingAnnees() {
        anneescolaireRepository.findAll().forEach(a -> {
            // Reconstruct the key from tenant → codeEcole mapping
            String tenant = a.getTenant();
            String codeEcole = findCodeEcoleByTenant(tenant);
            if (codeEcole != null) {
                anneesMap.put(a.getLibelle() + ":" + codeEcole, a);
            }
        });
    }

    private String findCodeEcoleByTenant(String tenant) {
        for (String code : new String[]{"ECB-001", "ECA-001", "ECF-001"}) {
            Ecole ecole = ecoleSeeder.getEcole(code);
            if (ecole != null && tenant.equals(ecole.getTenant())) {
                return code;
            }
        }
        return null;
    }

    private Anneescolaire createAnneeScolaire(String libelle, LocalDate debut, LocalDate fin, Boolean statut, String tenant) {
        Anneescolaire a = new Anneescolaire();
        a.setLibelle(libelle);
        a.setDateDebut(debut);
        a.setDateFin(fin);
        a.setStatut(statut);
        a.setTenant(tenant);
        return anneescolaireRepository.save(a);
    }
}
