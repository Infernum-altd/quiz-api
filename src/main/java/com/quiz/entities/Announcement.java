package com.quiz.entities;

import java.sql.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Announcement {
	private int id;
	private int userId;
	private int gameId;
	private String description;
	private StatusType status;
	private AnnouncementType announcementType;
	private Date modificationTime;
}
