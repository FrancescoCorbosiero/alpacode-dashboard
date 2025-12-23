-- =============================================
-- V1: Create Users Table
-- =============================================
-- Alpacode Dashboard - User management
-- Supports: PostgreSQL, H2 (PostgreSQL mode)
-- =============================================

CREATE TABLE IF NOT EXISTS app_user (
    id              BIGSERIAL PRIMARY KEY,
    email           VARCHAR(255) NOT NULL,
    password        VARCHAR(255),
    name            VARCHAR(100) NOT NULL,
    company_name    VARCHAR(200),
    role            VARCHAR(20) NOT NULL,
    status          VARCHAR(20) NOT NULL,
    invite_token    VARCHAR(255),
    invite_token_expiry TIMESTAMP,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_app_user_email UNIQUE (email),
    CONSTRAINT chk_app_user_role CHECK (role IN ('ADMIN', 'CLIENT')),
    CONSTRAINT chk_app_user_status CHECK (status IN ('ACTIVE', 'PENDING', 'DISABLED'))
);

-- Indexes for common query patterns
CREATE INDEX IF NOT EXISTS idx_app_user_email ON app_user(email);
CREATE INDEX IF NOT EXISTS idx_app_user_invite_token ON app_user(invite_token) WHERE invite_token IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_app_user_role ON app_user(role);
CREATE INDEX IF NOT EXISTS idx_app_user_status ON app_user(status);
CREATE INDEX IF NOT EXISTS idx_app_user_role_status ON app_user(role, status);

COMMENT ON TABLE app_user IS 'Application users - both admins and clients';
COMMENT ON COLUMN app_user.role IS 'User role: ADMIN or CLIENT';
COMMENT ON COLUMN app_user.status IS 'Account status: ACTIVE, PENDING (awaiting password), DISABLED';
COMMENT ON COLUMN app_user.invite_token IS 'One-time token for invite flow, expires after 48 hours';
