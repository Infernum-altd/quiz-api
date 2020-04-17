package com.quiz.controllers;

import com.quiz.service.AuthService;
import com.quiz.dto.UserDto;
import com.quiz.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    // TODO: 09.04.2020 validation
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody User user){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(user));
    }

    // TODO: 09.04.2020 dto for login
    @PostMapping(value ="/login")
    public ResponseToken login(@RequestBody User user) {
        return new ResponseToken(authService.login(user));
    }
}
