package com.quiz.dto;

import com.quiz.entities.Question;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameQuestionsDto {
    int questionNumber;
    int questionTimer;
    Question question;
}
