package com.quiz.dto;

import com.quiz.entities.StatusType;
import com.quiz.entities.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class QuizDto {
    private Integer id;
    private String name;
    private String image;
    private int author;
    private int category_id;
    private Date date;
    private String description;
    private StatusType status;
    private Timestamp modificationTime;
    private String category;
    private String authorName;
    private String authorSurname;
    private String authorEmail;
    private List<QuestionDto> questions;
    private List<Tag> tags;
    private boolean favorite;
    private boolean changed;
    private String moderatorComment;

    public QuizDto(int id, String name, int author, int category_id, Date date, String description, StatusType status, Timestamp modificationTime) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.category_id = category_id;
        this.date = date;
        this.description = description;
        this.status = status;
        this.modificationTime = modificationTime;
    }
    public QuizDto(int id, String name, int author, int category_id, Date date, String description, StatusType status, Timestamp modificationTime, String category,String authorName,String authorSurname, String authorEmail, String moderatorComment) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.category_id = category_id;
        this.date = date;
        this.description = description;
        this.status = status;
        this.modificationTime = modificationTime;
        this.category=category;
        this.authorName=authorName;
        this.authorSurname=authorSurname;
        this.authorEmail=authorEmail;
        this.moderatorComment=moderatorComment;
    }

    public QuizDto(Quiz quiz){
        this.id = quiz.getId();
        this.name = quiz.getName();
        this.author = quiz.getAuthor();
        this.category_id = quiz.getCategory_id();
        this.date = quiz.getDate();
        this.description = quiz.getDescription();
        this.status = quiz.getStatus();
        this.modificationTime = quiz.getModificationTime();
    }

}
