package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ai.niksar.contract_wisor_api.model.User;
import ai.niksar.contract_wisor_api.model.UserRoleModel;
import ai.niksar.contract_wisor_api.model.UserProfile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>  , JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByStatus(String status);
    @Query("SELECT u FROM UserRoleModel u WHERE u.status = '1' ORDER BY u.name ASC ")
    List<UserRoleModel> listUsers();
    @Query("SELECT u FROM UserProfile u WHERE u.username = :username")
    UserProfile userProfileByUsername(@Param("username") String username);
    @Query("SELECT u FROM UserProfile u WHERE u.id = :id")
    UserProfile userProfileById(@Param("id") UUID id);
    @Query("SELECT u FROM User u WHERE u.status = '1' OR u.status = '0' ORDER BY u.status DESC, u.name ASC")
    List<User> findAllUsers();
}

