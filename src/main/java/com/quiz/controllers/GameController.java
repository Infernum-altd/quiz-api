package com.quiz.controllers;

import com.quiz.dto.GameAnswersDto;
import com.quiz.dto.GameQuestionsDto;
import com.quiz.dto.GameSessionDto;
import com.quiz.entities.Player;
import com.quiz.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;


@RestController
@CrossOrigin
public class GameController {

    @Autowired
    private GameService gameService;
    @Autowired
    private SimpMessagingTemplate template;

    @PostMapping("/play/addSession")
    public int addGameSession(@RequestBody GameSessionDto gameSessionDto) {
        return gameService.addGameSession(gameSessionDto.getQuizId(), gameSessionDto.getHostId(),
                gameSessionDto.getQuestionTimer(), gameSessionDto.getMaxUsersNumber());
    }

    @MessageMapping("/play/game/{gameId}/user")
    public void userJoinGameSession(@DestinationVariable int gameId, @RequestBody Player player, SimpMessageHeaderAccessor headerAccessor) {
        String userHeaderAccessor = (String) headerAccessor.getSessionAttributes().put("userId", new int[]{player.getUserId(), gameId});
        template.convertAndSend("/play/game/" + gameId, gameService.addUserInSession(gameId, player));
    }

    @MessageMapping("/play/game/{gameId}/start")
    public void startGame(@DestinationVariable int gameId) {
        this.sendQuestion(gameId, this.gameService.nextQuestion(gameId));
    }

    @MessageMapping("play/game/{gameId}/user/{userId}/sendAnswer")
    public void receiveAnswer(@DestinationVariable int gameId, @DestinationVariable int userId, @RequestBody GameAnswersDto answers) {
        if (this.gameService.handleAnswer(gameId, userId, answers)) {
            this.sendQuestion(gameId, this.gameService.nextQuestion(gameId));
        }
    }

    @MessageMapping("/play/game/{gameId}/finish")
    public Set<Player> finishGame(@DestinationVariable int gameId) {
        return gameService.deleteGameSession(gameId);
    }

    private void sendQuestion(int gameId, GameQuestionsDto gameQuestionsDto) {
        template.convertAndSend("/play/game/" + gameId, gameQuestionsDto);
    }

    public void handleUserDisconnection(int userId, int gameId) {
        this.gameService.onUserDisconnection(userId, gameId);
    }
}
