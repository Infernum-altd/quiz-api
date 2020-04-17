package com.quiz.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.File;
import java.sql.Date;

import java.util.UUID;

@Data
@NoArgsConstructor
public class User {

    private int id;

    private String password;

    private String email;

    public Role role;

    private String name;

    private String surname;

    private byte[] image;

    private Date birthdate;

    public Gender gender;

    private int countryId;

    private String city;

    private int rating;

    private String about;

    private boolean active;

    private Notifications notifications;
}

