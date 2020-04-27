package com.quiz.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.quiz.dao.AnnouncementsDao;
import com.quiz.entities.Announcement;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AnnouncementService {
	
	private final AnnouncementsDao anDao;
	
	public List<Announcement> getAnnouncementList(int startIndex, int endIndex) {
		List<Announcement> result = anDao.getAnnouncementsByIdLimits(startIndex, endIndex);
		return result;
	}
	
	public List<Announcement> getAnnouncementList() {
		List<Announcement> result = anDao.getAllAnnouncements();
		return result;
	}

	public List<Announcement> getAnnouncementListByUserID(int user_id) {
		List<Announcement> result = anDao.getAnnouncementsByUserId(user_id);
		return result;
	}
	
	public List<Announcement> getAnnouncementListByGameID(int game_id) {
		List<Announcement> result = anDao.getAnnouncementsByUserId(game_id);
		return result;
	}
}
