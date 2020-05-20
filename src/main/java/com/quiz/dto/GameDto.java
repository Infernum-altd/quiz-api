package com.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class GameDto {
    private String quizName;
    private Date date;
    private int PersonalScore;
}
