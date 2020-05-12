package com.quiz.dao;

import com.quiz.dao.mapper.GameSessionMapper;
import com.quiz.dto.GameSessionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Objects;

import static com.quiz.dao.mapper.GameSessionMapper.*;


@RequiredArgsConstructor
@Repository
public class GameDao {
    private final JdbcTemplate jdbcTemplate;

    private final static String INSERT_GAME = "INSERT INTO games (quiz_id, host_id, question_timer, date, max_users_number) VALUES (?, ?, ?, ?, ?)";
    private final static String GET_PLAYER_LIMIT = "SELECT max_users_number FROM games WHERE id =?";
    private final static String SAVE_SCORE = "INSERT INTO score (user_id, game_id, score) VALUES (?, ?, ?)";

    public static final String GET_GAME = "SELECT quiz_id, host_id, question_timer,max_users_number FROM games WHERE id = ?";

    public int insertGame(int quizId, int hostId, int questionTimer, int max_users_number) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_GAME, new String[]{"id"});
                    ps.setInt(1, quizId);
                    ps.setInt(2, hostId);
                    ps.setInt(3, questionTimer);
                    ps.setDate(4, Date.valueOf(LocalDate.now()));
                    ps.setInt(5, max_users_number);
                    return ps;
                }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public int getUserNumberByGameId(int gameId) {
        return jdbcTemplate.query(GET_PLAYER_LIMIT,
                new Object[]{gameId},
                (resultSet, i) -> resultSet.getInt("max_users_number")).get(0);
    }

    public void saveScore(int userId, int gameId, int score) {
        jdbcTemplate.update(SAVE_SCORE, userId, gameId, score);
    }

    public GameSessionDto getGame(int gameId) {
        return jdbcTemplate.queryForObject(GET_GAME, new Object[]{gameId}, new GameSessionMapper());
    }
}
