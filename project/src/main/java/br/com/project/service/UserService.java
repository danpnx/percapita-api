package br.com.project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import br.com.project.exceptions.DatabaseException;
import br.com.project.exceptions.InvalidInputException;
import br.com.project.utils.Utilities;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.project.models.User;
import br.com.project.repositories.UserRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;

	private final PasswordEncoder encoder;

	public UserService(UserRepository userRepository, PasswordEncoder encoder) {
		this.userRepository = userRepository;
		this.encoder = encoder;
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
		String password = encoder.encode(user.getPassword());
		user.setPassword(password);
		userRepository.save(user);

		return HttpStatus.CREATED;
	}

	// Method used to update password in reset-password
	public void updatePassword(String password, User user) {
		if(!Utilities.validatePassword(password)) {
			throw new InvalidInputException("A senha deve conter de 8 a 20 caracteres, pelo menos um caractere em maiúsculo e um caractere especial");
		}
		user.setPassword(encoder.encode(password));
		userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<User> u = userRepository.findByUsername(username);

		return new org.springframework.security.core.userdetails.User(
				u.get().getUsername(), u.get().getPassword(), u.get().getAuthorities()
		);
	}

	// Method to return a user
	public User findByUsername(String username) {
		return userRepository.findByUsername(username).orElseThrow(() -> new DatabaseException("Usuário não encontrado"));
	}

	// Method used in PasswordRecoveryService
	public Optional<User> findByToken(String token) {
		return userRepository.findByToken(token);
	}

	// Method used in PasswordRecoveryService
	public String setTokenAndUpdate(User user) {
		user.setToken(Utilities.generateToken());
		user.setTokenCreationDate(LocalDateTime.now());
		userRepository.save(user);
		return user.getToken();
	}

	// Method used in PasswordRecoveryService
	public void setTokenAndUpdate(User user, String token, LocalDateTime time) {
		user.setToken(token);
		user.setTokenCreationDate(time);
		userRepository.save(user);
	}
}