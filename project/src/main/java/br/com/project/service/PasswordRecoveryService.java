package br.com.project.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.project.models.User;
import br.com.project.repositories.UserRepository;

@Service
@Transactional
public class PasswordRecoveryService {

	private static final long EXPIRE_TOKEN_AFTER_MINUTES = 30;

	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private UserRepository userRepository;
	

	public String forgotPassword(String username) {
		Optional<User> userOptional = userRepository.findByUsername(username);
		if (!userOptional.isPresent()) {
			return "Invalid email";
		}
		User user = userOptional.get();
		user.setToken(generateToken());
		user.setTokenCreationDate(LocalDateTime.now());
		user = userRepository.save(user);
		return user.getToken();
	}

	public String resetPassword(String token, String password) {

		Optional<User> userOptional = Optional.ofNullable(userRepository.findByToken(token));

		if (!userOptional.isPresent()) {
			return "Token inválido.";
		}

		LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();

		if (isTokenExpired(tokenCreationDate)) {
			return "O Token para sua recuperação de senha foi expirado.";

		}

		User user = userOptional.get();
		user.setPassword(password);
		user.setToken(null);
		user.setTokenCreationDate(null);
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String passwordEncoder = encoder.encode(user.getPassword());
		user.setPassword(passwordEncoder);
		userRepository.save(user);

		return "Senha atualizada com sucesso.";
	}

	private String generateToken() {
		StringBuilder token = new StringBuilder();

		return token.append(UUID.randomUUID().toString()).append(UUID.randomUUID().toString()).toString();
	}

	private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

		LocalDateTime now = LocalDateTime.now();
		Duration diff = Duration.between(tokenCreationDate, now);

		return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
	}

	public void sendEmail(String username, String response) {

		Optional<User> userOptional = userRepository.findByUsername(username);
		if (userOptional.isPresent()) {
			SimpleMailMessage mail = new SimpleMailMessage();
			mail.setFrom("anderson.c-10@hotmail.com");
			mail.setTo(username);
			mail.setSubject("Solicitação de recuperação de senha:");
			mail.setText("Olá, aqui está seu link de reset: " + response);

			mailSender.send(mail);
		}
	}

	public boolean validatePassword(String senha) {
		String regex = "^(?=.*[a-z])(?=.*[A-Z])"
					+"(?=.*[@#$%^&+=])"
					+ "(?=\\S+$).{8,20}$";
		
		if (senha.matches(regex)) {
			return true;
		}
		return false;
	}
}
