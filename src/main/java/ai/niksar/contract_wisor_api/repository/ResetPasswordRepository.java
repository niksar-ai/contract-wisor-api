package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ai.niksar.contract_wisor_api.model.ResetPassword;

import java.util.UUID;

public interface ResetPasswordRepository extends JpaRepository<ResetPassword, UUID>{
}
