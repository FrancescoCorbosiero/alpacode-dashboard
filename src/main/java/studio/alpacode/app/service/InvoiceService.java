package studio.alpacode.app.service;

import org.springframework.stereotype.Service;
import studio.alpacode.app.domain.Invoice;
import studio.alpacode.app.domain.InvoiceStatus;
import studio.alpacode.app.repository.InvoiceRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public List<Invoice> findByUserId(Long userId) {
        return invoiceRepository.findByUserId(userId);
    }

    public List<Invoice> findByUserIdAndStatus(Long userId, InvoiceStatus status) {
        return invoiceRepository.findByUserIdAndStatus(userId, status.name());
    }

    public List<Invoice> findByUserIdAndYear(Long userId, int year) {
        return invoiceRepository.findByUserIdAndYear(userId, year);
    }

    public List<Invoice> findByUserIdFiltered(Long userId, Integer year, String status) {
        List<Invoice> invoices = invoiceRepository.findByUserId(userId);

        return invoices.stream()
                .filter(inv -> year == null || inv.getInvoiceDate().getYear() == year)
                .filter(inv -> status == null || status.isEmpty() || inv.getStatus().name().equals(status))
                .toList();
    }

    public List<Integer> findDistinctYearsByUserId(Long userId) {
        return invoiceRepository.findDistinctYearsByUserId(userId);
    }

    public Optional<Invoice> findById(Long id) {
        return invoiceRepository.findById(id);
    }

    public long countByUserId(Long userId) {
        return invoiceRepository.countByUserId(userId);
    }

    public BigDecimal sumAmountByUserId(Long userId) {
        BigDecimal sum = invoiceRepository.sumAmountByUserId(userId);
        return sum != null ? sum : BigDecimal.ZERO;
    }

    public Invoice save(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }
}
