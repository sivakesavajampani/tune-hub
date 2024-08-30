package com.kodnest.tunehub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.kodnest.tunehub.entity.Song;
import com.kodnest.tunehub.service.SongService;

@Controller
public class SongController {
	@Autowired
	SongService songService;

	@PostMapping("/addsong")
	public String addsong(@ModelAttribute Song song) {
		boolean songStatus = songService.songExists(song.getName());
		if(songStatus ==false) {
			songService.addSong(song);
			System.out.println("song added successfully");
		}else {
			System.out.println("song already exists");
		}
		return "adminhome";
	}
	
	@GetMapping("/viewsong")
	public String viewsong(Model model) {
		List<Song> songList = songService.fetchAllSongs();
		//		System.out.println(songList);
		model.addAttribute("songs",songList);
		return "display";
	}
	
	@GetMapping("/playsongs")
	public String playsongs(Model model) {
		boolean premium = false;
		if (premium) {
			List<Song> songList = songService.fetchAllSongs();
			model.addAttribute("songs",songList);
			return "display";
		}else {
			return "subscriptionform";
		} 
	}
}
