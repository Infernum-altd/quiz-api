package com.quiz.dto;

import com.quiz.entities.Achievement;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AchievementDto {
    private int id;
    private String name;
    private String description;
    private int categoryID;

    public AchievementDto(Achievement achievement) {
        this.id = achievement.getId();
        this.name = achievement.getName();
        this.description = achievement.getDescription();
        this.categoryID = achievement.getCategoryId();
    }
}
