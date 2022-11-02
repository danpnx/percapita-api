package br.com.project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import br.com.project.utils.Utilities;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.project.models.User;
import br.com.project.repositories.UserRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;

//	private final PasswordEncoder encoder;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}
	
	public HttpStatus register(User user) {

		// Senha inválida
		if(!Utilities.validatePassword(user.getPassword())) {
			return HttpStatus.BAD_REQUEST;
		}

		// Email já está em uso
		if(existsByUsername(user.getUsername())) {
			return HttpStatus.CONFLICT;
		}

		// Conta criada com sucesso
		String password = new BCryptPasswordEncoder().encode(user.getPassword());
		user.setPassword(password);
		userRepository.save(user);

		return HttpStatus.CREATED;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<User> u = userRepository.findByUsername(username);

		if(u.isEmpty()) {
			throw new UsernameNotFoundException("Username not found");
		}

		return new org.springframework.security.core.userdetails.User(
				u.get().getUsername(), u.get().getPassword(), u.get().getAuthorities()
		);
	}

	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public Optional<User> findByToken(String token) {
		return userRepository.findByToken(token);
	}

	public void updatePassword(String password, User user) {
		user.setPassword(new BCryptPasswordEncoder().encode(password));
		userRepository.save(user);
	}

	public String setTokenAndUpdate(User user) {
		user.setToken(Utilities.generateToken());
		user.setTokenCreationDate(LocalDateTime.now());
		userRepository.save(user);
		return user.getToken();
	}

	public void setTokenAndUpdate(User user, String token, LocalDateTime time) {
		user.setToken(token);
		user.setTokenCreationDate(time);
		userRepository.save(user);
	}
}