package br.com.project.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.project.models.User;
import br.com.project.repositories.UserRepository;

@Service
public class EditProfileService {
	
	@Autowired
	UserRepository userRepository;
	
	public HttpStatus editName(Long username, String newName) {
		Optional<User> userOptional = userRepository.findById(username);
		if(username != null) {
			User user = userOptional.get();
			user.setName(newName);
			userRepository.save(user);
			return HttpStatus.ACCEPTED;
		}
		return HttpStatus.BAD_REQUEST;
	}
	
	public HttpStatus editPassword(Long username, String newPassword) {
		Optional<User> userOptional = userRepository.findById(username);
		if(username != null) {
			User user = userOptional.get();
			user.setPassword(newPassword);
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String passwordEncoder = encoder.encode(user.getPassword());
			user.setPassword(passwordEncoder);
			userRepository.save(user);
			return HttpStatus.ACCEPTED;
		}
			return HttpStatus.BAD_REQUEST;
	}
}