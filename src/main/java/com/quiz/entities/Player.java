package com.quiz.entities;

import lombok.Data;


@Data
public class Player implements Comparable<Player> {
    int userId;
    int userScore;
    String userName;
    boolean authorize;

    public Player(int userId, String userName) {
        this.userId = userId;
        this.userScore = 0;
        this.userName = userName;
    }

    @Override
    public int compareTo(Player player) {
        int result = Integer.compare(this.userScore, player.userScore);
        return result == 0 ? Integer.compare(this.userId, player.getUserId()) : result;
    }
}
