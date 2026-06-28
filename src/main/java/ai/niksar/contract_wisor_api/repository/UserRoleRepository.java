package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ai.niksar.contract_wisor_api.model.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
    @Query("SELECT u FROM UserRole u WHERE u.user.id = :userId AND (u.expiryDate IS NULL OR u.expiryDate >= :time) ")
    List<UserRole> findByUserId(UUID userId,LocalDateTime time);
    @Query("SELECT u FROM UserRole u WHERE u.role.id = :roleId")
    List<UserRole> findByRoleId(UUID roleId);
    @Query("SELECT u FROM UserRole u WHERE u.role.id = :roleId AND (u.expiryDate IS NULL OR u.expiryDate >= :time) ")
    List<UserRole> findByRoleIdWithDate(UUID roleId,LocalDateTime time);
    @Query("SELECT ur.role.roleName " +
            "FROM UserRole ur " +
            "WHERE ur.user.id = :userId " +
            "AND (ur.expiryDate IS NULL OR ur.expiryDate >= :time)")
    List<String> roleNameListByUserIdAndTime(UUID userId, LocalDateTime time);
    void deleteByRoleId(UUID roleId);

    @Query("SELECT ur.role.id " +
            "FROM UserRole ur " +
            "WHERE ur.user.id = :userId " +
            "AND (ur.expiryDate IS NULL OR ur.expiryDate >= :time)")
    List<UUID> roleIdListByUserIdAndTime(UUID userId, LocalDateTime time);
    @Query("SELECT u FROM UserRole u WHERE u.role.id = :roleId AND u.user.id = :userId ")
    UserRole findByRoleIdAndUserId(UUID roleId,UUID userId);
    @Query("SELECT CASE WHEN COUNT(ur) > 0 THEN true ELSE false END " +
            "FROM UserRole ur " +
            "WHERE ur.user.id = :userId " +
            "AND (ur.expiryDate IS NULL OR ur.expiryDate >= :time) " +
            "AND ur.role.isAdmin = true")
    Boolean isAdminUser(UUID userId, LocalDateTime time);
}
