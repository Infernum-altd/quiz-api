package com.quiz.entities;

import com.quiz.dto.GameQuestionsDto;
import lombok.Data;


import java.util.List;

import java.util.Set;
import java.util.TreeSet;

@Data
public class GameSession {
    private int hostId;
    private List<Question> questions;
    private Set<Player> playerSet = new TreeSet<>();
    private int currentQuestion;
    private int collectedAnswers;

    public GameSession(int hostId, List<Question> questions) {
        this.hostId = hostId;
        this.questions = questions;
        this.currentQuestion = 0;
        this.collectedAnswers = 0;
    }

    public GameQuestionsDto nextQuestion(){
        if (this.collectedAnswers == this.playerSet.size()) {
            this.collectedAnswers = 0;
            this.currentQuestion++;
        }

        return new GameQuestionsDto(this.currentQuestion ,questions.get(this.currentQuestion));
    }

    public void addScorePoint(int score, int userId){
        this.playerSet.forEach(
                player -> {
                    if (player.getUserId() == userId) {
                        player.setUserScore(player.getUserScore() + score);
                    }
                }
        );
    }
}
