package com.quiz.service;

import com.quiz.dao.UserDao;
import com.quiz.dao.mapper.GameDao;
import com.quiz.dto.GameAnswersDto;
import com.quiz.dto.GameQuestionsDto;
import com.quiz.dto.GameSessionDto;
import com.quiz.entities.*;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class GameService {
    private ConcurrentHashMap<Integer, GameSession> currentGames;

    QuestionService questionService;
    GameDao gameDao;
    UserDao userDao;

    public GameSessionDto addGameSession(int quizId, int hostId, int questionTimer, int maxUsersNumber) {
        User host = userDao.findById(hostId);
        GameSession gameSession = new GameSession(hostId,
                questionService.getQuestionsByQuizId(quizId));

        gameSession.getPlayerSet().add(new Player(host.getId(),host.getName() + " " + host.getSurname()));

        int gameId = createGame(quizId, hostId, questionTimer, maxUsersNumber);
        this.currentGames.put(gameId, gameSession);
        /*return this.currentGames.get(gameId);*/
        return new GameSessionDto(gameId, this.currentGames.get(gameId).getPlayerSet());
    }

    private int createGame(int quizId, int hostId, int questionTimer, int max_users_number){
        return gameDao.insetGame(quizId, hostId, questionTimer, max_users_number);
    }


    public GameSession addUserInSession(int gameId, int userId){
        User user = userDao.findById(userId);
        this.currentGames.get(gameId).getPlayerSet().add(new Player(user.getId(),user.getName() + " " + user.getSurname()));
        return this.currentGames.get(gameId);
    }



    public Set<Player> deleteGameSession(int gameId) {
        GameSession finishSession = this.currentGames.get(gameId);
        Set<Player> players = finishSession.getPlayerSet();

        players.forEach(user -> userDao.insertUserScore(user.getUserId(), gameId, user.getUserScore()));
        this.currentGames.remove(gameId);
        return players;
    }

    public GameQuestionsDto nextQuestion(int gameId) {
        return this.currentGames.get(gameId).nextQuestion();
    }

    public int handleAnswer(int gameId, int userId,  GameAnswersDto answer) {
        QuestionType questionType = this.currentGames.get(gameId).getQuestions().get(answer.getAnswers().get(0).getQuestionId()).getType();

        switch (questionType) {
            case ANSWER:
                if (isRightAnswer(answer.getAnswers().get(0).getText(), answer.getAnswers().get(0).getQuestionId(),gameId)) {
                    this.currentGames.get(gameId).addScorePoint(4, userId);
                }
                break;
            case OPTION:
                if (isRightOption(answer.getAnswers())) {
                    this.currentGames.get(gameId).addScorePoint(2, userId);
                }
                    break;
            case BOOLEAN:
                if (isRightBoolean(answer.getAnswers().get(0))) {
                    this.currentGames.get(gameId).addScorePoint(1, userId);
                }
                    break;
            case SEQUENCE:
                if (isRightSequence(answer.getAnswers())) {
                    this.currentGames.get(gameId).addScorePoint(3, userId);
                }
                break;
        }
        return this.currentGames.get(gameId).incrementCollectedAnswer();
    }

    private boolean isRightAnswer(String text, int questionNumber, int gameId) {
        return this.currentGames.get(gameId).getQuestions().get(questionNumber).getText().toLowerCase().equals(text.toLowerCase());
    }

    private boolean isRightOption(List<Answer> answers) {
        for (Answer answer: answers) {
            if (!answer.isCorrect()) {
                return false;
            }
        }
        return false;
    }

    private boolean isRightBoolean(Answer answer) {
        return answer.isCorrect();
    }

    private boolean isRightSequence(List<Answer> answers) {
        for (int i = 0; i < answers.size()-1; i++) {
            if (answers.get(i).getNextAnswerId() != answers.get(i+1).getNextAnswerId()) {
                return false;
            }
        }
        return true;
    }
}
