DROP INDEX IF EXISTS idx_trips_status;

CREATE TABLE trip_statuses (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL
);

INSERT INTO trip_statuses (code) VALUES ('UPCOMING');
INSERT INTO trip_statuses (code) VALUES ('ACTIVE');
INSERT INTO trip_statuses (code) VALUES ('ARCHIVED');

ALTER TABLE trips DROP COLUMN trip_status;

ALTER TABLE trips ADD COLUMN status_id BIGINT NOT NULL;

ALTER TABLE trips
    ADD CONSTRAINT fk_trips_status
        FOREIGN KEY (status_id)
            REFERENCES trip_statuses(id);

CREATE INDEX idx_trips_status_id ON trips(status_id);