package com.digiSchool.digiSchool.academic.organisation.service;

import java.util.List;

import com.digiSchool.digiSchool.academic.organisation.dto.ClasseDto;

public interface ClasseService {

    /**
     * Créer une classe (ancien - pour rétrocompatibilité)
     * @deprecated Utiliser create(dto, ecoleIdOverride) avec le contexte utilisateur
     */
    @Deprecated
    ClasseDto create(ClasseDto dto);

    /**
     * Créer une classe avec ecoleId depuis le contexte utilisateur.
     * @param dto Les données de la classe
     * @param ecoleIdOverride L'ecoleId de l'utilisateur (depuis JWT/header), peut être null en mode dev
     */
    ClasseDto create(ClasseDto dto, Long ecoleIdOverride);

    /**
     * Modifier une classe (ancien - pour rétrocompatibilité)
     * @deprecated Utiliser update(id, dto, ecoleIdOverride) avec le contexte utilisateur
     */
    @Deprecated
    ClasseDto update(Long id, ClasseDto dto);

    /**
     * Modifier une classe avec validation d'accès.
     * @param id L'ID de la classe
     * @param dto Les nouvelles données
     * @param userEcoleId L'ecoleId de l'utilisateur pour validation d'accès
     */
    ClasseDto update(Long id, ClasseDto dto, Long userEcoleId);

    ClasseDto getById(Long id);

    List<ClasseDto> getAll();

    List<ClasseDto> getByEcoleId(Long ecoleId);

    /**
     * Supprimer une classe (ancien - pour rétrocompatibilité)
     * @deprecated Utiliser delete(id, userEcoleId) avec le contexte utilisateur
     */
    @Deprecated
    void delete(Long id);

    /**
     * Supprimer une classe avec validation d'accès.
     * @param id L'ID de la classe
     * @param userEcoleId L'ecoleId de l'utilisateur pour validation d'accès
     */
    void delete(Long id, Long userEcoleId);

    /**
     * Vérifie si la classe peut encore accepter des élèves.
     * Lance une exception si la classe est inactive/archivée ou si la capacité est atteinte.
     */
    void verifierCapacite(Long classeId);
}
