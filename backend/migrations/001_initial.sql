-- Planned cloud schema for Neon PostgreSQL. Run via a trusted migration role.
-- This migration has not been applied to a live Neon database.
BEGIN;
CREATE TABLE anatomy_organs (
    id text PRIMARY KEY,
    name_ar text NOT NULL,
    name_en text NOT NULL,
    description_ar text NOT NULL,
    description_en text NOT NULL DEFAULT '',
    revision bigint NOT NULL DEFAULT 1 CHECK (revision > 0),
    published boolean NOT NULL DEFAULT false,
    updated_at timestamptz NOT NULL DEFAULT now()
);
CREATE TABLE anatomy_assets (
    id text PRIMARY KEY,
    organ_id text NOT NULL REFERENCES anatomy_organs(id),
    object_key text NOT NULL,
    sha256 text NOT NULL CHECK (sha256 ~ '^[0-9a-f]{64}$'),
    byte_size bigint NOT NULL CHECK (byte_size >= 0),
    license text NOT NULL,
    attribution text NOT NULL,
    revision bigint NOT NULL DEFAULT 1
);
CREATE TABLE user_favorites (
    user_id text NOT NULL,
    organ_id text NOT NULL REFERENCES anatomy_organs(id),
    updated_at timestamptz NOT NULL DEFAULT now(),
    PRIMARY KEY (user_id, organ_id)
);
CREATE TABLE user_progress (
    user_id text NOT NULL,
    lesson_id text NOT NULL,
    completed boolean NOT NULL DEFAULT false,
    updated_at timestamptz NOT NULL DEFAULT now(),
    PRIMARY KEY (user_id, lesson_id)
);
COMMIT;
-- Future API must verify identity and scope all user rows to that identity.
-- Do not grant direct database access to the Android client.
