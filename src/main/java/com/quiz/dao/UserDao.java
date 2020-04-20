package com.quiz.dao;

import static com.quiz.dao.mapper.UserMapper.*;

import com.quiz.dao.mapper.UserMapper;
import com.quiz.entities.Gender;
import com.quiz.entities.NotificationStatus;
import com.quiz.exceptions.DatabaseException;
import com.quiz.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository

@RequiredArgsConstructor
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    private final static String USER_FIND_BY_EMAIL = "SELECT id, email, password FROM users WHERE email = ?";
    private final static String USER_FIND_BY_ID = "SELECT id,email,password FROM users WHERE id = ?";
    private final static String USER_GET_ALL_FOR_PROFILE_BY_ID = "SELECT id, name, surname, birthdate, gender, city, about FROM users WHERE id = ?";
    private final static String FIND_FRIENDS_BY_USER_ID = "SELECT id, name, surname, rating FROM users where id in (SELECT friend_id FROM users INNER JOIN friends ON user_id = id WHERE id = ?)";
    private final static String INSERT_USER = "INSERT INTO users (email, password) VALUES (?,?)";
    private final static String UPDATE_USER = "UPDATE users  SET name = ?, surname = ?, birthdate = ?, gender = ?::gender_type, city = ?, about = ? WHERE id = ?";
    private final static String UPDATE_USER_PASSWORD = "UPDATE users SET password = ? WHERE id = ?";
    private final static String UPDATE_USER_IMAGE = "UPDATE users SET image = ? WHERE id = ?";
    private final static String GET_USER_ID_BY_EMAIL = "SELECT id FROM users WHERE email = ?";
    private final static String GET_USER_IMAGE_BY_USER_ID = "SELECT image FROM users WHERE id = ?";
    private final static String UPDATE_NOTIFICATION_STATUS = "UPDATE users SET notifications = ?::user_notification_type WHERE id = ?";
    private final static String GET_NOTIFICATION = "SELECT notifications from users WHERE id = ?";
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
                    user.setId(resultSet.getInt(USERS_ID));
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
                String.valueOf(user.getGender()), user.getCity(),
                user.getAbout(), user.getId());

        return affectedRowNumber > 0;
    }

    public boolean updatePasswordById(int id, String newPassword) {
        int affectedNumberOfRows = jdbcTemplate.update(UPDATE_USER_PASSWORD, newPassword, id);
        return affectedNumberOfRows > 0;
    }

    public int getUserIdByEmail(String email) {
        List<Integer> id = jdbcTemplate.query(GET_USER_ID_BY_EMAIL, new Object[]{email}, (resultSet, i) -> resultSet.getInt("id"));

        return id.get(0);
    }

    public boolean updateProfileImage(MultipartFile image, int userId) {
        int affectedNumbersOfRows = 0;
        try {
            affectedNumbersOfRows = jdbcTemplate.update(UPDATE_USER_IMAGE, image.getBytes(), userId);
        }catch (IOException e){
            e.printStackTrace();
        }
        return affectedNumbersOfRows > 0;
    }

    public byte[] getUserImageByUserId(int userId) {
        List<byte[]> imageBlob = jdbcTemplate.query(GET_USER_IMAGE_BY_USER_ID, new Object[]{userId}, (resultSet, i) -> resultSet.getBytes("image"));

        if (imageBlob.get(0) == null){
            return null;
        }
        return imageBlob.get(0);
    }

    public boolean updateNotificationStatus(String status, int userId) {
        int affectedNumberOfRows = jdbcTemplate.update(UPDATE_NOTIFICATION_STATUS, status, userId);
        return affectedNumberOfRows > 0;
    }

    public NotificationStatus getUserNotification(int userId) {
        return NotificationStatus.valueOf(jdbcTemplate.query(GET_NOTIFICATION, new Object[]{userId}, (resultSet, i) -> resultSet.getString("notifications")).get(0));
    }
}
