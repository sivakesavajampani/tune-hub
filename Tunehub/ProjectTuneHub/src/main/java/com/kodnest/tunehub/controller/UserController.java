package com.kodnest.tunehub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kodnest.tunehub.entity.Song;
import com.kodnest.tunehub.entity.User;
import com.kodnest.tunehub.service.SongService;
import com.kodnest.tunehub.serviceimpl.UserServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	@Autowired
	UserServiceImpl serviceImpl;

	@Autowired
	SongService songService;

	@PostMapping("/register")
	public String addUser(@ModelAttribute User user) {
		//email taken from the regisgtration form
		String email = user.getEmail();
		//checking if the email as enterd in registration form
		boolean status = serviceImpl.emailExists(email);
		if(status == false) {
			System.out.println(user.getUsername() +" " +user.getEmail() + " " + user.getPassword()+ " "+user.getGender() +" " + user.getRole()+" "+user.getAddress());
			serviceImpl.addUser(user);
			System.out.println("user Added");
		}else {
			System.out.println("User already Exist");
		}
		return "home";
	}

	//		@PostMapping("/validate")
	//		public String validate(@RequestParam("email") String email,@RequestParam String password) {
	//			if(serviceImpl.validateUser(email,password)== true){
	//				String role = serviceImpl.getRole(email);
	//				if(role.equals("admin")){
	//					return "adminhome";
	//				}else {
	//					return "customerhome";
	//				}
	//			}else{
	//				return "login";
	//			}
	//		}

	@PostMapping("/validate")
	public String validate(@RequestParam("email") String email,@RequestParam("password") String password,@RequestParam("role") String role,HttpSession session,Model model){
		@SuppressWarnings("unused")
		String role1 = serviceImpl.getRole(email);
		session.setAttribute("email", email);
		if(serviceImpl.validateUser(email, password,role).equals("admin"))
		{
			return "adminhome";
		}
		else if(serviceImpl.validateUser(email, password,role).equals("customer"))
		{
			User user = serviceImpl.getUser(email);
			boolean userstatus = user.isIspremium();
			List<Song> fetchAllSongs = songService.fetchAllSongs();
			model.addAttribute("songs",fetchAllSongs);
			model.addAttribute("ispremium",userstatus);

			return "customerhome";
		}
		else {
			return "login";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "login";
	}
}


