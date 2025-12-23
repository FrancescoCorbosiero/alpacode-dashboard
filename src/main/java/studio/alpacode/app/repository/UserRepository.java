package studio.alpacode.app.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import studio.alpacode.app.domain.Role;
import studio.alpacode.app.domain.User;

import java.util.List;
import java.util.Optional;

/**
 * Repository for User entity operations.
 * Extends PagingAndSortingRepository for production-ready pagination support.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long>, PagingAndSortingRepository<User, Long> {

    /**
     * Find user by email address.
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by invite token.
     */
    Optional<User> findByInviteToken(String inviteToken);

    /**
     * Check if email is already registered.
     */
    boolean existsByEmail(String email);

    /**
     * Find all users with given role.
     */
    @Query("SELECT * FROM app_user WHERE role = :role ORDER BY created_at DESC")
    List<User> findByRole(@Param("role") String role);

    /**
     * Find users with given role (paginated).
     */
    @Query("SELECT * FROM app_user WHERE role = :role ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
    List<User> findByRolePaginated(@Param("role") String role, @Param("limit") int limit, @Param("offset") int offset);

    /**
     * Count users by role.
     */
    @Query("SELECT COUNT(*) FROM app_user WHERE role = :role")
    long countByRole(@Param("role") String role);

    /**
     * Count users by role and status.
     */
    @Query("SELECT COUNT(*) FROM app_user WHERE role = :role AND status = :status")
    long countByRoleAndStatus(@Param("role") String role, @Param("status") String status);

    /**
     * Find all clients.
     */
    default List<User> findAllClients() {
        return findByRole(Role.CUSTOMER.name());
    }

    /**
     * Check if any admin exists.
     */
    default boolean existsAdmin() {
        return countByRole(Role.ADMIN.name()) > 0;
    }
}
