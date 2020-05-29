package com.quiz.dto;

import lombok.Data;

@Data
public class AnswerDto {
    private int id;
    private int questionId;
    private String text;
    private boolean correct;
    private String image;
    private Integer nextAnswerId;

}
