package com.quiz.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class User {

    private UUID id;

    private String password;

    private String email;
}
