package com.kodnest.tunehub.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodnest.tunehub.entity.User;
import com.kodnest.tunehub.repository.UserRepository;
import com.kodnest.tunehub.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	public String addUser(User user) {
		userRepository.save(user);
		return "user success";
	}

	// to check the duplicate entries
	public boolean emailExists(String email) {
		if (userRepository.findByEmail(email) != null) {
			return true;
		} else {
			return false;
		}
	}

	//	@Override
	//	public boolean validateUser(String email, String password) {
	//		User user = userRepository.findByEmail(email);
	//		
	//		String dbpwd = user.getPassword();
	//		if(password.equals(dbpwd)) {
	//			return true;
	//		}else {
	//			return false;
	//		}
	//	}

	@Override
	public String getRole(String email) {
		User user = userRepository.findByEmail(email);
		return user.getRole();
	}

	@Override
	public String validateUser(String email, String password, String role) {
		User user = userRepository.findByEmail(email);
		String in = user.getPassword();
		String out = user.getRole();
		if (in.equals(password) && out.equals(role)) {
			if(out.equals("admin"))
				return "admin";
			else
				return "customer";
		} 
		else {
			return "false";
		}
	}

	@Override
	public User getUser(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public void updateUser(User user) {
		userRepository.save(user);	
	}
}
