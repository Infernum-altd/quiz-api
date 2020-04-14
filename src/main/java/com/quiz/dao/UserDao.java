package com.quiz.dao;

import com.quiz.dao.mapper.UserMapper;
import com.quiz.exceptions.DatabaseException;
import com.quiz.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final static String USER_FIND_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    private final static String USER_FIND_BY_ID = "SELECT * FROM users WHERE id = ?";
    private final static String INSERT_USER = "INSERT INTO users (email,password,name,surname,image,birthdate, gender, city,about) VALUES(?,?,?,?,?,?,CAST(? AS gender_type),?,?)";
    public static final String TABLE_USERS = "users";

    public User findByEmail(String email) {
        List<User> users;

        try {
            users = jdbcTemplate.query(
                    USER_FIND_BY_EMAIL,
                    new Object[]{email}, new UserMapper()
            );
            if (users.isEmpty()) {
                return null;
            }
        } catch (DataAccessException e) {
            throw new DatabaseException(String.format("Find user by email '%s' database error occurred", email));
        }

        return users.get(0);
    }

    public User findById(int id) {
        List<User> users;

        try {
            users = jdbcTemplate.query(
                    USER_FIND_BY_ID,
                    new Object[]{id}, new UserMapper()
            );
            if (users.isEmpty()) {
                return null;
            }
        } catch (DataAccessException e) {
            throw new DatabaseException(String.format("Find user by id '%s' database error occurred", id));
        }

        return users.get(0);
    }

    @Transactional
    public User insert(User entity) {
        int id;

        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps =
                                connection.prepareStatement(INSERT_USER, new String[] {"id"});
                        ps.setString(1, entity.getEmail());
                        ps.setString(2,entity.getPassword());
                        ps.setString(3,entity.getName());
                        ps.setString(4,entity.getSurname());
                        ps.setBytes(5,entity.getImage());
                        ps.setDate(6, entity.getBirthdate());
                        ps.setString(7,entity.getGender().toString());
                        ps.setString(8,entity.getCity());
                        ps.setString(9,entity.getAbout());
                        return ps;
                    },
                    keyHolder);
            id = (Integer) keyHolder.getKey();
            entity.setId(id);
        } catch (DataAccessException e) {
            log.error("",e);
            throw new DatabaseException("Database access exception while user insert");
        }

        return entity;
    }
}
