DROP TABLE IF EXISTS countries;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS quizzes;
DROP TABLE IF EXISTS favorite_quizzes;
DROP TABLE IF EXISTS score;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS answers;

CREATE TYPE gender_type AS ENUM ('MALE','FEMALE','NOT_MENTIONED');
CREATE TYPE role_type AS ENUM ('USER', 'MODERATOR', 'ADMIN','SUPER_ADMIN');
CREATE TYPE user_notification_type AS ENUM ('ON','OFF','FRIENDS_ONLY', 'SYSTEM_ONLY');


CREATE TABLE countries
(
    id   serial PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE users
(
    id            serial PRIMARY KEY,
    email         VARCHAR(255) UNIQUE    NOT NULL,
    role          role_type              NOT NULL DEFAULT 'USER',
    name          VARCHAR(50)            NOT NULL,
    surname       VARCHAR(50),
    image         BYTEA,
    birthdate     DATE                   NOT NULL,
    gender        gender_type            NOT NULL DEFAULT 'NOT_MENTIONED',
    country_id    INTEGER REFERENCES countries (id),
    city          VARCHAR(255),
    rating        INTEGER                         DEFAULT 0,
    about         TEXT,
    active        BOOLEAN                NOT NULL DEFAULT true,
    notifications user_notification_type NOT NULL DEFAULT 'ON'
);

CREATE TYPE quiz_status_type AS ENUM ('PENDING','ACTIVE','DEACTIVATED', 'DELETED');

CREATE TABLE quizzes
(
    id                serial PRIMARY KEY,
    name              VARCHAR(255)                  NOT NULL,
    image             BYTEA,
    author            INTEGER REFERENCES users (id) NOT NULL,
    date              DATE                          NOT NULL,
    description       TEXT,
    status            quiz_status_type              NOT NULL DEFAULT 'PENDING',
    modification_time TIMESTAMP
);

CREATE TABLE favorite_quizzes
(
    user_id INTEGER REFERENCES users (id)  NOT NULL,
    quiz_id INTEGER REFERENCES quizes (id) NOT NULL
);

CREATE TABLE score
(
    user_id INTEGER REFERENCES users (id)  NOT NULL,
    quiz_id INTEGER REFERENCES quizes (id) NOT NULL,
    score   INTEGER                        NOT NULL
);



CREATE TYPE question_type AS ENUM ('OPTION','BOOLEAN','ANSWER','SEQUENCE');

CREATE TABLE questions
(
    id      serial PRIMARY KEY,
    quiz_id INTEGER REFERENCES quizzes (id),
    type    question_type NOT NULL,
    image   BYTEA,
    text    TEXT
);

CREATE TABLE answers
(
    id             serial PRIMARY KEY,
    question_id    INTEGER REFERENCES questions (id),
    text           TEXT,
    correct        BOOLEAN,
    next_answer_id INTEGER REFERENCES answers (id)
);