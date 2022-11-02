package br.com.project.service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

import br.com.project.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.project.models.User;
import br.com.project.repositories.UserRepository;

@Service
@Transactional
public class PasswordRecoveryService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserService userService;

	public String forgotPassword(String username) {
		Optional<User> userOptional = userService.findByUsername(username);

		if (userOptional.isEmpty()) {
			throw new RuntimeException("Email inválido!");
		}

		return userService.setTokenAndUpdate(userOptional.get());
	}

	public String resetPassword(String token, String password) {

		Optional<User> userOptional = userService.findByToken(token);

		if (userOptional.isEmpty()) {
			return "Token inválido!";
		}

		LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();

		if (Utilities.isTokenExpired(tokenCreationDate)) {
			return "O Token para a recuperação de senha foi expirado.";
		}

		userService.updatePassword(password, userOptional.get());
		userService.setTokenAndUpdate(userOptional.get(), null, null);

		return "Senha atualizada com sucesso.";
	}

	public void sendEmail(String username, String response) {

		Optional<User> userOptional = userService.findByUsername(username);

		if (userOptional.isPresent()) {
			SimpleMailMessage mail = new SimpleMailMessage();
			mail.setFrom("anderson.c-10@hotmail.com");
			mail.setTo(username);
			mail.setSubject("Solicitação de recuperação de senha:");
			mail.setText("Olá, " + userOptional.get().getName() +". Aqui está o link para resetar a senha: " + response + "\nCaso não tenha solicitado, por favor, ignore este email");

			mailSender.send(mail);
		} else {
			throw new RuntimeException("Fail to send email");
		}
	}
}
