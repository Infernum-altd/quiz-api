ALTER TABLE activities
    DROP COLUMN info CASCADE;
ALTER TABLE activities
    DROP COLUMN activity_type_id CASCADE;

DROP TABLE activity_type CASCADE;

ALTER TABLE activities
    ADD COLUMN text      TEXT,
    ADD COLUMN image     BYTEA,
    ADD COLUMN generated BOOLEAN CONSTRAINT NOT NULL default true ;

ALTER TABLE activities
    RENAME TO system_announcements;