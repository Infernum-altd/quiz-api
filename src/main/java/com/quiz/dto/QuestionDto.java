package com.quiz.dto;

import com.quiz.entities.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    private int id;
    private int quizId;
    private QuestionType type;
    private String text;
    private boolean active;
    private int languageId;
    private String image;
    private List<AnswerDto> answerList;

}
