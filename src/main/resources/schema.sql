-- Schema for H2 Database
-- Alpacode Dashboard Users Table

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    name VARCHAR(100) NOT NULL,
    company_name VARCHAR(200),
    role VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    invite_token VARCHAR(255),
    invite_token_expiry TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Index for faster email lookups
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- Index for invite token lookups
CREATE INDEX IF NOT EXISTS idx_users_invite_token ON users(invite_token);

-- Index for role-based queries
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);
