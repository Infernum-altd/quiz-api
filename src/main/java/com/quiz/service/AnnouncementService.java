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
	
	public List<Announcement> getActiveAnnouncementList(int startIndex, int endIndex) {
		List<Announcement> result = anDao.getAnnouncements(startIndex, endIndex);
		return result;
	}

}
