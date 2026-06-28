package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ai.niksar.contract_wisor_api.model.DocumentRelation;

import java.util.List;
import java.util.UUID;

public interface DocumentRelationRepository extends JpaRepository<DocumentRelation, UUID> , JpaSpecificationExecutor<DocumentRelation> {
    List<DocumentRelation> findByParentId(UUID parentId);
    @Query("SELECT dr FROM DocumentRelation dr WHERE dr.parentId = :parentId")
    Page<DocumentRelation> findByParentIdPageable(UUID parentId, Pageable pageable);
    List<DocumentRelation> findByChildId(UUID childId);
//    @Query("SELECT dr FROM DocumentRelation dr WHERE dr.parentId = :parentId AND dr.childId = :childId")
//    List<DocumentRelation> findByChildIdAndParentId(UUID parentId,UUID childId);
    DocumentRelation findByParentIdAndChild_Id(UUID parentId, UUID childId);
}

