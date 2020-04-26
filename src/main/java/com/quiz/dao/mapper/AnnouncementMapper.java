package com.quiz.dao.mapper;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.quiz.entities.Announcement;
import com.quiz.entities.StatusType;
@Component
public class AnnouncementMapper implements RowMapper<Announcement>{

	public static final String ANNOUNCEMENT_ID = "id";
	public static final String ANNOUNCEMENT_USER_ID = "user_id";
	public static final String ANNOUNCEMENT_GAME_ID = "game_id";
	public static final String ANNOUNCEMENT_DESCRIPTION = "description";
	public static final String ANNOUNCEMENT_STATUS= "status";
	public static final String ANNOUNCEMENT_MODIFICATION_TIME = "modification_time";
	
	@Override
	public Announcement mapRow(ResultSet rs, int row) throws SQLException{
		Announcement an = new Announcement();
		
		an.setId(rs.getInt(ANNOUNCEMENT_ID));
		an.setUser_id(rs.getInt(ANNOUNCEMENT_USER_ID));
		an.setGame_id(rs.getInt(ANNOUNCEMENT_GAME_ID));
		an.setDescription(rs.getString(ANNOUNCEMENT_DESCRIPTION));
		an.setStatus(StatusType.valueOf(rs.getString(ANNOUNCEMENT_STATUS)));
		an.setModification_time(rs.getDate(ANNOUNCEMENT_MODIFICATION_TIME));
		
		return an;
	}
}
