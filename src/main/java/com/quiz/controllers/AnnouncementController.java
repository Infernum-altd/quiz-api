package com.quiz.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.entities.Announcement;
import com.quiz.service.AnnouncementService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/announcements")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AnnouncementController {//add other crud operations, change time format, add announcementtypes
	
	@Autowired
	AnnouncementService anService;
	
	@GetMapping("/all")
	public List<Announcement> getAllAnnouncements() {
		return anService.getAnnouncementList();
	}
	
	@GetMapping
	public List<Announcement> getAnnouncementsIndexLimits(@RequestParam int startIndex, @RequestParam int size) {
		return anService.getAnnouncementList();
	}
	
	@PostMapping
	public ResponseEntity createNewAnnouncement() {
		
		return null;
	}
	
	
	
}
