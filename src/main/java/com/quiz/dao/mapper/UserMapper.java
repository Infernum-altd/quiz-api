package com.quiz.dao.mapper;

import com.quiz.entities.Gender;
import com.quiz.entities.Role;
import com.quiz.entities.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserMapper implements RowMapper<User> {

    public static final String USERS_ID = "id";
    public static final String USERS_PASSWORD = "password";
    public static final String USERS_EMAIL = "email";
    public static final String USERS_GENDER = "gender";
    public static final String USERS_ROLE = "role";
    public static final String USERS_NAME = "name";
    public static final String USERS_SURNAME = "surname";
    public static final String USERS_BIRTHDATE = "birthdate";
    public static final String USERS_CITY = "city";
    public static final String USERS_ABOUT = "about";
    public static final String USERS_IMAGE = "image";


    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();

        user.setId(resultSet.getInt(USERS_ID));
        user.setPassword(resultSet.getString(USERS_PASSWORD));
        user.setEmail(resultSet.getString(USERS_EMAIL));
        user.setName(resultSet.getString(USERS_NAME));
        user.setSurname(resultSet.getString(USERS_SURNAME));
        user.setCity(resultSet.getString(USERS_CITY));
        user.setAbout(resultSet.getString(USERS_ABOUT));
        user.setGender(Gender.valueOf(resultSet.getString(USERS_GENDER)));
        user.setRole(Role.valueOf(resultSet.getString(USERS_ROLE)));
        user.setImage(resultSet.getBytes(USERS_IMAGE));
        user.setBirthdate(resultSet.getDate(USERS_BIRTHDATE));
        return user;
    }
}
