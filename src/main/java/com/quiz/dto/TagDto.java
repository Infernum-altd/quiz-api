package com.quiz.dto;

import com.quiz.entities.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TagDto {
    private int id;
    private String name;

    public TagDto(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
    }

    public TagDto(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
