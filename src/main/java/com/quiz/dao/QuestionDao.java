package com.quiz.dao;

import com.quiz.entities.Question;
import com.quiz.entities.QuestionType;
import com.quiz.exceptions.DatabaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

import static com.quiz.dao.mapper.QuestionMapper.*;

@Repository
@RequiredArgsConstructor
public class QuestionDao {
    private final JdbcTemplate jdbcTemplate;

    private static final String QUESTION_FIND_BY_ID = "SELECT id, quiz_id, type, text, active FROM questions WHERE id = ?";
    private static final String QUESTION_FIND_BY_QUIZ_ID = "SELECT id, quiz_id, type, text, active FROM questions WHERE quiz_id = ?";

    private static final String INSERT_QUESTION = "INSERT INTO questions (quiz_id, type, text, active) VALUES ( ?, ?::question_type, ?, ?)";

    private static final String UPDATE_QUESTION = "UPDATE questions SET type=?, text=?, active=? WHERE id=?";

    public static final String TABLE_QUESTIONS = "questions";

    public Question findById(int id) {
        List<Question> questions;

        try {
            questions = getQuery(QUESTION_FIND_BY_ID, id);
            if (questions.isEmpty()) {
                return null;
            }

        } catch (DataAccessException e) {
            // TODO: 09.04.2020  check message
            throw new DatabaseException(String.format("Find question by id '%s' database error occured", id));
        }

        return questions.get(0);
    }

    private List<Question> getQuery(String sql, int id) {
        return jdbcTemplate.query(
                sql,
                new Object[]{id},
                (resultSet, i) -> {
                    Question question = new Question();

                    question.setId(resultSet.getInt(QUESTION_ID));
                    question.setQuizId(resultSet.getInt(QUESTION_QUIZ_ID));
                    question.setType(QuestionType.valueOf(resultSet.getString(QUESTION_TYPE)));
                    question.setText(resultSet.getString(QUESTION_TEXT));
                    question.setActive(resultSet.getBoolean(QUESTION_ACTIVE));

                    return question;
                }
        );
    }

    public List<Question> findQuestionsByQuizId(int id) {
        return getQuery(QUESTION_FIND_BY_QUIZ_ID, id);
    }

    @Transactional
    public Question insert(Question entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                        PreparedStatement ps = connection
                                .prepareStatement(INSERT_QUESTION, new String[]{"id"});
                        ps.setInt(1, entity.getQuizId());
                        ps.setString(2, String.valueOf(entity.getType()));
                        ps.setString(3, entity.getText());
                        ps.setBoolean(4, entity.isActive());
                        return ps;
                    },
                    keyHolder
            );
        } catch (DataAccessException e) {
            throw new DatabaseException("Database access exception while question insert");
        }

        entity.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        return entity;
    }

    public boolean updateQuestion(Question question) {
        int affectedRowNumber = jdbcTemplate.update(UPDATE_QUESTION,
                question.getType(),
                question.getText(),
                question.isActive(),
                question.getId());

        return affectedRowNumber > 0;
    }
}