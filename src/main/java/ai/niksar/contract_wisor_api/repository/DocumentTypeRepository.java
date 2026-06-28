package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ai.niksar.contract_wisor_api.dto.DocumentTypeCountDTO;
import ai.niksar.contract_wisor_api.dto.DocumentTypeSizeDTO;
import ai.niksar.contract_wisor_api.model.DocumentType;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;


public interface DocumentTypeRepository extends JpaRepository<DocumentType, UUID> {
    List<DocumentType> findByParentId(UUID parentId);
    @Query("SELECT new ai.niksar.contract_wisor_api.dto.DocumentTypeCountDTO(dt.id, dt.name, COUNT(d.id)) " +
            "FROM Document d JOIN d.documentType dt " +
            "GROUP BY dt.id " +
            "ORDER BY COUNT(d.id) DESC")
    List<DocumentTypeCountDTO> findDocumentTypesByDocCount(Pageable pageable);
    @Query("SELECT new ai.niksar.contract_wisor_api.dto.DocumentTypeSizeDTO(dt.id, dt.name, SUM(d.size)) " +
            "FROM Document d JOIN DocumentType dt ON d.documentTypeId = dt.id " +
            "GROUP BY dt.id " +
            "ORDER BY SUM(d.size) DESC")
    List<DocumentTypeSizeDTO> findDocumentTypeWithTotalSize(Pageable pageable);
    @Query("SELECT SUM(d.size)" +
            "FROM Document d JOIN d.documentType dt " +
            "WHERE dt.id NOT IN :idList ")
    BigInteger otherDocumentsOfSize(@Param("idList") List<UUID> idList);
    @Query("SELECT COUNT(d.size)" +
            "FROM Document d JOIN d.documentType dt " +
            "WHERE dt.id NOT IN :idList ")
    Long otherDocumentsOfCount(@Param("idList") List<UUID> idList);
}