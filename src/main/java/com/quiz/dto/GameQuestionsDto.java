package com.quiz.dto;

import com.quiz.entities.Question;
import lombok.Data;

@Data
public class GameQuestionsDto {
    int questionNumber;
    Question question;

    public GameQuestionsDto(int questionNumber, Question question) {
        this.questionNumber = questionNumber;
        this.question = question;
    }
}
