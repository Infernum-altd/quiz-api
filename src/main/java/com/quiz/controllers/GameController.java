package com.quiz.controllers;

import com.quiz.dto.GameAnswersDto;
import com.quiz.dto.GameQuestionsDto;
import com.quiz.dto.GameSessionDto;
import com.quiz.entities.Player;
import com.quiz.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Set;


@Controller
@RequiredArgsConstructor
public class GameController {
    private GameService gameService;
    private SimpMessagingTemplate template;

    @PostMapping("/play/addSession")
    public GameSessionDto addGameSession(GameSessionDto gameSessionDto) {
        return gameService.addGameSession(gameSessionDto.getQuizId(), gameSessionDto.getHostId(),
                gameSessionDto.getQuestionTimer(), gameSessionDto.getMaxUsersNumber());
    }

    @SubscribeMapping("/play/{gameId}")
    public GameSessionDto userJoinGameSession(@DestinationVariable int gameId, int userId) {
        return gameService.addUserInSession(gameId, userId);
    }

    @MessageMapping("/play/{gameId}/start")
    public void startGame(@DestinationVariable int gameId) {
        this.sendQuestion(gameId, this.gameService.nextQuestion(gameId));
    }

    @MessageMapping("play/{gameId}/{userId}/sendAnswer")
    public void receiveAnswer(@DestinationVariable int gameId, @DestinationVariable int userId, GameAnswersDto answers) {
        this.gameService.handleAnswer(gameId, userId, answers);
        this.sendQuestion(gameId, this.gameService.nextQuestion(gameId));
    }

    @MessageMapping("/play/{gameId}/finish")
    public Set<Player> finishGame(@DestinationVariable int gameId){
        return gameService.deleteGameSession(gameId);
    }

    private void sendQuestion(int gameId, GameQuestionsDto gameQuestionsDto){
        template.convertAndSend("/play/" + gameId, gameQuestionsDto);
    }

}
