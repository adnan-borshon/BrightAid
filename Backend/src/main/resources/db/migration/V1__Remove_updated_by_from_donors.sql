-- Remove updated_by column from donors table
ALTER TABLE donors DROP COLUMN IF EXISTS updated_by;