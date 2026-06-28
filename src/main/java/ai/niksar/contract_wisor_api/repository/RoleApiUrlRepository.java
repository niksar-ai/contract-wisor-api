package ai.niksar.contract_wisor_api.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ai.niksar.contract_wisor_api.model.RoleApiUrl;

import java.util.List;
import java.util.UUID;

public interface RoleApiUrlRepository extends JpaRepository<RoleApiUrl, UUID>{
    @Query("SELECT CONCAT(u.method, u.url) " +
            "FROM RoleApiUrl ra " +
            "JOIN ra.apiUrl u " +
            "WHERE ra.role.id = :roleId")
    List<String> findUrlsByRoleId(@Param("roleId") UUID roleId);

    @Query("SELECT u FROM RoleApiUrl u WHERE u.role.id = :roleId")
    List<RoleApiUrl> findByRoleId(UUID roleId);

    @Query("SELECT u.id " +
            "FROM RoleApiUrl ra " +
            "JOIN ra.apiUrl u " +
            "WHERE ra.role.id = :roleId")
    List<UUID> idListByRoleId(UUID roleId);

    @Modifying
    @Transactional
    @Query("DELETE FROM RoleApiUrl ra WHERE ra.apiUrl.id = :urlId")
    void deleteByUrlId(@Param("urlId") UUID urlId);

    @Query("SELECT DISTINCT u.code " +
            "FROM RoleApiUrl ra " +
            "JOIN ra.apiUrl u " +
            "WHERE ra.role.id IN :roleIds")
    List<String> findActionCodesWithRoleIdList(@Param("roleIds") List<UUID> roleIds);
}
