package com.quiz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.quiz.dao.mapper.AnnouncementMapper;
import com.quiz.entities.Announcement;
import com.quiz.entities.AnnouncementType;
import com.quiz.entities.StatusType;
import com.quiz.exceptions.DatabaseException;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AnnouncementsDao {
	
	private final JdbcTemplate jdbcTemplate;
	
	//sort by modification time or index ?
	 private final static String ANNOUNCEMENT_NO_CONSTRAINS = "SELECT * FROM announcements ORDER BY modification_time DESC;";
	 private final static String ANNOUNCEMENT_CONSTRAINTS_INDEXES = "SELECT * FROM announcements WHERE id >= ? ORDER BY modification_time DESC LIMIT ?;";
	 private final static String ANNOUNCEMENT_CONSTRAINTS_SIZE = "SELECT * FROM announcements ORDER BY modification_time DESC LIMIT ?;";
	 private final static String ANNOUCNEMENT_CONSTRAINTS_USER_ID = "SELECT * FROM announcements WHERE user_id=? ORDER BY modification_time DESC;";
	 private final static String ANNOUCNEMENT_CONSTRAINTS_GAME_ID = "SELECT * FROM announcements WHERE game_id=? ORDER BY modification_time DESC;";
	 private final static String ANNOUNCEMENT_CONSTRAINTS_STATUS = "SELECT * FROM announcements WHERE status=? ORDER BY modification_time DESC;";
	 private final static String GET_ANNOUNCMENT_BY_ID = "SELECT * FROM announcements WHERE id=?;";
	 
	 private final static String CREATE_NEW_ANNOUNCEMENT = "INSERT INTO announcements "
	 		+ "(user_id, game_id, description, status, announcement_type, modification_time)"
	 		+ " values (?, ?, ?, ?, ?, ?);";
	 private final static String UPDATE_STATUS = "UPDATE announcements SET status=? WHERE id=?";
	 
	 // element go in decsending order -> starting frm 10 size 4 -> 10,9,8,7
	 // if starting index < size then iterate till id = 0
	 public List<Announcement> getAnnouncementsByIdLimits(int startIndex, int size) { 
		List<Announcement> announcementList;
		
		try {
			announcementList = jdbcTemplate.query(
					ANNOUNCEMENT_CONSTRAINTS_INDEXES,
					new Object[] {startIndex, size},
					new AnnouncementMapper());
			if(announcementList.isEmpty()) {
				return null;
			}
			
		}catch(Exception e) { 
			throw new DatabaseException(e.getMessage());
		}
		
		return announcementList ;
	 }
	 
	 public List<Announcement> getNLastAnnouncements(int size) { 
			List<Announcement> announcementList;
			
			try {
				PreparedStatement ps = jdbcTemplate.getDataSource().
						getConnection().prepareStatement(ANNOUNCEMENT_CONSTRAINTS_SIZE);
				ps.setInt(1, size );
				
				
				announcementList = jdbcTemplate.query(
						new PreparedStatementCreator() {
							@Override
							public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
								PreparedStatement ps = con.prepareStatement(ANNOUNCEMENT_CONSTRAINTS_SIZE);
								ps.setInt(1, size);
								System.out.print(ps);
								return ps;
							}
						},
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
	 
	 
	 public Announcement getAnnouncementById(int id) { 
			Announcement result;
			
			try {
				result = jdbcTemplate.query(
						GET_ANNOUNCMENT_BY_ID,
						new Object[] {id},
						new ResultSetExtractor<Announcement>(){
							public Announcement extractData(ResultSet rs) throws SQLException {
								
								if(!rs.next())
									return null;
								
								Announcement an = new Announcement();
								an.setId(rs.getInt("id"));
								an.setUserId(rs.getInt("user_id"));
								an.setGameId(rs.getInt("game_id"));
								an.setDescription(rs.getString("description"));
								an.setStatus(StatusType.valueOf(rs.getString("status")));
								an.setAnnouncementType(AnnouncementType.valueOf(rs.getString("announcement_type")));
								an.setModificationTime(rs.getDate("modification_time"));
								rs.next();
								return an;
							}
						});
				if(result==null) {
					return null;
				}
				
			}catch(Exception e) { 
				throw new DatabaseException(e.getMessage());
			}
			
			return result ;
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
	  
	  public List<Announcement> getAnnouncementsByStatus(StatusType status) { 
			List<Announcement> announcementList;
			
			try {
				announcementList = jdbcTemplate.query(
						ANNOUNCEMENT_CONSTRAINTS_STATUS,
						new Object[] {status.name()},
						new AnnouncementMapper());
				if(announcementList.isEmpty()) {
					return null;
				}
				
			}catch(Exception e) { 
				throw new DatabaseException(e.getMessage());
			}
			
			return announcementList ;
		}
	  
	  public boolean createNewAnnouncement(Announcement an) {
		  boolean res;
		  try {
			  res = jdbcTemplate.execute(CREATE_NEW_ANNOUNCEMENT,
					  new PreparedStatementCallback<Boolean>() {
				  			public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException{
				  				ps.setInt(1,an.getUserId());
				  				ps.setInt(2,an.getGameId());
				  				ps.setString(3, an.getDescription());
				  				ps.setString(4, an.getStatus().name());
				  				ps.setString(5, an.getAnnouncementType().name());
				  				ps.setDate(6, an.getModificationTime());
				  				return ps.execute();
				  			}
			  });
		  }catch(Exception e ) {
			  throw new DatabaseException(e.getMessage());
		  }
		  
		  return res;
	  }


	public boolean updataAnnouncementStatus(int id, StatusType status) {
		// TODO Auto-generated method stub
		System.out.println("inside dao func\n");
		 int rowsAffected = jdbcTemplate.update(UPDATE_STATUS, status.name(), id);
		  return rowsAffected==1 ? true : false;
	}

}
