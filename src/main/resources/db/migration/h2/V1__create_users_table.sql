-- =============================================
-- V1: Create Users Table (H2 version)
-- =============================================

CREATE TABLE IF NOT EXISTS app_user (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
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

CREATE INDEX IF NOT EXISTS idx_app_user_email ON app_user(email);
CREATE INDEX IF NOT EXISTS idx_app_user_invite_token ON app_user(invite_token);
CREATE INDEX IF NOT EXISTS idx_app_user_role ON app_user(role);
CREATE INDEX IF NOT EXISTS idx_app_user_status ON app_user(status);
CREATE INDEX IF NOT EXISTS idx_app_user_role_status ON app_user(role, status);
