package studio.alpacode.app.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import studio.alpacode.app.domain.Invoice;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository for Invoice entity operations.
 * Uses database-agnostic SQL compatible with PostgreSQL and H2 (PostgreSQL mode).
 */
@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, Long>, PagingAndSortingRepository<Invoice, Long> {

    /**
     * Find all invoices for a user, ordered by date descending.
     */
    @Query("SELECT * FROM app_invoice WHERE user_id = :userId ORDER BY invoice_date DESC")
    List<Invoice> findByUserId(@Param("userId") Long userId);

    /**
     * Find invoices for a user with pagination.
     */
    @Query("SELECT * FROM app_invoice WHERE user_id = :userId ORDER BY invoice_date DESC LIMIT :limit OFFSET :offset")
    List<Invoice> findByUserIdPaginated(@Param("userId") Long userId, @Param("limit") int limit, @Param("offset") int offset);

    /**
     * Find invoices by user and status.
     */
    @Query("SELECT * FROM app_invoice WHERE user_id = :userId AND status = :status ORDER BY invoice_date DESC")
    List<Invoice> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

    /**
     * Find invoices by user and year.
     */
    @Query("SELECT * FROM app_invoice WHERE user_id = :userId AND EXTRACT(YEAR FROM invoice_date) = :year ORDER BY invoice_date DESC")
    List<Invoice> findByUserIdAndYear(@Param("userId") Long userId, @Param("year") int year);

    /**
     * Find invoices by user, year, and status.
     */
    @Query("SELECT * FROM app_invoice WHERE user_id = :userId AND EXTRACT(YEAR FROM invoice_date) = :year AND status = :status ORDER BY invoice_date DESC")
    List<Invoice> findByUserIdAndYearAndStatus(@Param("userId") Long userId, @Param("year") int year, @Param("status") String status);

    /**
     * Get distinct years for user's invoices (for filter dropdown).
     */
    @Query("SELECT DISTINCT EXTRACT(YEAR FROM invoice_date) AS year FROM app_invoice WHERE user_id = :userId ORDER BY year DESC")
    List<Integer> findDistinctYearsByUserId(@Param("userId") Long userId);

    /**
     * Count invoices by user.
     */
    @Query("SELECT COUNT(*) FROM app_invoice WHERE user_id = :userId")
    long countByUserId(@Param("userId") Long userId);

    /**
     * Count invoices by user and status.
     */
    @Query("SELECT COUNT(*) FROM app_invoice WHERE user_id = :userId AND status = :status")
    long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

    /**
     * Sum invoice amounts by user.
     */
    @Query("SELECT COALESCE(SUM(amount), 0) FROM app_invoice WHERE user_id = :userId")
    BigDecimal sumAmountByUserId(@Param("userId") Long userId);

    /**
     * Sum invoice amounts by user and status.
     */
    @Query("SELECT COALESCE(SUM(amount), 0) FROM app_invoice WHERE user_id = :userId AND status = :status")
    BigDecimal sumAmountByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);
}
