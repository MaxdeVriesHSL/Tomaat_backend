package tomaat.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tomaat.model.Role;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(String name);

    @Query(value = "SELECT r.name FROM role r JOIN users u ON r.id = u.role_id WHERE u.id = :userId", nativeQuery = true)
    Optional<String> findRoleNameByUserId(@Param("userId") UUID userId);
}
