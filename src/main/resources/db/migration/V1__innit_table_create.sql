CREATE TABLE participants (
                              id UUID PRIMARY KEY,
                              name VARCHAR(255),
                              email VARCHAR(255) UNIQUE,
                              password_hash VARCHAR(255)
);

CREATE TABLE trips (
                       id UUID PRIMARY KEY,
                       owner_id UUID NOT NULL,
                       name VARCHAR(255) NOT NULL,
                       start_date DATE,
                       end_date DATE,
                       total_budget DECIMAL(19, 2),
                       prepaid_expenses DECIMAL(19, 2),
                       join_code VARCHAR(8) NOT NULL UNIQUE,
                       trip_status VARCHAR(50) NOT NULL,
                       CONSTRAINT fk_trip_owner FOREIGN KEY (owner_id) REFERENCES participants(id) ON DELETE CASCADE
);

CREATE TABLE trip_participants (
                                   trip_id UUID NOT NULL,
                                   participant_id UUID NOT NULL,
                                   PRIMARY KEY (trip_id, participant_id),
                                   CONSTRAINT fk_trip_participants_trip FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE,
                                   CONSTRAINT fk_trip_participants_participant FOREIGN KEY (participant_id) REFERENCES participants(id) ON DELETE CASCADE
);

CREATE TABLE expenses (
                          id UUID PRIMARY KEY,
                          trip_id UUID NOT NULL,
                          amount DECIMAL(19, 2),
                          payer_id UUID,
                          date DATE,
                          description TEXT,
                          CONSTRAINT fk_expense_trip FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE,
                          CONSTRAINT fk_expense_payer FOREIGN KEY (payer_id) REFERENCES participants(id) ON DELETE SET NULL
);

CREATE TABLE expense_participants (
                                      expense_id UUID NOT NULL,
                                      participant_id UUID NOT NULL,
                                      PRIMARY KEY (expense_id, participant_id),
                                      CONSTRAINT fk_expense_participants_expense FOREIGN KEY (expense_id) REFERENCES expenses(id) ON DELETE CASCADE,
                                      CONSTRAINT fk_expense_participants_participant FOREIGN KEY (participant_id) REFERENCES participants(id) ON DELETE CASCADE
);

CREATE INDEX idx_trips_owner_id ON trips(owner_id);
CREATE INDEX idx_trips_join_code ON trips(join_code);
CREATE INDEX idx_trips_status ON trips(trip_status);
CREATE INDEX idx_expenses_trip_id ON expenses(trip_id);
CREATE INDEX idx_expenses_payer_id ON expenses(payer_id);
CREATE INDEX idx_trip_participants_participant ON trip_participants(participant_id);
CREATE INDEX idx_expense_participants_participant ON expense_participants(participant_id);
