package com.digiSchool.digiSchool.config.seeder;

import org.springframework.stereotype.Component;

import com.digiSchool.digiSchool.user.model.Eleve;
import com.digiSchool.digiSchool.user.model.EleveParent;
import com.digiSchool.digiSchool.user.model.Parent;
import com.digiSchool.digiSchool.user.model.TypeRelation;
import com.digiSchool.digiSchool.user.repository.EleveParentRepository;

/**
 * Seeder pour les relations eleve-parent.
 * Lie les eleves a leurs parents avec le bon tenant.
 * Tenant = eleve.getTenant() → format CM-{REGION}-ECOLE-{ID}
 */
@Component
public class EleveParentSeeder {

    private final EleveParentRepository eleveParentRepository;
    private final EleveSeeder eleveSeeder;
    private final ParentSeeder parentSeeder;

    public EleveParentSeeder(EleveParentRepository eleveParentRepository,
                             EleveSeeder eleveSeeder,
                             ParentSeeder parentSeeder) {
        this.eleveParentRepository = eleveParentRepository;
        this.eleveSeeder = eleveSeeder;
        this.parentSeeder = parentSeeder;
    }

    public void seed() {
        if (eleveParentRepository.count() > 0) {
            System.out.println("  -> Relations Eleve-Parent : deja presentes, skip");
            return;
        }

        // Ecole Bilingue - Famille Nkoulou
        createRelation("ELV-ECB-001", "PAR-ECB-001", TypeRelation.PERE, true);
        createRelation("ELV-ECB-002", "PAR-ECB-001", TypeRelation.PERE, true);

        // Ecole Bilingue - Famille Manga
        createRelation("ELV-ECB-003", "PAR-ECB-002", TypeRelation.MERE, true);
        createRelation("ELV-ECB-004", "PAR-ECB-002", TypeRelation.MERE, true);

        // Ecole Bilingue - Famille Ateba
        createRelation("ELV-ECB-005", "PAR-ECB-003", TypeRelation.PERE, true);

        // Progressive College - Famille Ebogo
        createRelation("ELV-ECA-001", "PAR-ECA-001", TypeRelation.PERE, true);
        createRelation("ELV-ECA-002", "PAR-ECA-001", TypeRelation.PERE, true);

        // Progressive College - Famille Njock
        createRelation("ELV-ECA-003", "PAR-ECA-002", TypeRelation.MERE, true);

        // Les Champions - Famille Wabo
        createRelation("ELV-ECF-001", "PAR-ECF-001", TypeRelation.PERE, true);
        createRelation("ELV-ECF-002", "PAR-ECF-001", TypeRelation.PERE, true);

        // Les Champions - Famille Tchinda
        createRelation("ELV-ECF-003", "PAR-ECF-002", TypeRelation.MERE, true);

        System.out.println("  -> Relations Eleve-Parent : 11 relations creees (tenant = ecole.getTenant())");
    }

    private void createRelation(String eleveMatricule, String parentMatricule,
                                TypeRelation typeRelation, boolean estPrincipal) {
        Eleve eleve = eleveSeeder.getEleve(eleveMatricule);
        Parent parent = parentSeeder.getParent(parentMatricule);

        if (eleve == null || parent == null) {
            System.out.println("  ⚠ Relation ignoree: eleve=" + eleveMatricule
                    + " parent=" + parentMatricule + " (introuvable)");
            return;
        }

        EleveParent ep = new EleveParent();
        ep.setEleve(eleve);
        ep.setParent(parent);
        ep.setTypeRelation(typeRelation);
        ep.setEstPrincipal(estPrincipal);
        ep.setAutorisePriseEnCharge(true);
        ep.setAutoriseUrgence(true);
        ep.setTenant(eleve.getTenant());

        eleveParentRepository.save(ep);
    }
}
