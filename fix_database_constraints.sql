-- Drop foreign key constraints first
ALTER TABLE project_updates DROP FOREIGN KEY IF EXISTS FKx703pp2vrtafnylhjyo7i2qu;
ALTER TABLE ngos DROP FOREIGN KEY IF EXISTS FKtp91pney2p5wkpn9y0bmjn7yr;
ALTER TABLE donors DROP FOREIGN KEY IF EXISTS FKeew3vfgp45p05bc32inv3275w;

-- Drop the problematic columns
ALTER TABLE project_updates DROP COLUMN IF EXISTS updated_by;
ALTER TABLE ngos DROP COLUMN IF EXISTS verified_by;
ALTER TABLE donors DROP COLUMN IF EXISTS division_id;