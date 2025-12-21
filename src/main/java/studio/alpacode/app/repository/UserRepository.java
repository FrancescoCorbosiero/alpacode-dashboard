package studio.alpacode.app.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import studio.alpacode.app.domain.Role;
import studio.alpacode.app.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByInviteToken(String inviteToken);

    @Query("SELECT * FROM users WHERE role = :role ORDER BY created_at DESC")
    List<User> findByRole(@Param("role") String role);

    @Query("SELECT COUNT(*) FROM users WHERE role = :role")
    long countByRole(@Param("role") String role);

    boolean existsByEmail(String email);

    default List<User> findAllClients() {
        return findByRole(Role.CLIENT.name());
    }

    default boolean existsAdmin() {
        return countByRole(Role.ADMIN.name()) > 0;
    }
}
