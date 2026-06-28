package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ai.niksar.contract_wisor_api.model.ApiUrl;

import java.util.List;
import java.util.UUID;

public interface ApiUrlRepository extends JpaRepository<ApiUrl, UUID>{
    @Query("SELECT CONCAT(u.method, u.url) FROM ApiUrl u")
    List<String> urlPathList();
    @Query("SELECT u FROM ApiUrl u ORDER BY u.url ASC")
    List<ApiUrl> urlListAsc();
    boolean existsByMethodAndUrlAndController(String method, String url,String controller);
    ApiUrl findByController(String controller);
    ApiUrl findByUrlAndMethod(String url,String method);

    @Query("SELECT a FROM ApiUrl a WHERE " +
            "(a.url = :url AND a.method = :method) " +
            "OR (a.code = :code AND :code IS NOT NULL) " +
            "OR a.controller = :controller")
    ApiUrl findByUrlMethodOrCodeOrController(@Param("url") String url,
                                                   @Param("method") String method,
                                                   @Param("code") String code,
                                                   @Param("controller") String controller);
}
