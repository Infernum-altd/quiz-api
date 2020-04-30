package com.quiz.dao;

import com.quiz.dao.mapper.QuizMapper;
import com.quiz.entities.Quiz;
import com.quiz.entities.StatusType;
import com.quiz.exceptions.DatabaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.quiz.dao.mapper.QuizMapper.*;

@Repository
@RequiredArgsConstructor
public class QuizDao {

    private final JdbcTemplate jdbcTemplate;

    private final static String GET_QUIZZES_BY_STATUS = "SELECT * FROM quizzes WHERE status = ?::status_type";
    private final static String GET_ALL_QUIZZES = "SELECT quizzes.id, quizzes.name, image, author, category_id, date, description, status, modification_time, categories.id, categories.name AS category FROM quizzes INNER JOIN categories ON categories.id = category_id WHERE quizzes.status = 'ACTIVE'";
    private final static String GET_QUIZ_BY_ID = "SELECT * FROM quizzes WHERE id = ?";
    private final static String GET_QUIZZES_CREATED_BY_USER_ID = "SELECT * FROM quizzes WHERE author = ? AND (status<>'DELETED' AND status<>'DEACTIVATED')";
    private final static String GET_FAVORITE_QUIZZES_BY_USER_ID = "SELECT * FROM quizzes INNER JOIN favorite_quizzes ON id = quiz_id WHERE user_id = ?";
    private final static String GET_QUIZZES_BY_CATEGORY_ID = "SELECT quizzes.id, quizzes.name, image, author, category_id, date, description, status, modification_time, categories.id, categories.name AS category FROM quizzes INNER JOIN categories ON categories.id = category_id WHERE (category_id = ?) AND (quizzes.status = 'ACTIVE')";
    private final static String GET_QUIZZES_BY_TAG = "SELECT * FROM quizzes INNER JOIN quizzes_tags on id = quiz_id where tag_id = ?";
    private final static String GET_QUIZZES_BY_NAME = "SELECT * FROM quizzes WHERE name LIKE ?";
    private final static String GET_QUIZ_IMAGE_BY_QUIZ_ID = "SELECT image FROM quizzes WHERE id = ?";
    private final static String INSERT_QUIZ = "INSERT INTO quizzes (name , author, category_id, date, description,status, modification_time) VALUES (?,?,?,?,?,?::status_type,?)";
    private final static String ADD_TAG_TO_QUIZ = "INSERT INTO quizzes_tags (quiz_id, tag_id) VALUES (?,?)";
    private final static String UPDATE_QUIZ = "UPDATE quizzes SET name = ?, author = ?, category_id = ?, date = ?, description = ?, status = ?::status_type, modification_time = ? WHERE id = ?";
    private final static String UPDATE_QUIZ_IMAGE = "UPDATE quizzes SET image = ? WHERE id = ?";
    private final static String GET_FILTERED_QUIZZES = "SELECT quizzes.id, quizzes.name, quizzes.image, author, category_id, date, description, status, " +
            "modification_time, categories.id, categories.name AS category, " +
            "users.name AS authorName, users.surname AS authorSurname " +
            "FROM quizzes INNER JOIN categories ON categories.id = category_id " +
            "INNER JOIN users ON quizzes.author = users.id " +
            "WHERE LOWER(quizzes.name) LIKE LOWER(?) OR " +
            "LOWER(categories.name) LIKE LOWER(?) OR " +
            "LOWER(users.name) LIKE LOWER(?) OR " +
            "LOWER(users.surname) LIKE LOWER(?) OR " +
            "date::text LIKE ?";

    private final static String IS_FAVORITE_QUIZ = "select * from favorite_quizzes WHERE quiz_id = ? AND user_id = ?";
    private final static String MARK_QUIZ_AS_FAVORITE = "INSERT INTO favorite_quizzes (user_id, quiz_id) VALUES(?, ?) ";
    private final static String UNMARK_QUIZ_AS_FAVORITE = "DELETE FROM favorite_quizzes where user_id = ? AND quiz_id = ?";
    private final static String GET_TAGS_BY_QUIZ_Id = "select name from tags INNER JOIN quizzes_tags ON tags.id = quizzes_tags.tag_id WHERE quizzes_tags.quiz_id = ?";

    //Functionality for dashboard
    public static final String GET_TOP_POPULAR_QUIZZES = "SELECT quizzes.id, quizzes.name, quizzes.author, quizzes.category_id, quizzes.date, quizzes.description, quizzes.status, quizzes.modification_time, COUNT(games.id) AS gamescount FROM games INNER JOIN quizzes ON games.id = quizzes.id WHERE category_id=3 GROUP BY quizzes.id ORDER BY gamescount DESC LIMIT ?";
    public static final String GET_TOP_POPULAR_QUIZZES_BY_CATEGORY = "SELECT quizzes.id, quizzes.name, quizzes.author, quizzes.category_id, quizzes.date, quizzes.description, quizzes.status, quizzes.modification_time, COUNT(games.id) AS gamescount FROM games INNER JOIN quizzes ON games.id = quizzes.id WHERE category_id=? GROUP BY quizzes.id ORDER BY gamescount DESC LIMIT ?";
    public static final String GET_RECENT_GAMES = "SELECT quizzes.id, quizzes.name, quizzes.author, quizzes.category_id, quizzes.date, quizzes.description, quizzes.status, quizzes.modification_time FROM games INNER JOIN quizzes ON games.id = quizzes.id WHERE games.id = (SELECT games.id FROM score WHERE user_id = ?) GROUP BY quizzes.id, games.date ORDER BY games.date DESC LIMIT ?";

    public static final String TABLE_QUIZZES = "quizzes";
    private final static String GET_GAMES_CREATED_BY_USER_ID = "SELECT * FROM quizzes WHERE author = ?";
    private final static String GET_FAVORITE_GAMES_BY_USER_ID = "SELECT * FROM quizzes INNER JOIN favorite_quizzes ON id = quiz_id WHERE user_id = ?";
    private final static String GET_QUIZ_CATEGORY_BY_CATEGORY_ID = "SELECT name FROM categories WHERE id = ?";

    public List<Quiz> getGamesCreatedByUser(int userId) {

        List<Quiz> quizzesCreatedByUser = jdbcTemplate.query(GET_GAMES_CREATED_BY_USER_ID, new Object[]{userId}, new QuizMapper());

        if (quizzesCreatedByUser.isEmpty()){
            return null;
        }

        return quizzesCreatedByUser;
    }

    public List<Quiz> getQuizzesByStatus(StatusType status) {

        List<Quiz> quizzesByStatus = jdbcTemplate.query(GET_QUIZZES_BY_STATUS, new Object[]{status}, new QuizMapper());

        if (quizzesByStatus.isEmpty()) {
            return null;
        }

        return quizzesByStatus;
    }

    public List<Quiz> getAllQuizzes(int userId) {
        List<Quiz> quizzes = jdbcTemplate.query(GET_ALL_QUIZZES, new QuizMapper());

        if (quizzes.isEmpty()) {
            return null;
        }

        for (Quiz quiz: quizzes) {
            quiz.setTags(getQuizTags(quiz.getId()));
           List<Integer> answer = jdbcTemplate.query(IS_FAVORITE_QUIZ, new Object[]{quiz.getId(), userId}, (resultSet, i) -> {return resultSet.getInt("quiz_id");
           });

            if (answer.isEmpty()) {
                quiz.setFavorite(false);
            } else {
                quiz.setFavorite(true);
            }
        }
        return quizzes;
    }

    public Quiz findById(int id) {
        List<Quiz> quizzes;

        try {
            quizzes = jdbcTemplate.query(
                    GET_QUIZ_BY_ID,
                    new Object[]{id}, (resultSet, i) -> {
                        Quiz quiz = new Quiz();

                        quiz.setId(resultSet.getInt(QUIZ_ID));
                        quiz.setName(resultSet.getString(QUIZ_NAME));
                        quiz.setAuthor(resultSet.getInt(QUIZ_AUTHOR));
                        quiz.setCategory_id(resultSet.getInt(QUIZ_CATEGORY));
                        quiz.setDate(resultSet.getDate(QUIZ_DATE));
                        quiz.setDescription(resultSet.getString(QUIZ_DESCRIPTION));
                        quiz.setStatus(StatusType.valueOf(resultSet.getString(QUIZ_STATUS)));
                        quiz.setModificationTime(resultSet.getTimestamp(QUIZ_MODIFICATION_TIME));
                        return quiz;
                    }
            );
            if (quizzes.isEmpty()) {
                return null;
            }
        } catch (DataAccessException e) {
            // TODO: 09.04.2020  check message
            throw new DatabaseException(String.format("Find quiz by id '%s' database error occured", id));
        }

        return quizzes.get(0);
    }

    public List<Quiz> getQuizzesCreatedByUser(int userId) {

        List<Quiz> quizzesCreatedByUser = jdbcTemplate.query(GET_QUIZZES_CREATED_BY_USER_ID, new Object[]{userId}, new QuizMapper());

        if (quizzesCreatedByUser.isEmpty()){
            return null;
        }

        return quizzesCreatedByUser;
    }

    public String getCategoryNameByCategoryId(int categoryId){
        List<String> categoryNames = jdbcTemplate.query(GET_QUIZ_CATEGORY_BY_CATEGORY_ID, new Object[]{categoryId}, (resultSet, i) -> resultSet.getString("name"));

        return categoryNames.get(0);
    }
    public List<Quiz> findQuizzesByName(String name) {

        List<Quiz> quizzesByName = jdbcTemplate.query(GET_QUIZZES_BY_NAME, new Object[]{"%" + name + "%"}, new QuizMapper());

        if (quizzesByName.isEmpty()) {
            return null;
        }

        return quizzesByName;
    }

    public List<Quiz> getFavoriteQuizzesByUserId(int userId) {
        List<Quiz> quizzesFavoriteByUser = jdbcTemplate.query(GET_FAVORITE_QUIZZES_BY_USER_ID, new Object[]{userId}, new QuizMapper());

        if (quizzesFavoriteByUser.isEmpty()) {
            return null;
        }

        return quizzesFavoriteByUser;
    }

    public List<Quiz> getQuizzesByCategory(int categoryId) {

        List<Quiz> quizzesByCategory = jdbcTemplate.query(GET_QUIZZES_BY_CATEGORY_ID, new Object[]{categoryId}, new QuizMapper());

        if (quizzesByCategory.isEmpty()) {
            return null;
        }

        return quizzesByCategory;
    }

    public List<Quiz> getQuizzesByTag(int tagId) {

        List<Quiz> quizzesByTag = jdbcTemplate.query(GET_QUIZZES_BY_TAG, new Object[]{tagId}, new QuizMapper());

        if (quizzesByTag.isEmpty()) {
            return null;
        }

        return quizzesByTag;
    }

    public byte[] getQuizImageByQuizId(int quizId) {
        List<byte[]> imageBlob = jdbcTemplate.query(
                GET_QUIZ_IMAGE_BY_QUIZ_ID,
                new Object[]{quizId},
                (resultSet, i) -> resultSet.getBytes("image"));
        if (imageBlob.get(0) == null) {
            return null;
        }
        return imageBlob.get(0);
    }

    @Transactional
    public Quiz insert(Quiz entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(INSERT_QUIZ, new String[]{"id"});
                ps.setString(1, entity.getName());
                ps.setInt(2, entity.getAuthor());
                ps.setInt(3, entity.getCategory_id());
                ps.setDate(4, entity.getDate());
                ps.setString(5, entity.getDescription());
                ps.setString(6, String.valueOf(entity.getStatus()));
                ps.setTimestamp(7, entity.getModificationTime());
                return ps;
            }, keyHolder);

        } catch (DataAccessException e) {
            throw new DatabaseException("Database access exception while quiz insert");
        }

        entity.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        return entity;
    }

    @Transactional
    public boolean addTagToQuiz(int quizId, int tagId) {
        int affectedRowNumber;
        try {
            affectedRowNumber = jdbcTemplate.update(ADD_TAG_TO_QUIZ, quizId, tagId);
        } catch (DataAccessException e) {
            throw new DatabaseException("Database access exception while quiz-tag insert");
        }
        return affectedRowNumber > 0;
    }

    public boolean updateQuiz(Quiz quiz) {
        int affectedRowNumber = jdbcTemplate.update(UPDATE_QUIZ, quiz.getName(),
                quiz.getAuthor(), quiz.getCategory_id(),
                quiz.getDate(), quiz.getDescription(),
                quiz.getStatus(), quiz.getModificationTime(), quiz.getId());

        return affectedRowNumber > 0;
    }

    public boolean updateQuizImage(MultipartFile image, int quizId) {
        int affectedNumberOfRows = 0;
        try {
            affectedNumberOfRows = jdbcTemplate.update(UPDATE_QUIZ_IMAGE, image.getBytes(), quizId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return affectedNumberOfRows > 0;
    }


    public List<Quiz> getTopPopularQuizzes(int limit) {
        List<Quiz> quizzes = jdbcTemplate.query(
                GET_TOP_POPULAR_QUIZZES,
                new Object[]{limit}, (resultSet, i) -> {
                    Quiz quiz = new Quiz();

                    quiz.setId(resultSet.getInt(QUIZ_ID));
                    quiz.setName(resultSet.getString(QUIZ_NAME));
                    quiz.setAuthor(resultSet.getInt(QUIZ_AUTHOR));
                    quiz.setCategory_id(resultSet.getInt(QUIZ_CATEGORY));
                    quiz.setDate(resultSet.getDate(QUIZ_DATE));
                    quiz.setDescription(resultSet.getString(QUIZ_DESCRIPTION));
                    quiz.setStatus(StatusType.valueOf(resultSet.getString(QUIZ_STATUS)));
                    quiz.setModificationTime(resultSet.getTimestamp(QUIZ_MODIFICATION_TIME));
                    return quiz;
                }
        );
        if (quizzes.isEmpty()) {
            return null;
        }

        return quizzes;
    }

    public List<Quiz> getTopPopularQuizzesByCategory(int categoryId, int limit) {
        List<Quiz> quizzes = jdbcTemplate.query(
                GET_TOP_POPULAR_QUIZZES_BY_CATEGORY,
                new Object[]{categoryId, limit}, (resultSet, i) -> {
                    Quiz quiz = new Quiz();

                    quiz.setId(resultSet.getInt(QUIZ_ID));
                    quiz.setName(resultSet.getString(QUIZ_NAME));
                    quiz.setAuthor(resultSet.getInt(QUIZ_AUTHOR));
                    quiz.setCategory_id(resultSet.getInt(QUIZ_CATEGORY));
                    quiz.setDate(resultSet.getDate(QUIZ_DATE));
                    quiz.setDescription(resultSet.getString(QUIZ_DESCRIPTION));
                    quiz.setStatus(StatusType.valueOf(resultSet.getString(QUIZ_STATUS)));
                    quiz.setModificationTime(resultSet.getTimestamp(QUIZ_MODIFICATION_TIME));
                    return quiz;
                }
        );
        if (quizzes.isEmpty()) {
            return null;
        }

        return quizzes;
    }

    public List<Quiz> getRecentGames(int userId, int limit) {
        List<Quiz> quizzes = jdbcTemplate.query(
                GET_RECENT_GAMES,
                new Object[]{userId, limit}, (resultSet, i) -> {
                    Quiz quiz = new Quiz();

                    quiz.setId(resultSet.getInt(QUIZ_ID));
                    quiz.setName(resultSet.getString(QUIZ_NAME));
                    quiz.setAuthor(resultSet.getInt(QUIZ_AUTHOR));
                    quiz.setCategory_id(resultSet.getInt(QUIZ_CATEGORY));
                    quiz.setDate(resultSet.getDate(QUIZ_DATE));
                    quiz.setDescription(resultSet.getString(QUIZ_DESCRIPTION));
                    quiz.setStatus(StatusType.valueOf(resultSet.getString(QUIZ_STATUS)));
                    quiz.setModificationTime(resultSet.getTimestamp(QUIZ_MODIFICATION_TIME));
                    return quiz;
                }
        );
        if (quizzes.isEmpty()) {
            return null;
        }

        return quizzes;
    }

    public List<Quiz> getQuizzesByFilter(String searchByUser) {
        String regEx = "'%" + searchByUser + "%'";
        List<Quiz> getFilteredQuizzes = jdbcTemplate.query("SELECT quizzes.id, quizzes.name, quizzes.image, author, category_id, date, description, status," +
                        "modification_time, categories.id, categories.name AS category, " +
                        "users.name AS authorName, users.surname AS authorSurname " +
                        "FROM quizzes INNER JOIN categories ON categories.id = category_id " +
                        "INNER JOIN users ON quizzes.author = users.id " +
                        "INNER JOIN quizzes_tags ON  quizzes.id = quizzes_tags.quiz_id " +
                        "INNER JOIN tags ON tags.id = quizzes_tags.tag_id " +
                        "WHERE (LOWER(quizzes.name) LIKE LOWER(" + regEx +") OR " +
                        "LOWER(categories.name) LIKE LOWER(" + regEx +") OR " +
                        "LOWER(users.name) LIKE LOWER(" + regEx +") OR " +
                        "LOWER(users.surname) LIKE LOWER(" + regEx +") OR " +
                        "LOWER(tags.name) LIKE LOWER('%hat%') OR " +
                        "date::text LIKE (" + regEx +")) AND (quizzes.status = 'ACTIVE')",
/*                GET_FILTERED_QUIZZES,*/
/*                new Object[]{regEx, regEx, regEx, regEx, regEx},*/
                new QuizMapper());

        if (getFilteredQuizzes.isEmpty()) {
            return null;
        }
        getFilteredQuizzes = getFilteredQuizzes.stream().distinct().collect(Collectors.toList());

        return getFilteredQuizzes.stream().distinct().collect(Collectors.toList());
    }

    public boolean markQuizAsFavorite(int quizId, int userId) {
        int affectedRowNumber = jdbcTemplate.update(MARK_QUIZ_AS_FAVORITE, userId, quizId);

        return affectedRowNumber > 0;
    }

    public boolean unmarkQuizAsFavorite(int quizId, int userId) {
        int affectedRowNumber = jdbcTemplate.update(UNMARK_QUIZ_AS_FAVORITE, userId, quizId);

        return affectedRowNumber > 0;
    }

    private List<String> getQuizTags(int quizId){
        List<String> tags = jdbcTemplate.query(
                GET_TAGS_BY_QUIZ_Id,
                new Object[]{quizId}, (resultSet, i) -> resultSet.getString("name")
        );
        if (tags.isEmpty()) {
            return null;
        }

        return tags;
    }
}

