package com.quiz.controllers;

import com.quiz.entities.*;
import com.quiz.service.PaginationService;
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
    @Autowired
    PaginationService paginationService;

    @GetMapping("/myprofile/{userId}")
    public ResponseEntity<User> getUserProfile(@PathVariable int userId){
        return ResponseEntity.status(HttpStatus.OK).body(userRepo.findProfileInfoByUserId(userId));
    }

    @GetMapping("/myfriends/{pageSize}/{pageNumber}/{userId}")
    public ResponseEntity<ResponcePaginatedList<User>> getFriends(@PathVariable int pageSize, @PathVariable int pageNumber, @PathVariable int userId, @RequestParam(defaultValue = "", required = false, value = "sort") String sort) {
        List<User> friends = userRepo.findFriendByUserId(userId, sort);
        return ResponseEntity.ok(new ResponcePaginatedList<>(paginationService.paginate(friends, pageSize, pageNumber), friends.size()));
    }

    @GetMapping("/myfriends/{userSearch}/{pageSize}/{pageNumber}/{userId}")
    public ResponseEntity<ResponcePaginatedList<User>> getFriends(@PathVariable String userSearch, @PathVariable int pageSize, @PathVariable int pageNumber, @PathVariable int userId, @RequestParam(defaultValue = "", required = false, value = "sort") String sort) {
        List<User> friends = userRepo.filterFriendByUserId(userSearch, userId, sort);
        return ResponseEntity.ok(new ResponcePaginatedList<>(paginationService.paginate(friends, pageSize, pageNumber), friends.size()));
    }

    @GetMapping("/adminUsers")
    public ResponseEntity<List<User>> getAdminsUsers(){
        return ResponseEntity.ok(userRepo.findAdminsUsers());
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

    @GetMapping("/myquizzes/{pageSize}/{pageNumber}/{userId}")
    public ResponseEntity<ResponcePaginatedList<Quiz>> getUserQuizzes(@PathVariable int pageSize, @PathVariable int pageNumber, @PathVariable int userId,  @RequestParam(defaultValue = "", required = false, value = "sort") String sort){
        List<Quiz> quizzes = quizService.findQuizzesCreatedByUserId(userId, sort);
        return ResponseEntity.ok(new ResponcePaginatedList<>(paginationService.paginate(quizzes, pageSize, pageNumber), quizzes.size()));
    }

    @GetMapping("/myquizzes/{userSearch}/{pageSize}/{pageNumber}/{userId}")
    public ResponseEntity<ResponcePaginatedList<Quiz>> getUserQuizzes(@PathVariable String userSearch, @PathVariable int pageSize, @PathVariable int pageNumber, @PathVariable int userId,  @RequestParam(defaultValue = "", required = false, value = "sort") String sort) {
        List<Quiz> quizzes = quizService.filterQuizzesByUserId(userSearch, userId, sort);
        return ResponseEntity.ok(new ResponcePaginatedList<>(paginationService.paginate(quizzes, pageSize, pageNumber), quizzes.size()));
    }

    @GetMapping("/myfavorite/{userId}/{pageSize}/{pageNumber}")
    public ResponseEntity<ResponcePaginatedList<Quiz>> getFavoriteQuizzes(@PathVariable int userId, @PathVariable int pageSize, @PathVariable int pageNumber){
        List<Quiz> quizzes = quizService.findFavoriteQuizzes(userId);
        return ResponseEntity.ok(new ResponcePaginatedList<>(paginationService.paginate(quizzes, pageSize, pageNumber), quizzes.size()));
    }

    @GetMapping("/myfavorite/{userSearch}/{userId}/{pageSize}/{pageNumber}")
    public ResponseEntity<ResponcePaginatedList<Quiz>> getFavoriteQuizzes(@PathVariable String userSearch, @PathVariable int userId, @PathVariable int pageSize, @PathVariable int pageNumber){
        List<Quiz> quizzes = quizService.searchInFavoriteQuizzes(userId, userSearch);
        return ResponseEntity.ok(new ResponcePaginatedList<>(paginationService.paginate(quizzes, pageSize, pageNumber), quizzes.size()));
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
    public ResponseEntity<String> updateNotificationStatus(@RequestBody String status, @PathVariable int userId){
        boolean isRecordAffected = userRepo.changeNotificationStatus(status, userId);

        if (isRecordAffected){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("status/{userId}")
    public ResponseEntity<NotificationStatus> getUserNotificationStatus(@PathVariable int userId){
        return ResponseEntity.ok(userRepo.getNotificationStatus(userId));
    }

//    @PostMapping("myprofile/create_moderator/{role}")
//    public ResponseEntity<UserDto> createAdminUsers(@RequestBody User adminUsers,@PathVariable String role){
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(userRepo.createAdminUsers(adminUsers, role));
//    }

    @DeleteMapping("/delete/{id}")
    void deleteUserById(@PathVariable int id) {
        userRepo.deleteUserById(id);
    }

//    @GetMapping("/not_checked_quizzes")
//    public ResponseEntity<List<Quiz>> getNotCheckedQuizzes(){
//        return ResponseEntity.ok(quizService.findNotCheckedQuizzes());
//    }

    @PostMapping("updateActive/{userId}")
    public ResponseEntity<String> updateStatus(@RequestBody String status, @PathVariable int userId){
        boolean isRecordAffected = userRepo.updateStatusById(userId);

        if (isRecordAffected){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
