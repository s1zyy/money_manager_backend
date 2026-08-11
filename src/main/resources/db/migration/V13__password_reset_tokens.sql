CREATE TABLE password_reset_tokens (
    token VARCHAR(8) PRIMARY KEY,
    participant_id UUID NOT NULL REFERENCES participants(id) ON DELETE CASCADE,
    expires_at TIMESTAMP NOT NULL
);
