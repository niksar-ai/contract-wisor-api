package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ai.niksar.contract_wisor_api.model.RoleMenu;
import java.util.UUID;
import java.util.List;

@Repository
public interface RoleMenuRepository extends JpaRepository<RoleMenu, UUID> {
    List<RoleMenu> findByRoleId(UUID roleId);
    List<RoleMenu> findByMenuId(UUID menuId);
    boolean existsByRoleIdAndMenuId(UUID roleId, UUID menuId);
    void deleteByRoleId(UUID roleId);

}
