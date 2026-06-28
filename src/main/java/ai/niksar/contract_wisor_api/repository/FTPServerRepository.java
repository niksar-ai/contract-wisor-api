package ai.niksar.contract_wisor_api.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import ai.niksar.contract_wisor_api.dto.FTPServerDTO;
import ai.niksar.contract_wisor_api.model.FTPServer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface FTPServerRepository extends JpaRepository<FTPServer, UUID> {
    @Query("SELECT new ai.niksar.contract_wisor_api.dto.FTPServerDTO(f.id, f.server, f.serverName, f.port, f.ftpUser, f.password, f.lastConnectAt) FROM FTPServer f WHERE f.status = '1' AND f.username = :username ORDER BY f.lastConnectAt DESC")
    List<FTPServerDTO> findAllFTPServerByUsername(@PathVariable String username);

    @Transactional
    @Modifying
    @Query("UPDATE FTPServer f SET f.lastConnectAt = :lastConnectAt WHERE f.status = '1' AND f.server = :server AND f.port = :port AND f.username = :username")
    void findAndUpdateForLastConnectAt(@PathVariable LocalDateTime lastConnectAt, @PathVariable String server, @PathVariable String port, @PathVariable String username);
}