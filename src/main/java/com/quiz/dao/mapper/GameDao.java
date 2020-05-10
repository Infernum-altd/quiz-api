package com.quiz.dao.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.Date;

@RequiredArgsConstructor
public class GameDao {
    private final JdbcTemplate jdbcTemplate;

    private final static String INSERT_GAME = "INSERT INTO games (quiz_id, host_id, question_timer, date, max_users_number) VALUES (?, ?, ?, ?, ?)";
    private final static String GET_PLAYER_LIMIT = "SELECT max_users_number FROM games WHERE id =?";
    private final static String SAVE_SCORE = "INSERT INTO score (user_id, game_id, score) VALUES (?, ?, ?)";

    public int insetGame(int quizId, int hostId, int questionTimer, int max_users_number) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        return jdbcTemplate.update(INSERT_GAME,
                    new Object[]{quizId, hostId, questionTimer, new Date(), max_users_number}, keyHolder);
    }

    public int getUserNumberByGameId(int gameId){
        return jdbcTemplate.query(GET_PLAYER_LIMIT,
                new Object[]{gameId},
                (resultSet, i) -> resultSet.getInt("max_users_number")).get(0);
    }

    public void saveScore(int userId, int gameId, int score) {
        jdbcTemplate.update(SAVE_SCORE, userId, gameId, score);
    }
}
