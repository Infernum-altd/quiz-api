package com.quiz.controllers;

import com.quiz.dao.UserDao;
import com.quiz.entities.User;
import com.quiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    UserService userRepo;

    @GetMapping("/myprofile/{id}")
    public ResponseEntity<User> getUserProfile(@PathVariable int id){
        return ResponseEntity.ok(userRepo.findProfileInfoByUserId(id));
    }

    @GetMapping("/myfriends/{id}")
    public ResponseEntity<List<User>> showFriends(@PathVariable int id) {
        return ResponseEntity.ok(userRepo.findFriendByUserId(id));
    }

    @PostMapping("/myprofile/update")
    public ResponseEntity<User> updateUserProfile(@RequestBody User user){
        return ResponseEntity.ok(userRepo.updateUser(user));
    }
}
