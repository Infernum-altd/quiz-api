package com.quiz.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.entities.Announcement;
import com.quiz.service.AnnouncementService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/announcements")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AnnouncementController {
	
	@Autowired
	AnnouncementService anService;
	
	@GetMapping
	public List<Announcement> getAnnouncement() {
		return anService.getActiveAnnouncementList(2,10);
	}
	
}
