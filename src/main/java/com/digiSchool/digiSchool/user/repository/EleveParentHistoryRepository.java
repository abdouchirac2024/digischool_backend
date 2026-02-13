package com.digiSchool.digiSchool.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.user.model.EleveParentHistory;

@Repository
public interface EleveParentHistoryRepository extends JpaRepository<EleveParentHistory, Long> {

    List<EleveParentHistory> findByEleveParentIdEleveParentOrderByDateModificationDesc(Long eleveParentId);

    @Query("SELECT h FROM EleveParentHistory h WHERE h.eleveId = :eleveId ORDER BY h.dateModification DESC")
    List<EleveParentHistory> findByEleveIdOrderByDateModificationDesc(@Param("eleveId") Long eleveId);

    @Query("SELECT h FROM EleveParentHistory h WHERE h.parentId = :parentId ORDER BY h.dateModification DESC")
    List<EleveParentHistory> findByParentIdOrderByDateModificationDesc(@Param("parentId") Long parentId);

    List<EleveParentHistory> findByAction(String action);

    @Query("SELECT h FROM EleveParentHistory h WHERE h.tenant = :tenant ORDER BY h.dateModification DESC")
    List<EleveParentHistory> findAllByTenantOrderByDateModificationDesc(@Param("tenant") String tenant);
}
