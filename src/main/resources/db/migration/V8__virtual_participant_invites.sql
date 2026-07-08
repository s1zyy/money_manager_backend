CREATE TABLE virtual_participant_invites (
    token VARCHAR(8) PRIMARY KEY,
    virtual_participant_id UUID NOT NULL REFERENCES participants(id),
    trip_id UUID NOT NULL REFERENCES trips(id),
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL
);
