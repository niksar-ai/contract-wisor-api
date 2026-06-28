package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ai.niksar.contract_wisor_api.model.Menu;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MenuRepository extends JpaRepository<Menu, UUID> {

    @Query("SELECT m FROM Menu m WHERE m.parentMenu.id = :parentId")
    List<Menu> findAllByParentId(@Param("parentId") UUID parentId);

    @Query("SELECT m FROM Menu m WHERE m.parentMenu.id = :parentId AND m.menuOrder >= :menuOrder")
    List<Menu> findAllByParentIdAndMenuOrderGreaterThanEqual(@Param("parentId") UUID parentId, @Param("menuOrder") Integer menuOrder);

    @Query("SELECT m FROM Menu m WHERE m.menuOrder >= :menuOrder AND m.parentMenu IS NULL")
    List<Menu> findByMenuOrderGreaterThanEqual(@Param("menuOrder") Integer menuOrder);

    @Query("SELECT MAX(m.menuOrder) FROM Menu m WHERE m.parentMenu IS NULL")
    Integer findMaxMenuOrderWithoutParent();

    @Query("SELECT m FROM Menu m WHERE m.parentMenu.id = :parentId AND m.menuOrder >= :menuOrder")
    List<Menu> findBySubMenuOrderGreaterThanEqual(@Param("parentId") UUID parentId, @Param("menuOrder") Integer menuOrder);

    @Query("SELECT m FROM Menu m WHERE m.parentMenu.id = :parentId AND m.menuOrder > :menuOrder")
    List<Menu> findAllByParentIdAndMenuOrderGreaterThan(@Param("parentId") UUID parentId, @Param("menuOrder") Integer menuOrder);

    @Query("SELECT m FROM Menu m WHERE m.parentMenu.id = :parentId AND m.menuOrder < :menuOrder")
    List<Menu> findAllByParentIdAndMenuOrderLessThan(@Param("parentId") UUID parentId, @Param("menuOrder") Integer menuOrder);

    @Query("SELECT COUNT(m) FROM Menu m WHERE m.parentMenu.id = :parentId")
    int countByParentId(@Param("parentId") UUID parentId);
    @Query("SELECT m FROM Menu m WHERE m.parentMenu IS NULL ORDER BY m.menuOrder")
    List<Menu> findMenusByParentIdNullOrderByMenuOrder();
    @Query("SELECT m FROM Menu m WHERE m.parentMenu.id = :parentId ORDER BY m.menuOrder")
    List<Menu> findMenusByParentIdOrderByMenuOrder(@Param("parentId") UUID parentId);

    Optional<Menu> findByUrl(String url);
}
