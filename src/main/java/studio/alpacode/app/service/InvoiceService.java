package studio.alpacode.app.service;

import org.springframework.stereotype.Service;
import studio.alpacode.app.domain.Invoice;
import studio.alpacode.app.domain.InvoiceStatus;
import studio.alpacode.app.repository.InvoiceRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service for invoice operations.
 * Handles business logic and delegates to repository.
 */
@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * Find all invoices for a user.
     */
    public List<Invoice> findByUserId(Long userId) {
        return invoiceRepository.findByUserId(userId);
    }

    /**
     * Find invoices with pagination.
     */
    public List<Invoice> findByUserIdPaginated(Long userId, int page, int size) {
        int offset = page * size;
        return invoiceRepository.findByUserIdPaginated(userId, size, offset);
    }

    /**
     * Find invoices by status.
     */
    public List<Invoice> findByUserIdAndStatus(Long userId, InvoiceStatus status) {
        return invoiceRepository.findByUserIdAndStatus(userId, status.name());
    }

    /**
     * Find invoices by year.
     */
    public List<Invoice> findByUserIdAndYear(Long userId, int year) {
        return invoiceRepository.findByUserIdAndYear(userId, year);
    }

    /**
     * Find invoices with optional year and status filters.
     * Uses database-level filtering for efficiency.
     */
    public List<Invoice> findByUserIdFiltered(Long userId, Integer year, String status) {
        if (year != null && status != null && !status.isEmpty()) {
            return invoiceRepository.findByUserIdAndYearAndStatus(userId, year, status);
        } else if (year != null) {
            return invoiceRepository.findByUserIdAndYear(userId, year);
        } else if (status != null && !status.isEmpty()) {
            return invoiceRepository.findByUserIdAndStatus(userId, status);
        } else {
            return invoiceRepository.findByUserId(userId);
        }
    }

    /**
     * Get distinct years for user's invoices.
     */
    public List<Integer> findDistinctYearsByUserId(Long userId) {
        return invoiceRepository.findDistinctYearsByUserId(userId);
    }

    /**
     * Find invoice by ID.
     */
    public Optional<Invoice> findById(Long id) {
        return invoiceRepository.findById(id);
    }

    /**
     * Count invoices by user.
     */
    public long countByUserId(Long userId) {
        return invoiceRepository.countByUserId(userId);
    }

    /**
     * Count invoices by user and status.
     */
    public long countByUserIdAndStatus(Long userId, InvoiceStatus status) {
        return invoiceRepository.countByUserIdAndStatus(userId, status.name());
    }

    /**
     * Sum invoice amounts by user.
     */
    public BigDecimal sumAmountByUserId(Long userId) {
        BigDecimal sum = invoiceRepository.sumAmountByUserId(userId);
        return sum != null ? sum : BigDecimal.ZERO;
    }

    /**
     * Sum invoice amounts by user and status.
     */
    public BigDecimal sumAmountByUserIdAndStatus(Long userId, InvoiceStatus status) {
        BigDecimal sum = invoiceRepository.sumAmountByUserIdAndStatus(userId, status.name());
        return sum != null ? sum : BigDecimal.ZERO;
    }

    /**
     * Save invoice.
     */
    public Invoice save(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    /**
     * Delete invoice.
     */
    public void delete(Invoice invoice) {
        invoiceRepository.delete(invoice);
    }
}
