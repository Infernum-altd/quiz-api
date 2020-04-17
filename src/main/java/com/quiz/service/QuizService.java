package com.quiz.service;

import com.quiz.dao.QuizDao;
import com.quiz.entities.Quiz;
import com.quiz.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuizService {

    private final QuizDao quizDao;

    public Quiz findById(int id) { return quizDao.findById(id); }

    public List<Quiz> findQuizzesCreatedByUserId(int userId) {
        return quizDao.getQuizzesCreatedByUser(userId);
    }

    public List<Quiz> findFavoriteQuizzes(int userId) {
        return quizDao.getFavoriteQuizzesByUserId(userId);
    }

    public List<Quiz> findQuizzesByCategory(int categoryId) {
        return quizDao.getQuizzesByCategory(categoryId);
    }

    public boolean updateQuiz(Quiz quiz) {
        return quizDao.updateQuiz(quiz);
    }

}
