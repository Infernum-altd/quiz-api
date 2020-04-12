package com.quiz.dto;

import com.quiz.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class UserDto {

    private UUID id;

    private String email;

    public UserDto(User user) {
        id = user.getId();
        email = user.getEmail();
    }
}
