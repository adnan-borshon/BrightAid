-- Remove the calculated columns from donors table
ALTER TABLE donors DROP COLUMN IF EXISTS total_donated;
ALTER TABLE donors DROP COLUMN IF EXISTS total_schools_supported;
ALTER TABLE donors DROP COLUMN IF EXISTS total_students_sponsored;
ALTER TABLE donors DROP COLUMN IF EXISTS total_projects_donated;