package com.quiz.dao;

import com.quiz.dao.mapper.QuizMapper;
import com.quiz.entities.Quiz;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QuizDao {

    private final JdbcTemplate jdbcTemplate;

    private final static String GET_GAMES_CREATED_BY_USER_ID = "SELECT * FROM quizzes WHERE author = ?";
    private final static String GET_FAVORITE_GAMES_BY_USER_ID = "SELECT * FROM quizzes INNER JOIN favorite_quizzes ON id = quiz_id WHERE user_id = ?";
    private final static String GET_NOT_CHECKED_QUIZZES = "SELECT * FROM quizzes where status='PENDING'";

    public List<Quiz> getGamesCreatedByUser(int userId) {

        List<Quiz> quizzesCreatedByUser = jdbcTemplate.query(GET_GAMES_CREATED_BY_USER_ID, new Object[]{userId}, new QuizMapper());

        if (quizzesCreatedByUser.isEmpty()){
            return null;
        }

        return quizzesCreatedByUser;
    }

    public List<Quiz> getFavoriteGamesByUserId(int userId) {
        List<Quiz> quizzesCreatedByUser = jdbcTemplate.query(GET_FAVORITE_GAMES_BY_USER_ID, new Object[]{userId}, new QuizMapper());

        if (quizzesCreatedByUser.isEmpty()){
            return null;
        }

        return quizzesCreatedByUser;
    }

    public List<Quiz> getNotCheckedQuizzes() {
        List<Quiz> notCheckedQuizzes = jdbcTemplate.query(GET_NOT_CHECKED_QUIZZES, new Object[]{}, new QuizMapper());

        if (notCheckedQuizzes.isEmpty()){
            return null;
        }

        return notCheckedQuizzes;
    }

}
