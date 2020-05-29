package com.quiz.dto;

import com.quiz.entities.Question;
import com.quiz.entities.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    private int id;
    private int quizId;
    private QuestionType type;
    private String text;
    private boolean active;
    private int languageId;
    private String image;
    private List<AnswerDto> answerList;

    public QuestionDto(Question question) {
        this.id = question.getId();
        this.quizId = question.getQuizId();
        this.type = question.getType();
        this.text = question.getText();
        this.active = question.isActive();
        this.languageId = question.getLanguageId();
    }

}
