package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import ai.niksar.contract_wisor_api.model.SessionHistory;
import ai.niksar.contract_wisor_api.model.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface SessionHistoryRepository extends JpaRepository<SessionHistory, UUID> {

    List<SessionHistory> findByUser(User user);
    @Query("SELECT s FROM SessionHistory s WHERE s.sessionId = :sessionId")
    SessionHistory findBySessionId(@PathVariable UUID sessionId);

    List<SessionHistory> findByUserAndHistoryDate(User user, String historyDate);

    List<SessionHistory> findByUserAndLoginStatus(User user, String loginStatus);
}
