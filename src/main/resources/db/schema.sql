DROP TABLE IF EXISTS users;

CREATE TYPE gender_type AS ENUM ('MALE','FEMALE','NOT_MENTIONED');
CREATE TYPE role_type AS ENUM ('USER', 'MODERATOR', 'ADMIN','SUPER_ADMIN');
CREATE TYPE user_notification_type AS ENUM ('ON','OFF','FRIENDS_ONLY', 'SYSTEM_ONLY');


CREATE TABLE countries
(
    id   serial PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE USERS
(
    id            serial PRIMARY KEY,
    email         VARCHAR(255) UNIQUE    NOT NULL,
    role          role_type              NOT NULL DEFAULT 'USER',
    name          VARCHAR(50)            NOT NULL,
    surname       VARCHAR(50),
    birthdate     DATE                   NOT NULL,
    gender        gender_type            NOT NULL DEFAULT 'NOT_MENTIONED',
    country_id    INTEGER REFERENCES countries (id),
    city          VARCHAR(255),
    rating        INTEGER                         DEFAULT 0,
    about         TEXT,
    active        BOOLEAN                NOT NULL DEFAULT true,
    notifications user_notification_type NOT NULL DEFAULT 'ON'
);