package com.quiz.service;

import com.quiz.dao.UserDao;
import com.quiz.dao.mapper.GameDao;
import com.quiz.dto.GameAnswersDto;
import com.quiz.dto.GameQuestionsDto;
import com.quiz.dto.GameSessionDto;
import com.quiz.entities.*;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class GameService {
    private ConcurrentHashMap<Integer, GameSession> currentGames = new ConcurrentHashMap<>();
    @Autowired
    QuestionService questionService;
    @Autowired
    GameDao gameDao;
    @Autowired
    UserDao userDao;

    public int addGameSession(int quizId, int hostId, int questionTimer, int maxUsersNumber) {
        User host = userDao.findById(hostId);
        GameSession gameSession = new GameSession(hostId,
                questionService.getQuestionsByQuizId(quizId));

        gameSession.getPlayerSet().add(new Player(host.getId(),host.getName() + " " + host.getSurname()));

        int gameId = createGame(quizId, hostId, questionTimer, maxUsersNumber);
        this.currentGames.put(gameId, gameSession);
        return gameId;
        //return new GameSessionDto(gameId, this.currentGames.get(gameId).getPlayerSet());
    }

    private int createGame(int quizId, int hostId, int questionTimer, int max_users_number){
        return gameDao.insetGame(quizId, hostId, questionTimer, max_users_number);
    }


    public GameSessionDto addUserInSession(int gameId, int userId){
        User user = userDao.findById(userId);
        if (this.currentGames.get(gameId).getPlayerSet().size() == this.gameDao.getUserNumberByGameId(gameId)) {
            throw new RuntimeException("The session is already full");
        }

        this.currentGames.get(gameId).getPlayerSet().add(new Player(user.getId(),user.getName() + " " + user.getSurname()));
        return new GameSessionDto(gameId, this.currentGames.get(gameId).getPlayerSet());
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

    public boolean handleAnswer(int gameId, int userId,  GameAnswersDto answer) {
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
        return this.currentGames.get(gameId).isAllAnswerCollected();
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

    public void onUserDisconnection(int userId, int gameId) {
        Player DisconnectedPlayer = this.currentGames.get(gameId).getPlayerSet().stream().filter(player -> player.getUserId()==userId).findFirst().get();
        this.gameDao.saveScore(userId, gameId, DisconnectedPlayer.getUserScore());
        this.currentGames.get(gameId).getPlayerSet().remove(DisconnectedPlayer);
    }
}
