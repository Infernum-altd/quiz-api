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
	
	//sort by modification time or index ?
	 private final static String ANNOUNCEMENT_NO_CONSTRAINS = "SELECT * FROM announcements ORDER BY modification_time DESC;";
	 private final static String ANNOUNCEMENT_CONSTRAINTS_INDEXES = "SELECT id, user_id,game_id, description, status, modification_time FROM announcements WHERE id >= ? AND id < ? ORDER BY modification_time DESC";
	 private final static String ANNOUCNEMENT_CONSTRAINTS_USER_ID = "SELECT * FROM announcements WHERE user_id=? ORDER BY modification_time DESC;";
	 private final static String ANNOUCNEMENT_CONSTRAINTS_GAME_ID = "SELECT * FROM announcements WHERE game_id=? ORDER BY modification_time DESC;";
	 
	 // element go in decsending order -> starting frm 10 size 4 -> 10,9,8,7
	 // if starting index < size then iterate till id = 0
	 public List<Announcement> getAnnouncementsByIdLimits(int startIndex, int size) { 
		List<Announcement> announcementList;
		
		int endIndex = startIndex>size ? startIndex-size : 1;
		
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
	 
	 public List<Announcement> getAllAnnouncements() { 
			List<Announcement> announcementList;
			
			try {
				announcementList = jdbcTemplate.query(
						ANNOUNCEMENT_NO_CONSTRAINS,
						new Object[] {},
						new AnnouncementMapper());
				if(announcementList.isEmpty()) {
					return null;
				}
				
			}catch(Exception e) { 
				throw new DatabaseException(e.getMessage());
			}
			
			return announcementList ;
		}
	 
	  public List<Announcement> getAnnouncementsByUserId(int user_id) { 
			List<Announcement> announcementList;
			
			try {
				announcementList = jdbcTemplate.query(
						ANNOUCNEMENT_CONSTRAINTS_USER_ID,
						new Object[] {user_id},
						new AnnouncementMapper());
				if(announcementList.isEmpty()) {
					return null;
				}
				
			}catch(Exception e) { 
				throw new DatabaseException(e.getMessage());
			}
			
			return announcementList ;
		}
	  
	  public List<Announcement> getAnnouncementsByGameId(int game_id) { 
			List<Announcement> announcementList;
			
			try {
				announcementList = jdbcTemplate.query(
						ANNOUCNEMENT_CONSTRAINTS_GAME_ID,
						new Object[] {game_id},
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
