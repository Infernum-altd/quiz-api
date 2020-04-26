package com.quiz.entities;

import java.sql.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Announcement {
	private int id;
	private int user_id;
	private int game_id;
	private String description;
	private Date modification_time;
}
