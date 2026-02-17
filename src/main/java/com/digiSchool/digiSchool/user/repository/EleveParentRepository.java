package com.digiSchool.digiSchool.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.user.model.EleveParent;
import com.digiSchool.digiSchool.user.model.TypeRelation;

@Repository
public interface EleveParentRepository extends JpaRepository<EleveParent, Long> {

    @Query("SELECT ep FROM EleveParent ep LEFT JOIN FETCH ep.eleve LEFT JOIN FETCH ep.parent")
    List<EleveParent> findAllWithRelations();

    List<EleveParent> findByEleveIdEleve(Long eleveId);

    List<EleveParent> findByParentIdParent(Long parentId);

    // Méthodes tenant-aware pour isolation multi-tenant
    @Query("SELECT ep FROM EleveParent ep WHERE ep.idEleveParent = :id AND ep.tenant = :tenant")
    Optional<EleveParent> findByIdAndTenant(@Param("id") Long id, @Param("tenant") String tenant);

    @Query("SELECT ep FROM EleveParent ep WHERE ep.eleve.idEleve = :eleveId AND ep.tenant = :tenant")
    List<EleveParent> findByEleveIdEleveAndTenant(@Param("eleveId") Long eleveId, @Param("tenant") String tenant);

    @Query("SELECT ep FROM EleveParent ep WHERE ep.parent.idParent = :parentId AND ep.tenant = :tenant")
    List<EleveParent> findByParentIdParentAndTenant(@Param("parentId") Long parentId, @Param("tenant") String tenant);

    @Query("SELECT ep FROM EleveParent ep WHERE ep.eleve.idEleve = :eleveId AND ep.autoriseUrgence = true AND ep.tenant = :tenant")
    List<EleveParent> findContactsUrgenceByEleveAndTenant(@Param("eleveId") Long eleveId, @Param("tenant") String tenant);

    @Query("SELECT ep FROM EleveParent ep WHERE ep.eleve.idEleve = :eleveId AND ep.autorisePriseEnCharge = true AND ep.tenant = :tenant")
    List<EleveParent> findAutorisesPriseEnChargeByEleveAndTenant(@Param("eleveId") Long eleveId, @Param("tenant") String tenant);

    @Query("SELECT ep FROM EleveParent ep WHERE ep.eleve.idEleve = :eleveId AND ep.estPrincipal = true AND ep.tenant = :tenant")
    Optional<EleveParent> findPrincipalByEleveAndTenant(@Param("eleveId") Long eleveId, @Param("tenant") String tenant);

    Optional<EleveParent> findByEleveIdEleveAndParentIdParentAndTypeRelation(
            Long eleveId, Long parentId, TypeRelation typeRelation);

    boolean existsByEleveIdEleveAndParentIdParentAndTypeRelation(
            Long eleveId, Long parentId, TypeRelation typeRelation);

    boolean existsByEleveIdEleveAndParentIdParent(Long eleveId, Long parentId);

    long countByEleveIdEleve(Long eleveId);

    long countByParentIdParent(Long parentId);

    @Query("SELECT ep FROM EleveParent ep WHERE ep.eleve.idEleve = :eleveId AND ep.estPrincipal = true")
    Optional<EleveParent> findPrincipalByEleve(@Param("eleveId") Long eleveId);

    @Query("SELECT ep FROM EleveParent ep WHERE ep.eleve.idEleve = :eleveId AND ep.autoriseUrgence = true")
    List<EleveParent> findContactsUrgenceByEleve(@Param("eleveId") Long eleveId);

    @Query("SELECT ep FROM EleveParent ep WHERE ep.eleve.idEleve = :eleveId AND ep.autorisePriseEnCharge = true")
    List<EleveParent> findAutorisesPriseEnChargeByEleve(@Param("eleveId") Long eleveId);

    @Query("SELECT ep FROM EleveParent ep WHERE ep.tenant = :tenant")
    List<EleveParent> findAllByTenant(@Param("tenant") String tenant);

    @Query("SELECT ep FROM EleveParent ep WHERE ep.eleve.idEleve = :eleveId ORDER BY ep.estPrincipal DESC, ep.createdAt ASC")
    List<EleveParent> findByEleveOrderByPrincipal(@Param("eleveId") Long eleveId);
}
