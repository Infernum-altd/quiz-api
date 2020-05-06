package com.quiz.service;

import com.quiz.dao.QuestionDao;
import com.quiz.dto.QuestionDto;
import com.quiz.entities.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionDao questionDao;

    public Question findById(int id) {
        return questionDao.findById(id);
    }

    public List<Question> findQuestionsByQuizId(int id) {
        return questionDao.findQuestionsByQuizId(id);
    }

    public QuestionDto insertQuestion(Question question) {
        questionDao.insert(question);
        return new QuestionDto(question);
    }

    public byte[] getQuestionByQuestionId(int questionId) {
        return questionDao.getQuestionImageByQuestionId(questionId);
    }

    public boolean updateQuestion(Question question) {
        return questionDao.updateQuestion(question);
    }

    public boolean updateImageByQuestionId(MultipartFile image, int answerId) {
        return questionDao.updateQuestionImage(image, answerId);
    }

    public List<Question> getQuestionsByQuizId(int quizId) {
        return questionDao.getQuestionsByQuizId(quizId);
    }
}
