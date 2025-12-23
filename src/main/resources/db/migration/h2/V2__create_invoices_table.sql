-- =============================================
-- V2: Create Invoices Table (H2 version)
-- =============================================

CREATE TABLE IF NOT EXISTS app_invoice (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
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
    CONSTRAINT chk_app_invoice_amount CHECK (amount >= 0)
);

CREATE INDEX IF NOT EXISTS idx_app_invoice_user_id ON app_invoice(user_id);
CREATE INDEX IF NOT EXISTS idx_app_invoice_status ON app_invoice(status);
CREATE INDEX IF NOT EXISTS idx_app_invoice_date ON app_invoice(invoice_date);
CREATE INDEX IF NOT EXISTS idx_app_invoice_user_status ON app_invoice(user_id, status);
CREATE INDEX IF NOT EXISTS idx_app_invoice_user_date ON app_invoice(user_id, invoice_date);
