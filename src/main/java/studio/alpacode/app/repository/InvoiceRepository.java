package studio.alpacode.app.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import studio.alpacode.app.domain.Invoice;

import java.util.List;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, Long> {

    @Query("SELECT * FROM APP_INVOICE WHERE USER_ID = :userId ORDER BY INVOICE_DATE DESC")
    List<Invoice> findByUserId(@Param("userId") Long userId);

    @Query("SELECT * FROM APP_INVOICE WHERE USER_ID = :userId AND STATUS = :status ORDER BY INVOICE_DATE DESC")
    List<Invoice> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

    @Query("SELECT * FROM APP_INVOICE WHERE USER_ID = :userId AND EXTRACT(YEAR FROM INVOICE_DATE) = :year ORDER BY INVOICE_DATE DESC")
    List<Invoice> findByUserIdAndYear(@Param("userId") Long userId, @Param("year") int year);

    @Query("SELECT DISTINCT EXTRACT(YEAR FROM INVOICE_DATE) as year FROM APP_INVOICE WHERE USER_ID = :userId ORDER BY year DESC")
    List<Integer> findDistinctYearsByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(*) FROM APP_INVOICE WHERE USER_ID = :userId")
    long countByUserId(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(AMOUNT), 0) FROM APP_INVOICE WHERE USER_ID = :userId")
    java.math.BigDecimal sumAmountByUserId(@Param("userId") Long userId);
}
