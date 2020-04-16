package com.quiz.entities;

import lombok.Data;

@Data
public class ResponseToken {
    String token;

    String id;
    String email;

    public ResponseToken(String token, String id, String email) {
        this.token = token;
        this.id = id;
        this.email = email;
    }
}
