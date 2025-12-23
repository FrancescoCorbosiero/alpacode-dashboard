-- =============================================
-- Alpacode Dashboard - PostgreSQL Schema
-- =============================================
--
-- USAGE:
--   psql -h localhost -U alpacode -d alpacode -f schema.sql
--
-- Or connect and run:
--   \i schema.sql
--
-- =============================================

-- =============================================
-- USERS TABLE
-- =============================================
CREATE TABLE IF NOT EXISTS app_user (
    id                      BIGSERIAL PRIMARY KEY,
    email                   VARCHAR(255) NOT NULL,
    password                VARCHAR(255),
    name                    VARCHAR(100) NOT NULL,
    company_name            VARCHAR(200),
    role                    VARCHAR(20) NOT NULL,
    status                  VARCHAR(20) NOT NULL,
    invite_token            VARCHAR(255),
    invite_token_expiry     TIMESTAMP,
    created_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_app_user_email UNIQUE (email),
    CONSTRAINT chk_app_user_role CHECK (role IN ('ADMIN', 'CLIENT')),
    CONSTRAINT chk_app_user_status CHECK (status IN ('ACTIVE', 'PENDING', 'DISABLED'))
);

-- User indexes
CREATE INDEX IF NOT EXISTS idx_app_user_email ON app_user(email);
CREATE INDEX IF NOT EXISTS idx_app_user_role ON app_user(role);
CREATE INDEX IF NOT EXISTS idx_app_user_status ON app_user(status);
CREATE INDEX IF NOT EXISTS idx_app_user_invite_token ON app_user(invite_token)
    WHERE invite_token IS NOT NULL;

-- Comments
COMMENT ON TABLE app_user IS 'Application users (admins and clients)';
COMMENT ON COLUMN app_user.role IS 'ADMIN or CLIENT';
COMMENT ON COLUMN app_user.status IS 'ACTIVE, PENDING (awaiting password), or DISABLED';
COMMENT ON COLUMN app_user.invite_token IS 'One-time invite token, expires after 48 hours';


-- =============================================
-- INVOICES TABLE
-- =============================================
CREATE TABLE IF NOT EXISTS app_invoice (
    id                      BIGSERIAL PRIMARY KEY,
    invoice_number          VARCHAR(50) NOT NULL,
    user_id                 BIGINT NOT NULL,
    invoice_date            DATE NOT NULL,
    due_date                DATE,
    description             VARCHAR(500),
    amount                  DECIMAL(12, 2) NOT NULL,
    currency                VARCHAR(3) NOT NULL DEFAULT 'EUR',
    status                  VARCHAR(20) NOT NULL,
    pdf_path                VARCHAR(500),
    created_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_app_invoice_number UNIQUE (invoice_number),
    CONSTRAINT fk_app_invoice_user FOREIGN KEY (user_id)
        REFERENCES app_user(id) ON DELETE RESTRICT,
    CONSTRAINT chk_app_invoice_status CHECK (status IN ('PAID', 'PENDING', 'OVERDUE')),
    CONSTRAINT chk_app_invoice_amount CHECK (amount >= 0)
);

-- Invoice indexes
CREATE INDEX IF NOT EXISTS idx_app_invoice_user_id ON app_invoice(user_id);
CREATE INDEX IF NOT EXISTS idx_app_invoice_status ON app_invoice(status);
CREATE INDEX IF NOT EXISTS idx_app_invoice_date ON app_invoice(invoice_date DESC);
CREATE INDEX IF NOT EXISTS idx_app_invoice_user_status ON app_invoice(user_id, status);
CREATE INDEX IF NOT EXISTS idx_app_invoice_user_date ON app_invoice(user_id, invoice_date DESC);

-- Comments
COMMENT ON TABLE app_invoice IS 'Client invoices';
COMMENT ON COLUMN app_invoice.amount IS 'Invoice amount (max 9,999,999,999.99)';
COMMENT ON COLUMN app_invoice.pdf_path IS 'Path to PDF file (S3 key or filesystem path)';
COMMENT ON COLUMN app_invoice.status IS 'PAID, PENDING, or OVERDUE';


-- =============================================
-- TRIGGER: Auto-update updated_at
-- =============================================
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Apply to app_user
DROP TRIGGER IF EXISTS trg_app_user_updated_at ON app_user;
CREATE TRIGGER trg_app_user_updated_at
    BEFORE UPDATE ON app_user
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Apply to app_invoice
DROP TRIGGER IF EXISTS trg_app_invoice_updated_at ON app_invoice;
CREATE TRIGGER trg_app_invoice_updated_at
    BEFORE UPDATE ON app_invoice
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();
