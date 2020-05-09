package com.quiz.dto;

import com.quiz.entities.Player;
import lombok.Data;

import java.util.Set;

@Data
public class GameSessionDto {
    private int quizId;
    private int hostId;
    private int questionTimer;
    private int maxUsersNumber;
    private Set<Player> players;

    public GameSessionDto(int quizId, Set<Player> players) {
        this.quizId = quizId;
        this.players = players;
    }
}
