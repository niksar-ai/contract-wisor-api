package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ai.niksar.contract_wisor_api.model.BlockedUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface BlockedUserRepository extends JpaRepository<BlockedUser, UUID> {

    List<BlockedUser> findByUserIdAndStatus(UUID userId, String status);

    List<BlockedUser> findAllByStatusAndUnblockedAtBefore(String status, LocalDateTime unblockedAt);


}
