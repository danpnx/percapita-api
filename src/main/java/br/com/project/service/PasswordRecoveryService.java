package br.com.project.service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

import br.com.project.exceptions.InvalidTokenException;
import br.com.project.exceptions.RecoverPasswordTokenExpiredException;
import br.com.project.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.com.project.models.User;

@Service
@Transactional
public class PasswordRecoveryService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserService userService;

	public String forgotPassword(String username) {
		User user = userService.findByUsername(username);

		return userService.setTokenAndUpdate(user);
	}

	public String resetPassword(String token, String password) {

		Optional<User> userOptional = userService.findByToken(token);

		if (userOptional.isEmpty()) {
			throw new InvalidTokenException("Token inválido");
		}

		LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();

		if (TokenUtils.isTokenExpired(tokenCreationDate)) {
			throw new RecoverPasswordTokenExpiredException("O Token para a recuperação de senha foi expirado");
		}

		userService.updatePassword(password, userOptional.get());
		userService.setTokenAndUpdate(userOptional.get(), null, null);

		return "Senha atualizada com sucesso.";
	}

	public void sendEmail(String username, String response) {

		User user = userService.findByUsername(username);

		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom("suportepercapita@outlook.com");
		mail.setTo(username);
		mail.setSubject("Solicitação de recuperação de senha:");
		mail.setText("Olá, " + user.getName() +". Aqui está o link para resetar a senha: " + response + "\n"
				     + "Caso não tenha solicitado, por favor, ignore este email");
		mailSender.send(mail);
	}
}
