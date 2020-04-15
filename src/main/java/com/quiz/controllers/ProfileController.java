package com.quiz.controllers;

import com.quiz.entities.Quiz;
import com.quiz.entities.User;
import com.quiz.service.QuizService;
import com.quiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    UserService userRepo;

    @Autowired
    QuizService quizService;

    @GetMapping("/myprofile/{userId}")
    public ResponseEntity<User> getUserProfile(@PathVariable int userId){
        return ResponseEntity.ok(userRepo.findProfileInfoByUserId(userId));
    }

    @GetMapping("/myfriends/{userId}")
    public ResponseEntity<List<User>> showFriends(@PathVariable int userId) {
        return ResponseEntity.ok(userRepo.findFriendByUserId(userId));
    }

    @PostMapping("/myprofile/update")
    public ResponseEntity<String> updateUserProfile(@RequestBody User user){
        boolean isRecordAffected = userRepo.updateUser(user);

        if (isRecordAffected){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping("updatePassword/{userId}")
    public ResponseEntity<String> updatePassword(@RequestBody String newPassword, @PathVariable int userId){
        boolean isRecordAffected = userRepo.updatePasswordById(userId, newPassword);

        if (isRecordAffected){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/myquizzes/{userId}")
    public ResponseEntity<List<Quiz>> getUserQuizzes(@PathVariable int userId){
        return ResponseEntity.ok(quizService.findQuizzesCreatedByUserId(userId));
    }

    @GetMapping("/myfavorite/{userId}")
    public ResponseEntity<List<Quiz>> getFavoriteQuizzes(@PathVariable int userId){
        return ResponseEntity.ok(quizService.findFavoriteQuizzes(userId));
    }
}
