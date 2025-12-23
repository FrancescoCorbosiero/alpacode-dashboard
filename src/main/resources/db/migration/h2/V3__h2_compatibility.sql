-- =============================================
-- V3: H2 Compatibility adjustments
-- =============================================
-- H2 in PostgreSQL mode needs some adjustments
-- This is a placeholder for H2-specific migrations
-- =============================================

-- H2 doesn't support triggers the same way PostgreSQL does
-- The application layer handles updated_at via @LastModifiedDate
-- No action needed here, but file required for Flyway ordering

SELECT 1;
