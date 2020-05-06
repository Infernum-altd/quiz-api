package com.quiz.service;

import com.quiz.dao.UserDao;
import com.quiz.dao.mapper.GameDao;
import com.quiz.entities.GameSession;
import com.quiz.entities.Player;
import com.quiz.entities.User;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class GameService {
    private ConcurrentHashMap<Integer, GameSession> currentGames;

    QuestionService questionService;
    GameDao gameDao;
    UserDao userDao;

    public int addGameSession(int quizId, int hostId, int questionTimer, int maxUsersNumber) {
        User host = userDao.findById(hostId);
        GameSession gameSession = new GameSession(hostId,
                questionService.getQuestionsByQuizId(quizId));

        gameSession.getPlayerSet().add(new Player(host.getId(),host.getName() + " " + host.getSurname()));

        int gameId = createGame(quizId, hostId, questionTimer, maxUsersNumber);
        currentGames.put(gameId, gameSession);
        return gameId;
    }

    private int createGame(int quizId, int hostId, int questionTimer, int max_users_number){
        return gameDao.insetGame(quizId, hostId, questionTimer, max_users_number);
    }


    public Set<Player> userInSession(int gameId){
        return currentGames.get(gameId).getPlayerSet();
    }

    public Set<Player> addUserInSession(int gameId, int userId){
        User user = userDao.findById(userId);
        currentGames.get(gameId).getPlayerSet().add(new Player(user.getId(),user.getName() + " " + user.getSurname()));
        return currentGames.get(gameId).getPlayerSet();
    }

    public Set<Player> deleteGameSession(int gameId) {
        GameSession finishSession = currentGames.get(gameId);
        Set<Player> players = finishSession.getPlayerSet();

        players.forEach(user -> userDao.insertUserScore(user.getUserId(), gameId, user.getUserScore()));
        currentGames.remove(gameId);
        return players;
    }

}
