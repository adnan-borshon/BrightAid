-- Drop user_id column from students table if it exists
ALTER TABLE students DROP COLUMN IF EXISTS user_id;