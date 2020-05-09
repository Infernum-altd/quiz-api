package com.quiz.dto;

import com.quiz.entities.Answer;
import lombok.Data;

import java.util.List;

@Data
public class GameAnswersDto {
    private List<Answer> answers;
}
