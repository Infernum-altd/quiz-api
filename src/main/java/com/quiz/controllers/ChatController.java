package com.quiz.controllers;

import com.quiz.entities.Player;
import com.quiz.entities.Question;
import com.quiz.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.Set;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private GameService gameService;

    private SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/play/addSession")
    @SendTo("/play/gameSession")
    public int addGameSession(int quizId, int hostId, int questionTimer, int maxUsersNumber) {
        return gameService.addGameSession(quizId, hostId, questionTimer, maxUsersNumber);
    }

    @MessageMapping("/play/{quizId}/join")
    public Set<Player> userJoinGameSession(int gameId, int userId, SimpMessageHeaderAccessor headerAccessor) {

        String username = (String) headerAccessor.getSessionAttributes().put("userId", userId);

        return gameService.addUserInSession(gameId, userId);
    }


    @MessageMapping("play/{roomId}/question")
    public Question sendMessage(@DestinationVariable String roomId, Question question) {
        messagingTemplate.convertAndSend(roomId, question);
        return question;
    }
}
