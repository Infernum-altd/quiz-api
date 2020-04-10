package com.quize.quizebackend.dto;

import com.quize.quizebackend.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    private int id;

    private String email;

    public UserDto(User user) {
        id = user.getId();
        email = user.getEmail();
    }
}
