package com.quiz.dao;

import com.quiz.dao.mapper.UserMapper;
import com.quiz.exceptions.DatabaseException;
import com.quiz.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final static String USER_FIND_BY_EMAIL = "SELECT id,email,password FROM users WHERE email = ?";
    private final static String USER_FIND_BY_ID = "SELECT id,email,password FROM users WHERE id = ?";
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
            // TODO: 09.04.2020  check message
            throw new DatabaseException(String.format("Find user by email '%s' database error occured", email));
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
            // TODO: 09.04.2020  check message
            throw new DatabaseException(String.format("Find user by id '%s' database error occured", id));
        }

        return users.get(0);
    }

    @Transactional
    public User insert(User entity) {
        int id;

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName(TABLE_USERS)
                .usingGeneratedKeyColumns(UserMapper.USERS_ID);


        Map<String, Object> parameters = new HashMap<>();
        parameters.put(UserMapper.USERS_ID, entity.getId());
        parameters.put(UserMapper.USERS_EMAIL, entity.getEmail());
        parameters.put(UserMapper.USERS_PASSWORD, entity.getPassword());


        try {
            //id = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
            jdbcTemplate.update("insert into users (email, password) values(?,?)",entity.getEmail(), entity.getPassword());
            //id = jdbcTemplate.queryFor"select (id) from users where email =" + entity.getEmail());
            //entity.setId(id);
        } catch (DataAccessException e) {
            //throw new DatabaseException("Database access exception while user insert");
        }

        return entity;
    }
}
