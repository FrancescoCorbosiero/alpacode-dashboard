-- =============================================
-- V3: Add updated_at trigger (PostgreSQL only)
-- =============================================
-- Auto-updates the updated_at column on row modification
-- =============================================

-- Create the trigger function
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Apply trigger to app_user
DROP TRIGGER IF EXISTS trigger_app_user_updated_at ON app_user;
CREATE TRIGGER trigger_app_user_updated_at
    BEFORE UPDATE ON app_user
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Apply trigger to app_invoice
DROP TRIGGER IF EXISTS trigger_app_invoice_updated_at ON app_invoice;
CREATE TRIGGER trigger_app_invoice_updated_at
    BEFORE UPDATE ON app_invoice
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();
