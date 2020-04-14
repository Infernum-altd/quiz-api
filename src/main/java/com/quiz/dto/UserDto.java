package com.quiz.dto;

import com.quiz.dao.mapper.UserMapper;
import com.quiz.entities.Gender;
import com.quiz.entities.Notifications;
import com.quiz.entities.Role;
import com.quiz.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        about=user.getAbout();
        name= user.getName();
        surname=user.getSurname();
        birthdate= user.getBirthdate();
        city=user.getCity();
        image= user.getImage();
        gender=user.getGender();
    }
}
