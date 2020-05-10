package com.quiz.controllers;

import com.quiz.dto.GameAnswersDto;
import com.quiz.dto.GameQuestionsDto;
import com.quiz.dto.GameSessionDto;
import com.quiz.entities.Player;
import com.quiz.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;


@Controller
@RequiredArgsConstructor
public class GameController {
    private GameService gameService;
    private SimpMessagingTemplate template;

    @PostMapping("/play/addSession")
    public GameSessionDto addGameSession(@RequestBody  GameSessionDto gameSessionDto) {
        return gameService.addGameSession(gameSessionDto.getQuizId(), gameSessionDto.getHostId(),
                gameSessionDto.getQuestionTimer(), gameSessionDto.getMaxUsersNumber());
    }

    @SubscribeMapping("/play/{gameId}")
    public void userJoinGameSession(@DestinationVariable int gameId, int userId, SimpMessageHeaderAccessor headerAccessor) {
        String userHeaderAccessor = (String) headerAccessor.getSessionAttributes().put("userId", new int[]{userId, gameId});
        template.convertAndSend("/play/" + gameId, gameService.addUserInSession(gameId, userId));
    }

    @MessageMapping("/play/{gameId}/start")
    public void startGame(@DestinationVariable int gameId) {
        this.sendQuestion(gameId, this.gameService.nextQuestion(gameId));
    }

    @MessageMapping("play/{gameId}/{userId}/sendAnswer")
    public void receiveAnswer(@DestinationVariable int gameId, @DestinationVariable int userId, GameAnswersDto answers) {
        if (this.gameService.handleAnswer(gameId, userId, answers)) {
            this.sendQuestion(gameId, this.gameService.nextQuestion(gameId));
        }
    }

    @MessageMapping("/play/{gameId}/finish")
    public Set<Player> finishGame(@DestinationVariable int gameId){
        return gameService.deleteGameSession(gameId);
    }

    private void sendQuestion(int gameId, GameQuestionsDto gameQuestionsDto){
        template.convertAndSend("/play/" + gameId, gameQuestionsDto);
    }

    public void handleUserDisconnection(int userId, int gameId) {
        this.gameService.onUserDisconnection(userId, gameId);
    }
}
