package com.quiz.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Answer {

    private int id;
    private int questionId;
    private String text;
    private boolean correct;
    private int nextAnswerId;
}

////CREATE TABLE answers
////(
////    id             serial PRIMARY KEY,
////    question_id    INTEGER REFERENCES questions (id),
////    text           TEXT,
////    image          BYTEA,
////    correct        BOOLEAN NOT NULL,
////    next_answer_id INTEGER REFERENCES answers (id)
////);