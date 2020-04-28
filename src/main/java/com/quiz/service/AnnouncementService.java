package com.quiz.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.quiz.dao.AnnouncementsDao;
import com.quiz.entities.Announcement;
import com.quiz.entities.StatusType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AnnouncementService {
	
	private final AnnouncementsDao anDao;
	
	public List<Announcement> getAnnouncementList(int size){
		return anDao.getNLastAnnouncements(size);
	}
	
	public List<Announcement> getAnnouncementList(int startIndex, int size) {
		List<Announcement> result = anDao.getAnnouncementsByIdLimits(startIndex, size);
		return result;
	}
	
	public List<Announcement> getAnnouncementList() {
		List<Announcement> result = anDao.getAllAnnouncements();
		return result;
	}
	
	public Announcement getAnnouncementById(int id){
		return anDao.getAnnouncementById(id);
	}

	public List<Announcement> getAnnouncementListByUserID(int user_id) {
		List<Announcement> result = anDao.getAnnouncementsByUserId(user_id);
		return result;
	}
	
	public List<Announcement> getAnnouncementListByGameID(int game_id) {
		List<Announcement> result = anDao.getAnnouncementsByUserId(game_id);
		return result;
	}
	
	public List<Announcement> getAnnouncementListByStatus(StatusType status){
		return anDao.getAnnouncementsByStatus(status);
	}
	
	public boolean updateAnnouncementStatus(int id, StatusType status) {
		return anDao.updataAnnouncementStatus(id, status);
	}

	public boolean createNewAnnouncement(Announcement req) {
		// TODO Auto-generated method stub
		return anDao.createNewAnnouncement(req);
	}
}
