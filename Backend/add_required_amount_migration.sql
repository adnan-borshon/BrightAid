-- Add required_amount column to school_projects table
ALTER TABLE school_projects ADD COLUMN IF NOT EXISTS required_amount DECIMAL(15,2) DEFAULT 0.00;

-- Update existing projects with a default required amount if needed
UPDATE school_projects SET required_amount = 100000.00 WHERE required_amount IS NULL OR required_amount = 0;