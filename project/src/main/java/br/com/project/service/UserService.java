package br.com.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.project.models.User;
import br.com.project.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	public User save(User cadastro) {
		return userRepository.save(cadastro);
	}
	
	public boolean  existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}
	public boolean validatePassword(String senha) {
		String regex = "^(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";
		if (senha.matches(regex)) {
			return true;
		}
		return false;
	}
	
	public User register(User register) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String senhaEncoder = encoder.encode(register.getPassword());
		register.setPassword(senhaEncoder);
		return userRepository.save(register);
	}
}