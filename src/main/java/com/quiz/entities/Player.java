package com.quiz.entities;

import lombok.Data;


@Data
public class Player implements Comparable<Player> {
    int userId;
    int userScore;
    String userName;

    public Player(int userId, String userName) {
        this.userId = userId;
        this.userScore = 0;
        this.userName = userName;
    }

    @Override
    public int compareTo(Player player) {
        return Integer.compare(this.userScore, player.getUserScore());
    }
}
