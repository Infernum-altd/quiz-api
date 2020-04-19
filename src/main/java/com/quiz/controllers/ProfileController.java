package com.quiz.controllers;

import com.quiz.entities.Quiz;
import com.quiz.entities.ResponseText;
import com.quiz.entities.User;
import com.quiz.service.QuizService;
import com.quiz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProfileController {

    @Autowired
    UserService userRepo;
    @Autowired
    QuizService quizService;

    @GetMapping("/myprofile/{userId}")
    public ResponseEntity<User> getUserProfile(@PathVariable int userId){
        return ResponseEntity.status(HttpStatus.OK).body(userRepo.findProfileInfoByUserId(userId));
    }

    @GetMapping("/myfriends/{userId}")
    public ResponseEntity<List<User>> getFriends(@PathVariable int userId) {
        return ResponseEntity.ok(userRepo.findFriendByUserId(userId));
    }

    @PostMapping("/myprofile/update")
    public ResponseEntity<User> updateUserProfile(@RequestBody User user){
        boolean isRecordAffected = userRepo.updateUser(user);

        if (isRecordAffected){
            return ResponseEntity.ok(user);
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

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ResponseText> getCategoryNameByCategoryId(@PathVariable int categoryId){
        return ResponseEntity.ok(new ResponseText(quizService.getCategoryNameByCategoryId(categoryId)));
    }

    @PostMapping("/newicon/{userId}")
    public ResponseEntity<String> changeProfileIcon(@RequestParam(value = "image") MultipartFile image, @PathVariable int userId){
        boolean isRecordAffected = userRepo.updateProfileImage(image, userId);

        if (isRecordAffected){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/getimage/{userId}")
    public ResponseEntity<ResponseText> getUserImage(@PathVariable int userId){
        return ResponseEntity.ok(new ResponseText(new String(Base64.getEncoder().encode(userRepo.getImageByUserId(userId)))));
    }

    @PostMapping("/status/{userId}")
    public ResponseEntity<ResponseText> updateNotificationStatus(@RequestBody String status, @PathVariable int userId){
        boolean isRecordAffected = userRepo.changeNotificationStatus(status, userId);

        if (isRecordAffected){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("status/{userId}")
    public ResponseEntity<ResponseText> getUserNotificationStatus(@PathVariable int userId){
        return ResponseEntity.ok(new ResponseText(userRepo.getNotificationStatus(userId)));
    }
}
