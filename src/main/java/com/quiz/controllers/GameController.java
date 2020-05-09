package com.quiz.controllers;

import com.quiz.dto.GameAnswersDto;
import com.quiz.dto.GameQuestionsDto;
import com.quiz.dto.GameSessionDto;
import com.quiz.entities.Answer;
import com.quiz.entities.GameSession;
import com.quiz.entities.Player;
import com.quiz.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Set;


@Controller
@RequiredArgsConstructor
public class GameController {
    private GameService gameService;
    //private SimpMessageSendingOperations messagingTemplate;
    //private SimpMessagingTemplate template;



//private final SimpMessagingTemplate template;

/*    @Autowired
    GameController(SimpMessagingTemplate template){
        this.template = template;
    }

    @MessageMapping("/send/message")
    public void sendMessage(String message){
        System.out.println(message);
        this.template.convertAndSend("/message",  message);
    }*/

    @MessageMapping("/play/addSession")
    public GameSessionDto addGameSession(GameSessionDto gameSessionDto) {
        return gameService.addGameSession(gameSessionDto.getQuizId(), gameSessionDto.getHostId(),
                gameSessionDto.getQuestionTimer(), gameSessionDto.getMaxUsersNumber());
    }

    @MessageMapping("/play/{gameId}/join/{userId}")
    public GameSession userJoinGameSession(@DestinationVariable int gameId, @DestinationVariable int userId, SimpMessageHeaderAccessor headerAccessor) {
        String username = (String) headerAccessor.getSessionAttributes().put("userId", userId);
        return gameService.addUserInSession(gameId, userId);
    }

/*    @MessageMapping("/play/{gameId}/sendQuestion")
    public GameQuestionsDto sendQuestion(@DestinationVariable int gameId) {
        return this.gameService.nextQuestion(gameId);
    }*/

    @MessageMapping("play/{gameId}/{userId}/getAnswer")
    public GameQuestionsDto getAnswer(@DestinationVariable int gameId, @DestinationVariable int userId, GameAnswersDto answers) {
        this.gameService.handleAnswer(gameId, userId, answers);
        return this.gameService.nextQuestion(gameId);
    }

    @MessageMapping("/play/{gameId}/finish")
    public Set<Player> finishGame(@DestinationVariable int gameId){
        return gameService.deleteGameSession(gameId);
    }

}
