package com.quiz.dto;

import com.quiz.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    private int id;

    private String email;

    public Role role;

    private String name;

    private String surname;

    private byte[] image;

    private Date birthdate;

    public Gender gender;

    private String city;

    private String about;

    public UserDto(User user) {
        id = user.getId();
        email = user.getEmail();
    }
}
