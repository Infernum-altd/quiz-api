package com.quiz.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.entities.Announcement;
import com.quiz.entities.StatusType;
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
	
	//announcements go in descending order to show newest than older ones
	@GetMapping("/byIndex")
	public List<Announcement> getAnnouncements(@RequestParam int startIndex, @RequestParam int size) {
		return anService.getAnnouncementList(startIndex, size);
	}
	
	//get $size last announcements
	@GetMapping("/fewLast")
	public List<Announcement> getAnnouncements(@RequestParam int size){
		return anService.getAnnouncementList(size);
	}
	
	@GetMapping("/getAnnouncementById")
	public Announcement getAnnouncementsById(@RequestParam int id){
		return anService.getAnnouncementById( id);
	}
	
	@GetMapping("/byUserId")
	public List<Announcement> getAnnouncementsByUserId(@RequestParam int user_id){
		return anService.getAnnouncementListByUserID(user_id);
	}
	
	@GetMapping("/byGameId")
	public List<Announcement> getAnnouncementsByGameId(@RequestParam int game_id){
		return anService.getAnnouncementListByGameID(game_id);
	}
	
	@GetMapping("/byStatusType")
	public List<Announcement> getAnnouncements(@RequestParam StatusType status){
		return anService.getAnnouncementListByStatus(status);
	}
	
	//curl request for new announcement:
	//curl -H "Content-Type: application/json" -v --data "{\"id\":\"0\",\"userId\":\"2\",\"gameId\":\"3\",\"description\":\"12341\",\"status\":\"PENDING\",\"announcementType\":\"PUBLIC_GAME\",\"modificationTime\":\"1898-11-08T04:05:06\"}" localhost:8080/announcements/newAnnouncement
	@PostMapping("/newAnnouncement")
	public ResponseEntity<String> createNewAnnouncement(@RequestBody Announcement req, HttpServletResponse res) {

		boolean result = anService.createNewAnnouncement(req);
		
		if(result)
			return ResponseEntity.status(HttpStatus.CREATED).build();
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	//curl request for updating status for give id
	//curl -v -H "Content-Type: application/json" --data "\"PENDING\"" localhost:8080/announcements/status/1
	@PostMapping("/status/{id}")
	public ResponseEntity<String> updateAnnouncementStatus(@RequestBody StatusType status, @PathVariable Integer id, HttpServletResponse res) {
		//System.out.println(status.name() + " " + id);
		
		System.out.println(status);
	
		boolean result = anService.updateAnnouncementStatus(id,status); 
		//boolean result = anService.updateAnnouncementStatus(id,StatusType.valueOf(status)); 
		
		if(result)
			return ResponseEntity.status(HttpStatus.CREATED).build();
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		//return "RETURN" + id;
	}
	
	
}
