package com.quiz.entities;

import lombok.Data;


import java.util.List;

import java.util.Set;
import java.util.TreeSet;

@Data
public class GameSession {
    int hostId;
    List<Question> questions;

    Set<Player> playerSet = new TreeSet<>();

    public GameSession(int hostId, List<Question> questions) {
        this.hostId = hostId;
        this.questions = questions;
    }
}
