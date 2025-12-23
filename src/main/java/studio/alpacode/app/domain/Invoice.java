package studio.alpacode.app.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Invoice entity.
 * Stores client invoices with PDF reference.
 */
public class Invoice {

    @Id
    private Long id;

    private String invoiceNumber;

    private Long userId;

    private LocalDate invoiceDate;

    private LocalDate dueDate;

    private String description;

    private BigDecimal amount;

    private String currency;

    private InvoiceStatus status;

    private String pdfPath;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Invoice() {
    }

    public Invoice(String invoiceNumber, Long userId, LocalDate invoiceDate,
                   String description, BigDecimal amount, InvoiceStatus status) {
        this.invoiceNumber = invoiceNumber;
        this.userId = userId;
        this.invoiceDate = invoiceDate;
        this.description = description;
        this.amount = amount;
        this.currency = "EUR";
        this.status = status;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Convenience methods

    public boolean isPaid() {
        return this.status == InvoiceStatus.PAID;
    }

    public boolean isPending() {
        return this.status == InvoiceStatus.PENDING;
    }

    public boolean isOverdue() {
        return this.status == InvoiceStatus.OVERDUE;
    }

    /**
     * Returns formatted amount with currency symbol.
     * Uses Italian locale for proper decimal formatting.
     */
    public String getFormattedAmount() {
        if (amount == null) {
            return "€ 0,00";
        }
        return String.format(Locale.ITALIAN, "€ %,.2f", amount);
    }
}
