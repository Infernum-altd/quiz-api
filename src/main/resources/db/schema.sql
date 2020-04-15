DROP TABLE IF EXISTS countries CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS quizzes CASCADE;
DROP TABLE IF EXISTS favorite_quizzes CASCADE;
DROP TABLE IF EXISTS score CASCADE;
DROP TABLE IF EXISTS questions CASCADE;
DROP TABLE IF EXISTS answers CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS tags CASCADE;
DROP TABLE IF EXISTS games CASCADE;
DROP TABLE IF EXISTS quizzes_tags CASCADE;
DROP TABLE IF EXISTS announcements CASCADE;
DROP TABLE IF EXISTS achievement_categories CASCADE;
DROP TABLE IF EXISTS achievements CASCADE;
DROP TABLE IF EXISTS rules CASCADE;
DROP TABLE IF EXISTS achievements_rules CASCADE;
DROP TABLE IF EXISTS users_achievements CASCADE;
DROP TABLE IF EXISTS friends CASCADE;
DROP TABLE IF EXISTS activity_type CASCADE;
DROP TABLE IF EXISTS activities CASCADE;
DROP TABLE IF EXISTS chats CASCADE;
DROP TABLE IF EXISTS chats_users CASCADE;
DROP TABLE IF EXISTS messages CASCADE;
DROP TABLE IF EXISTS moderators_quizzes CASCADE;
DROP TABLE IF EXISTS moderators_announcements CASCADE;
DROP TABLE IF EXISTS languages CASCADE;
DROP TABLE IF EXISTS quizzes_languages CASCADE;

DROP TYPE IF EXISTS gender_type CASCADE;
DROP TYPE IF EXISTS role_type CASCADE;
DROP TYPE IF EXISTS user_notification_type CASCADE;
DROP TYPE IF EXISTS status_type CASCADE;
DROP TYPE IF EXISTS question_type CASCADE;
DROP TYPE IF EXISTS friend_view_settings CASCADE;
DROP TYPE IF EXISTS friendship_status CASCADE;
DROP TYPE IF EXISTS game_status_type CASCADE;



CREATE TYPE gender_type AS ENUM ('MALE','FEMALE','NOT_MENTIONED');
CREATE TYPE role_type AS ENUM ('USER', 'MODERATOR', 'ADMIN','SUPER_ADMIN');
CREATE TYPE user_notification_type AS ENUM ('ON','OFF','FRIENDS_ONLY', 'SYSTEM_ONLY');


CREATE TABLE countries
(
    id   serial PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE languages
(
    id   serial PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
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
    notifications user_notification_type NOT NULL DEFAULT 'ON',
    language_id   INTEGER REFERENCES languages (id)
);



CREATE TYPE status_type AS ENUM ('PENDING','ACTIVE','DEACTIVATED','DELETED');

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

CREATE TABLE quizzes_languages
(
    quiz_id     INTEGER REFERENCES quizzes (id),
    language_id INTEGER REFERENCES languages (id)
);



CREATE TYPE question_type AS ENUM ('OPTION','BOOLEAN','ANSWER','SEQUENCE');

CREATE TABLE questions
(
    id          serial PRIMARY KEY,
    quiz_id     INTEGER REFERENCES quizzes (id),
    type        question_type NOT NULL,
    image       BYTEA,
    text        TEXT,
    active      BOOLEAN       NOT NULL DEFAULT true,
    language_id INTEGER REFERENCES languages (id)
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



CREATE TYPE game_status_type AS ENUM ('WAITING','STARTED','FINISHED');

CREATE TABLE games
(
    id               serial PRIMARY KEY,
    quiz_id          INTEGER REFERENCES quizzes (id) NOT NULL,
    host_id          INTEGER REFERENCES users (id)   NOT NULL,
    question_timer   INTEGER                         NOT NULL,
    date             TIMESTAMP                       NOT NULL,
    sound            BOOLEAN                         NOT NULL DEFAULT true,
    max_users_number INTEGER                         NOT NULL,
    private          BOOLEAN                         NOT NULL DEFAULT false,
    status           game_status_type                NOT NULL DEFAULT 'WAITING'
);

CREATE TABLE score
(
    user_id INTEGER REFERENCES users (id) NOT NULL,
    game_id INTEGER REFERENCES games (id) NOT NULL,
    score   INTEGER                       NOT NULL
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



CREATE TABLE moderators_quizzes
(
    moderator_id INTEGER REFERENCES users (id)   NOT NULL,
    quiz_id      INTEGER REFERENCES quizzes (id) NOT NULL
);

CREATE TABLE moderators_announcements
(
    moderator_id    INTEGER REFERENCES users (id)         NOT NULL,
    announcement_id INTEGER REFERENCES announcements (id) NOT NULL
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
    image       BYTEA,
    category_id INTEGER REFERENCES achievement_categories (id)
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
CREATE TYPE friendship_status AS ENUM ('WAITING','PENDING','FRIEND','SPAM');

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
    id               serial PRIMARY KEY,
    sender_id        INTEGER REFERENCES users (id)         NOT NULL,
    receiver_id      INTEGER REFERENCES users (id)         NOT NULL,
    activity_type_id INTEGER REFERENCES activity_type (id) NOT NULL,
    info             JSON,
    date             TIMESTAMP                             NOT NULL
);



CREATE TABLE chats
(
    id         serial PRIMARY KEY,
    creator    INTEGER REFERENCES users (id) NOT NULL,
    name       VARCHAR(50)                   NOT NULL,
    image      BYTEA,
    active     BOOLEAN                       NOT NULL DEFAULT true,
    group_chat BOOLEAN                       NOT NULL DEFAULT false
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