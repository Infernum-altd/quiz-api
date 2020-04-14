DROP TABLE IF EXISTS countries;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS quizzes;
DROP TABLE IF EXISTS favorite_quizzes;
DROP TABLE IF EXISTS score;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS answers;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS games;
DROP TABLE IF EXISTS games_tags;
DROP TABLE IF EXISTS announcements;
DROP TABLE IF EXISTS achievement_categories;
DROP TABLE IF EXISTS achievements;
DROP TABLE IF EXISTS rules;
DROP TABLE IF EXISTS achievements_rules;
DROP TABLE IF EXISTS users_achievements;
DROP TABLE IF EXISTS friends;
DROP TABLE IF EXISTS activity_type;
DROP TABLE IF EXISTS activities;
DROP TABLE IF EXISTS chats;
DROP TABLE IF EXISTS chats_users;
DROP TABLE IF EXISTS messages;



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
    password      VARCHAR(255)           NOT NULL,
    role          role_type              NOT NULL DEFAULT 'USER',
    name          VARCHAR(50)            NOT NULL DEFAULT 'John',
    surname       VARCHAR(50),
    image         BYTEA,
    birthdate     DATE                   NOT NULL DEFAULT CURRENT_DATE,
    gender        gender_type            NOT NULL DEFAULT 'NOT_MENTIONED',
    country_id    INTEGER REFERENCES countries (id),
    city          VARCHAR(255),
    rating        INTEGER                         DEFAULT 0,
    about         TEXT,
    active        BOOLEAN                NOT NULL DEFAULT true,
    notifications user_notification_type NOT NULL DEFAULT 'ON'
);



CREATE TYPE status_type AS ENUM ('PENDING','ACTIVE','DEACTIVATED');

CREATE TABLE categories
(
    id   serial PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE tags
(
    id   serial PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE quizzes
(
    id                serial PRIMARY KEY,
    name              VARCHAR(255)                  NOT NULL,
    image             BYTEA,
    author            INTEGER REFERENCES users (id) NOT NULL,
    category_id       INTEGER REFERENCES categories (id),
    date              DATE                          NOT NULL,
    description       TEXT,
    status            status_type                   NOT NULL DEFAULT 'PENDING',
    modification_time TIMESTAMP
);

CREATE TABLE quizzes_tags
(
    quiz_id INTEGER REFERENCES quizzes (id),
    tag_id  INTEGER REFERENCES tags (id)
);

CREATE TABLE favorite_quizzes
(
    user_id INTEGER REFERENCES users (id)   NOT NULL,
    quiz_id INTEGER REFERENCES quizzes (id) NOT NULL
);

CREATE TABLE score
(
    user_id INTEGER REFERENCES users (id)   NOT NULL,
    quiz_id INTEGER REFERENCES quizzes (id) NOT NULL,
    score   INTEGER                         NOT NULL
);



CREATE TYPE question_type AS ENUM ('OPTION','BOOLEAN','ANSWER','SEQUENCE');

CREATE TABLE questions
(
    id      serial PRIMARY KEY,
    quiz_id INTEGER REFERENCES quizzes (id),
    type    question_type NOT NULL,
    image   BYTEA,
    text    TEXT,
    active  BOOLEAN       NOT NULL DEFAULT true
);

CREATE TABLE answers
(
    id             serial PRIMARY KEY,
    question_id    INTEGER REFERENCES questions (id),
    text           TEXT,
    image          BYTEA,
    correct        BOOLEAN NOT NULL,
    next_answer_id INTEGER REFERENCES answers (id)
);



CREATE TABLE games
(
    id               serial PRIMARY KEY,
    quiz_id          INTEGER REFERENCES quizzes (id) NOT NULL,
    host_id          INTEGER REFERENCES users (id)   NOT NULL,
    question_timer   INTEGER                         NOT NULL,
    date             TIMESTAMP                       NOT NULL,
    sound            BOOLEAN                         NOT NULL DEFAULT true,
    max_users_number INTEGER                         NOT NULL,
    private          BOOLEAN                         NOT NULL DEFAULT false
);


CREATE TABLE announcements
(
    id                serial PRIMARY KEY,
    user_id           INTEGER REFERENCES users (id),
    game_id           INTEGER REFERENCES games (id),
    description       TEXT,
    status            status_type NOT NULL DEFAULT 'PENDING',
    modification_time TIMESTAMP
);



CREATE TABLE achievement_categories
(
    id   serial PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE achievements
(
    id          serial PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    description TEXT,
    image       BYTEA
);

CREATE TABLE rules
(
    id        serial PRIMARY KEY,
    condition JSON
);

CREATE TABLE achievements_rules
(
    achievement_id INTEGER REFERENCES achievements (id),
    rule_id        INTEGER REFERENCES rules (id)
);

CREATE TABLE users_achievements
(
    user_id        INTEGER REFERENCES users (id),
    achievement_id INTEGER REFERENCES achievements (id),
    progress       INTEGER,
    date           DATE
);



CREATE TYPE friend_view_settings AS ENUM ('ALL','FAVORITES','QUIZZES','SOCIAL','OFF');
CREATE TYPE friendship_status AS ENUM ('WAITING','PENDING','FRIEND' );

CREATE TABLE friends
(
    friend_id    INTEGER REFERENCES users (id) NOT NULL,
    user_id      INTEGER REFERENCES users (id) NOT NULL,
    view_setting friend_view_settings          NOT NULL,
    status       friendship_status             NOT NULL
);



CREATE TABLE activity_type
(
    id   serial PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE activities
(
    id          serial PRIMARY KEY,
    sender_id   INTEGER REFERENCES users (id)         NOT NULL,
    receiver_id INTEGER REFERENCES users (id)         NOT NULL,
    activity_type_id INTEGER REFERENCES activity_type (id) NOT NULL,
    info        JSON,
    date        TIMESTAMP                             NOT NULL
);



CREATE TABLE chats
(
    id      serial PRIMARY KEY,
    creator INTEGER REFERENCES users (id) NOT NULL,
    name    VARCHAR(50)                   NOT NULL,
    image   BYTEA,
    active  BOOLEAN                       NOT NULL DEFAULT true
);

CREATE TABLE chats_users
(
    chat_id       INTEGER REFERENCES chats (id) NOT NULL,
    user_id       INTEGER REFERENCES users (id) NOT NULL,
    notifications BOOLEAN                       NOT NULL DEFAULT true
);

CREATE TABLE messages
(
    id      serial PRIMARY KEY,
    user_id INTEGER REFERENCES users (id) NOT NULL,
    chat_id INTEGER REFERENCES chats (id) NOT NULL,
    text    TEXT,
    image   BYTEA,
    date    TIMESTAMP
)