package com.quiz.service;

import com.quiz.dao.AnswerDao;
import com.quiz.dao.QuestionDao;
import com.quiz.dao.QuizDao;
import com.quiz.dto.QuestionCheckDto;
import com.quiz.dto.QuizCheckDto;
import com.quiz.dto.QuizDto;
import com.quiz.entities.Answer;
import com.quiz.entities.Question;
import com.quiz.entities.Quiz;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class QuizCheckService {

    private final QuizDao quizDao;
    private final QuestionDao questionDao;
    private final AnswerDao answerDao;
    
    public QuizCheckDto getQuizCheck (int id) {
        List<Question> questions = questionDao.findQuestionsByQuizId(id);

        List<QuestionCheckDto> questionCheckDto= new ArrayList<>();
        for(Question question: questions){
            List<Answer> answers= answerDao.findAnswersByQuestionId(question.getId());
            QuestionCheckDto questionDto = new QuestionCheckDto(question, answers);
            questionCheckDto.add(questionDto);
        }
        QuizDto quizDto = null;//quizDao.findInfoById(id);
         return new QuizCheckDto(quizDto, questionCheckDto);
    }
    public boolean updateStatusById(int id, String status) {
        return quizDao.updateStatusById(id, status);
    }
    public boolean updateCommentById(int id, String comment) {
        return quizDao.updateCommentById(id, comment);
    }

    public boolean assignModerator(int quizId, int moderatorId) {
        return quizDao.assignModeratorById(quizId, moderatorId);
    }
}
