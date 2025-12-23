-- =============================================
-- Alpacode Dashboard - Database Schema
-- =============================================

-- Users Table
CREATE TABLE IF NOT EXISTS app_user (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    email               VARCHAR(255) NOT NULL UNIQUE,
    password            VARCHAR(255),
    name                VARCHAR(100) NOT NULL,
    company_name        VARCHAR(200),
    role                VARCHAR(20) NOT NULL,
    status              VARCHAR(20) NOT NULL,
    invite_token        VARCHAR(255),
    invite_token_expiry TIMESTAMP,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_app_user_email ON app_user(email);
CREATE INDEX IF NOT EXISTS idx_app_user_invite_token ON app_user(invite_token);
CREATE INDEX IF NOT EXISTS idx_app_user_role ON app_user(role);

-- Invoices Table
CREATE TABLE IF NOT EXISTS app_invoice (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    invoice_number  VARCHAR(50) NOT NULL UNIQUE,
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
    FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS idx_app_invoice_user_id ON app_invoice(user_id);
CREATE INDEX IF NOT EXISTS idx_app_invoice_status ON app_invoice(status);
CREATE INDEX IF NOT EXISTS idx_app_invoice_date ON app_invoice(invoice_date);
