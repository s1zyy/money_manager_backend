ALTER TABLE trip_participants ADD COLUMN budget NUMERIC(19,4) NOT NULL DEFAULT 0;
ALTER TABLE trips DROP COLUMN total_budget;
ALTER TABLE trips DROP COLUMN prepaid_expenses;
