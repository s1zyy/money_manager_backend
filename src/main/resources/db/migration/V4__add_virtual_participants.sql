ALTER TABLE participants ADD COLUMN is_virtual BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE participants ALTER COLUMN email DROP NOT NULL;

ALTER TABLE participants DROP CONSTRAINT IF EXISTS participants_email_key;

CREATE UNIQUE INDEX participants_email_unique ON participants(email) WHERE email IS NOT NULL;
