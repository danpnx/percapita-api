package br.com.project.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.project.models.User;
import br.com.project.models.UserLogin;
import br.com.project.repositories.UserRepository;

@Service
public class UserLoginService {

	@Autowired
	UserRepository userRepository;
	

	public User register(User user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String senhaEncoder = encoder.encode(user.getPassword());
		user.setPassword(senhaEncoder);
		return userRepository.save(user);
	}

	public Optional<UserLogin> login(Optional<UserLogin> user) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Optional<User> usuario = userRepository.findByUsername(user.get().getUsername());

		if (usuario.isPresent()) {
			if (encoder.matches(user.get().getSenha(), usuario.get().getPassword()))
				;
			{
				String auth = user.get().getUsername() + ":" + user.get().getSenha();
				byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));

				String authHeader = "Basic " + new String(encodedAuth);
				user.get().setToken(authHeader);
				user.get().setUsername(usuario.get().getUsername());

				return user;
			}
		}
		return null;
	}
}
