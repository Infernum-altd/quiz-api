package com.quiz.dao;

import static com.quiz.dao.mapper.UserMapper.*;

import com.quiz.dao.mapper.QuizMapper;
import com.quiz.dao.mapper.UserMapper;
import com.quiz.entities.Gender;
import com.quiz.entities.Quiz;
import com.quiz.entities.Role;
import com.quiz.exceptions.DatabaseException;
import com.quiz.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    private final static String USER_FIND_BY_EMAIL = "SELECT id, email, password FROM users WHERE email = ?";
    private final static String USER_FIND_BY_ID = "SELECT id,email,password FROM users WHERE id = ?";
    private final static String USER_GET_ALL_FOR_PROFILE_BY_ID = "SELECT id, name, surname, birthdate, gender, city, about FROM users WHERE id = ?";
    private final static String FIND_FRIENDS_BY_USER_ID = "SELECT friend_id, name, surname, rating FROM users INNER JOIN friends ON id = user_id WHERE id = ?";
    private final static String INSERT_USER = "INSERT INTO users (email, password) VALUES (?,?)";
    private final static String INSERT_MODERATOR = "INSERT INTO users (email, password, role) VALUES (?,?,CAST(? AS role_type))";
    private final static String UPDATE_USER = "UPDATE users  SET name = ?, surname = ?, birthdate = ?, gender = ?, city = ?, about = ? WHERE id = ?";
    private final static String UPDATE_USER_PASSWORD = "UPDATE users SET password = ? WHERE id = ?";
    private static final String GET_USER_ID_BY_EMAIL = "SELECT id FROM users WHERE email = ?";
    private final static String FIND_ADMINS_USERS = "SELECT id,email,name,surname,role FROM users WHERE role = 'ADMIN' OR role = 'MODERATOR' OR role = 'SUPER_ADMIN'";
    private final static String DELETE_USER="DELETE FROM users WHERE id = ?";
    public static final String TABLE_USERS = "users";

    public User findByEmail(String email) {
        List<User> users;

       try {
            users = jdbcTemplate.query(
                    USER_FIND_BY_EMAIL,
                    new Object[]{email}, (resultSet, i) -> {
                        User user = new User();

                            user.setId(resultSet.getInt(USERS_ID));
                            user.setEmail(resultSet.getString(USERS_EMAIL));
                            user.setPassword(resultSet.getString(USERS_PASSWORD));

                        return user;
                    }
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
                    new Object[]{id}, (resultSet, i) -> {
                        User user = new User();

                        user.setId(resultSet.getInt(USERS_ID));
                        user.setEmail(resultSet.getString(USERS_EMAIL));
                        user.setPassword(resultSet.getString(USERS_PASSWORD));
                        return user;
                    }
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
            jdbcTemplate.update(INSERT_USER, entity.getEmail(), entity.getPassword());
            //entity.setId(id);
        } catch (DataAccessException e) {
          throw new DatabaseException("Database access exception while user insert");
        }

        return entity;
    }

    public User findProfileInfoByUserId(int id){
        List<User> users = jdbcTemplate.query(
                USER_GET_ALL_FOR_PROFILE_BY_ID,
                new Object[]{id}, (resultSet, i) -> {
                    User user = new User();

                    user.setName(resultSet.getString(USERS_NAME));
                    user.setSurname(resultSet.getString(USERS_SURNAME));
                    user.setBirthdate(resultSet.getDate(USERS_BIRTHDATE));
                    user.setGender(Gender.valueOf(resultSet.getString(USERS_GENDER)));
                    user.setCity(resultSet.getString(USERS_CITY));
                    user.setAbout(resultSet.getString(USERS_ABOUT));

                    return user;
                });

        if (users.isEmpty()){
            return null;
        }

        return users.get(0);
    }

    public List<User> findFriendByUserId(int id){
        List<User> friends = jdbcTemplate.query(
                FIND_FRIENDS_BY_USER_ID,
                new Object[]{id}, (resultSet, i) -> {
                    User user = new User();
                    user.setId(resultSet.getInt("friend_id"));
                    user.setName(resultSet.getString(USERS_NAME));
                    user.setSurname(resultSet.getString(USERS_SURNAME));
                    user.setRating(resultSet.getInt(USERS_RATING));

                    return user;
                });

        if (friends.isEmpty()){
            return null;
        }

        return friends;
    }

    public boolean updateUser(User user) {
        int affectedRowNumber = jdbcTemplate.update(UPDATE_USER, user.getName(),
                user.getSurname(), user.getBirthdate(),
                user.getGender(), user.getCity(),
                user.getAbout());

        return affectedRowNumber > 0;
    }

    public boolean updatePasswordById(int id, String newPassword) {
        int affectedNumberOfRows = jdbcTemplate.update(UPDATE_USER_PASSWORD, newPassword, id);
        return affectedNumberOfRows > 0;
    }

    public int getUserIdByEmail(String email) {
        List<Integer> id = jdbcTemplate.query(GET_USER_ID_BY_EMAIL, new Object[]{email}, (resultSet, i) -> {
             return resultSet.getInt("id");
        });

        return id.get(0);
    }
    public List<User> findAdminsUsers() {
        List<User> adminsUsers = jdbcTemplate.query(
                FIND_ADMINS_USERS, (resultSet, i) -> {
                    User user = new User();
                    user.setEmail(resultSet.getString(USERS_EMAIL));
                    user.setName(resultSet.getString(USERS_NAME));
                    user.setSurname(resultSet.getString(USERS_SURNAME));
                    user.setRole(Role.valueOf(resultSet.getString(USERS_ROLE).trim()));

                    return user;
                });

        if (adminsUsers.isEmpty()) {
            return null;
        }

        return adminsUsers;
    }
    @Transactional
    public User createAdminUsers(User entity,String role) {
        int id;
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps =
                                connection.prepareStatement(INSERT_MODERATOR, new String[] {"id"});
                        ps.setString(1, entity.getEmail());
                        ps.setString(2,entity.getPassword());
                        ps.setString(3,(role.equals("moderator")) ? Role.MODERATOR.toString() : Role.ADMIN.toString());
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
    public void deleteUserById(int id) {
        jdbcTemplate.update(DELETE_USER,id);
    }
}
