package com.quiz.entities;

import com.quiz.dto.GameQuestionsDto;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.LongAdder;

@Data
public class GameSession {
    private final int hostId;
    private final List<Question> questions;
    private Set<Player> playerSet;
    private int currentQuestion;
    private LongAdder collectedAnswers;
    private int questionTimer;

    public GameSession(int hostId, List<Question> questions, int questionTimer) {
        this.hostId = hostId;
        this.questions = questions;
        this.currentQuestion = 0;
        this.playerSet = new ConcurrentSkipListSet<>();
        this.collectedAnswers = new LongAdder();
        this.questionTimer = questionTimer;
    }

    public synchronized GameQuestionsDto nextQuestion() {
        if (this.collectedAnswers.intValue() == this.playerSet.size()) {
            this.collectedAnswers.reset();
            this.currentQuestion++;
        }

        if (currentQuestion == questions.size()) {
            return null;
        }

        return new GameQuestionsDto(this.currentQuestion, this.questionTimer, questions.get(this.currentQuestion));
    }

    public void addScorePoint(int score, int userId) {
        for (Player player : this.playerSet) {
            if (player.getUserId() == userId) {
                player.setUserScore(player.getUserScore() + score);
            }
        }
    }

    public void addPlayer(Player player) {
        this.playerSet.add(player);
    }

    public int incrementCollectedAnswer() {
        return this.collectedAnswers.intValue();
    }

    public boolean isAllAnswerCollected() {
        this.collectedAnswers.increment();
        return this.collectedAnswers.intValue() == this.playerSet.size();
    }
}

