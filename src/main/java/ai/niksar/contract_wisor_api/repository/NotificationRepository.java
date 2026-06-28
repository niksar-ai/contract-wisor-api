package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ai.niksar.contract_wisor_api.model.Notification;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID>{
}
