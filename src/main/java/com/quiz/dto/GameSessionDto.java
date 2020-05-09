package com.quiz.dto;

import lombok.Data;

@Data
public class GameSessionDto {
    int quizId;
    int hostId;
    int questionTimer;
    int maxUsersNumber;
}
