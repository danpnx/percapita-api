package br.com.project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import br.com.project.exceptions.DatabaseException;
import br.com.project.exceptions.InvalidInputException;
import br.com.project.utils.TokenUtils;
import br.com.project.utils.InputUtils;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.project.models.User;
import br.com.project.repositories.UserRepository;

@Service
@Transactional
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;

	private final PasswordEncoder encoder;
	
	@Autowired
	private JavaMailSender mailSender;

	public UserService(UserRepository userRepository, PasswordEncoder encoder) {
		this.userRepository = userRepository;
		this.encoder = encoder;
	}
	
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}
	
	public HttpStatus register(User user) {

		// Validando email
		if(existsByUsername(user.getUsername())) {
			return HttpStatus.CONFLICT;
		}

		if(user.getUsername() == null || user.getUsername().equals("")) {
			throw new InvalidInputException("Digite um email válido");
		}

		if(InputUtils.isExceedingUsernameSize(user.getUsername())) {
			throw new InvalidInputException("O email não deve possuir mais que 100 caracteres");
		}

		// Validando nome do usuário
		if(user.getName() == null || user.getName().equals("")) {
			throw new InvalidInputException("Digite o seu nome");
		}

		if(InputUtils.isExceedingCompleteNameSize(user.getName())) {
			throw new InvalidInputException("O nome não deve exceder 60 caracteres");
		}

		// Validando senha do usuário
		if(user.getPassword() == null || user.getPassword().equals("")) {
			throw new InvalidInputException("A sua senha não deve exceder 20 caracteres");
		}

		if(!InputUtils.validatePassword(user.getPassword()) || user.getPassword().length() < 8) {
			return HttpStatus.BAD_REQUEST;
		}

		if(InputUtils.isExceedingPasswordSize(user.getPassword())) {
			throw new InvalidInputException("A sua senha não deve exceder 20 caracteres");
		}

		// Conta criada com sucesso
		String password = encoder.encode(user.getPassword());
		user.setPassword(password);
		userRepository.save(user);
		sendEmail(user.getUsername());
		return HttpStatus.CREATED;
	}

	// Method used to update password in reset-password
	public void updatePassword(String password, User user) {
		if(!InputUtils.validatePassword(password) || password.length() < 8) {
			throw new InvalidInputException("A senha deve conter de 8 a 20 caracteres, pelo menos um caractere em maiúsculo e um caractere especial");
		}

		if(InputUtils.isExceedingPasswordSize(password)) {
			throw new InvalidInputException("A sua senha não deve exceder 20 caracteres");
		}

		user.setPassword(encoder.encode(password));
		userRepository.save(user);
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
		user.setToken(TokenUtils.generateToken());
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

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<User> u = userRepository.findByUsername(username);

		return new org.springframework.security.core.userdetails.User(
				u.get().getUsername(), u.get().getPassword(), u.get().getAuthorities());
	}
	
	public void sendEmail(String username) {

		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom("suportepercapita@outlook.com");
		mail.setTo(username);
		mail.setSubject("Criação de conta percapita");
		mail.setText(" Ficamos felizes em receber seu cadastro em nosso aplicativo de gestão financeira, Percapita! \n\n"
				+ " Para realizar o seu login, basta acessar nossa página de login: http://localhost:8080/login \n\n"
				+ " Obrigado por nos deixar organizar suas transações ;D" );
		mailSender.send(mail);
	}
}