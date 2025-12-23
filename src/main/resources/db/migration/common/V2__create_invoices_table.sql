-- =============================================
-- V2: Create Invoices Table
-- =============================================
-- Alpacode Dashboard - Invoice management
-- Supports: PostgreSQL, H2 (PostgreSQL mode)
-- =============================================

CREATE TABLE IF NOT EXISTS app_invoice (
    id              BIGSERIAL PRIMARY KEY,
    invoice_number  VARCHAR(50) NOT NULL,
    user_id         BIGINT NOT NULL,
    invoice_date    DATE NOT NULL,
    due_date        DATE,
    description     VARCHAR(500),
    amount          DECIMAL(12, 2) NOT NULL,
    currency        VARCHAR(3) NOT NULL DEFAULT 'EUR',
    status          VARCHAR(20) NOT NULL,
    pdf_path        VARCHAR(500),
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_app_invoice_number UNIQUE (invoice_number),
    CONSTRAINT fk_app_invoice_user FOREIGN KEY (user_id)
        REFERENCES app_user(id) ON DELETE RESTRICT,
    CONSTRAINT chk_app_invoice_status CHECK (status IN ('PAID', 'PENDING', 'OVERDUE')),
    CONSTRAINT chk_app_invoice_amount CHECK (amount >= 0),
    CONSTRAINT chk_app_invoice_currency CHECK (currency ~ '^[A-Z]{3}$')
);

-- Indexes for common query patterns
CREATE INDEX IF NOT EXISTS idx_app_invoice_user_id ON app_invoice(user_id);
CREATE INDEX IF NOT EXISTS idx_app_invoice_status ON app_invoice(status);
CREATE INDEX IF NOT EXISTS idx_app_invoice_date ON app_invoice(invoice_date DESC);
CREATE INDEX IF NOT EXISTS idx_app_invoice_user_status ON app_invoice(user_id, status);
CREATE INDEX IF NOT EXISTS idx_app_invoice_user_date ON app_invoice(user_id, invoice_date DESC);

COMMENT ON TABLE app_invoice IS 'Client invoices with PDF storage reference';
COMMENT ON COLUMN app_invoice.amount IS 'Invoice amount with 2 decimal precision (max 9,999,999,999.99)';
COMMENT ON COLUMN app_invoice.pdf_path IS 'Path to stored PDF file (S3 key or filesystem path)';
COMMENT ON COLUMN app_invoice.status IS 'Payment status: PAID, PENDING, OVERDUE';
