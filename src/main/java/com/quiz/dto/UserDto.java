package com.quiz.dto;

import com.quiz.entities.Gender;
import com.quiz.entities.NotificationStatus;
import com.quiz.entities.Role;
import com.quiz.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UserDto {

    private int id;
    private String email;
    private Role role;
    private String name;
    private String surname;
    private Date birthdate;
    private Gender gender;
    private int country_id;
    private String city;
    private int rating;
    private String about;
    private boolean active;
    private NotificationStatus notification;
    private int languageId;

    public UserDto(User user) {
        id = user.getId();
        email = user.getEmail();
        role=user.getRole();
    }

    public UserDto(int id, String email, Role role, String name, String surname, Date birthdate, Gender gender, int country_id, String city, int rating, String about, boolean active, NotificationStatus notification, int languageId) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.name = name;
        this.surname = surname;
        this.birthdate = birthdate;
        this.gender = gender;
        this.country_id = country_id;
        this.city = city;
        this.rating = rating;
        this.about = about;
        this.active = active;
        this.notification = notification;
        this.languageId = languageId;
    }
}
