package com.quiz.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.quiz.dao.mapper.AnnouncementMapper;
import com.quiz.entities.Announcement;
import com.quiz.exceptions.DatabaseException;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AnnouncementsDao {
	
	private final JdbcTemplate jdbcTemplate;
	
	 private final static String ANNOUNCEMENT_CONSTRAINTS_INDEXES = "SELECT id, user_id,game_id, desctiption, status, modification_time FROM announcements WHERE id > ? AND id < ?";
	 

	public List<Announcement> getAnnouncements(int startIndex, int endIndex) {
		List<Announcement> announcementList;
		try {
			announcementList = jdbcTemplate.query(
					ANNOUNCEMENT_CONSTRAINTS_INDEXES,
					new Object[] {startIndex, endIndex},
					new AnnouncementMapper());
		if(announcementList.isEmpty()) {
			return null;
		}
			
		}catch(Exception e) { 
			throw new DatabaseException(e.getMessage());
		}
		
		return announcementList ;
	}

	

}
