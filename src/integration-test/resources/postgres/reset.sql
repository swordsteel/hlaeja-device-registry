-- Disable triggers on the tables

-- Truncate tables
TRUNCATE TABLE nodes CASCADE;
TRUNCATE TABLE devices CASCADE;
TRUNCATE TABLE types CASCADE;

-- Enable triggers on the account table
