package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import ai.niksar.contract_wisor_api.dto.DocumentSortedDTO;
import ai.niksar.contract_wisor_api.model.DocumentFavourite;
import ai.niksar.contract_wisor_api.model.UserModel;

import java.util.List;
import java.util.UUID;


@Repository
public interface DocumentFavoriteRepository extends JpaRepository<DocumentFavourite, UUID> {

    @Query("SELECT d FROM DocumentFavourite d WHERE d.documentId = :documentId")
    List<DocumentFavourite> findByDocumentId(@PathVariable UUID documentId);
    @Query("SELECT d FROM DocumentFavourite d WHERE d.createUser = :username")
    List<DocumentFavourite> findByUsername(@PathVariable String username);
    @Query("SELECT d FROM DocumentFavourite d WHERE d.createUser = :username AND d.documentId = :documentId")
    DocumentFavourite find(@PathVariable String username,@PathVariable UUID documentId);
    @Query("SELECT new ai.niksar.contract_wisor_api.dto.DocumentSortedDTO(d.id, d.pageCount, d.name, d.size, d.state, d.createDate, d.createTime, d.updateDate, d.updateTime, d.status, d.documentFileType, d.createUser, d.updateUser, dm , dt) " +
            "FROM Document d JOIN DocumentFavourite df ON d.id = df.documentId " +
            "JOIN DocumentMetadata dm ON d.metadataId = dm.id " +
            "JOIN DocumentType dt ON d.documentTypeId = dt.id " +
            "WHERE df.createUser = :user ")
    List<DocumentSortedDTO> favDocumentList(@Param("user") String user);
    @Query("SELECT u FROM UserModel u JOIN DocumentFavourite df ON df.createUser = u.username " +
            " WHERE df.documentId = :documentId ")
    List<UserModel> userList(@Param("documentId") UUID documentId);
}
