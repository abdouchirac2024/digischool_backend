package com.digiSchool.digiSchool.user.service;

import org.springframework.stereotype.Service;
import com.digiSchool.digiSchool.academic.organisation.model.Ecole;
import com.digiSchool.digiSchool.user.repository.EleveRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IdentifierGeneratorService {

    private final EleveRepository eleveRepository;

    /**
     * Génère un matricule élève au format ELV-{CODE_ECOLE}-{ANNEE}-{SEQ}
     * Exemple: ELV-ECB-2025-0001
     */
    public String generateEleveMatricule(Ecole ecole, String annee) {
        String codeEcole = ecole.getCodeEcole();
        // Nettoyer l'année si c'est format 2024/2025 -> 2024
        String anneeRef = annee.contains("/") ? annee.split("/")[0] : annee;

        String prefix = String.format("ELV-%s-%s-", codeEcole, anneeRef);

        Integer maxNumber = eleveRepository.findMaxMatriculeNumber(ecole.getTenant(), prefix);
        int nextNumber = (maxNumber != null ? maxNumber : 0) + 1;

        return String.format("%s%04d", prefix, nextNumber);
    }

    /**
     * Génère un identifiant parent au format PAR-{CODE_ECOLE}-{TEL}
     * Exemple: PAR-ECB-677123456
     */
    public String generateParentMatricule(Ecole ecole, String telephone) {
        String codeEcole = ecole.getCodeEcole();
        // Nettoyer le téléphone (enlever les espaces, tirets, etc. et garder les 9
        // derniers chiffres)
        String cleanTel = telephone.replaceAll("[^0-9]", "");
        if (cleanTel.length() > 9) {
            cleanTel = cleanTel.substring(cleanTel.length() - 9);
        }

        return String.format("PAR-%s-%s", codeEcole, cleanTel);
    }
}
