/*package br.com.project.configuration;

import br.com.project.models.User;
import br.com.project.repositories.FinancialTransactionRepository;
import br.com.project.repositories.TagRepository;
import br.com.project.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.UUID;

// Classe para inserção de dados simulados
// Para acessar o banco de dados: http://localhost:8080/h2
// usuário: sa
// não possui senha
@Configuration
@Profile(value = "test")
public class TestConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FinancialTransactionRepository transactionRepository;
    private final TagRepository tagRepository;
    private final BCryptPasswordEncoder encoder;

    public TestConfig(UserRepository userRepository,
                      FinancialTransactionRepository transactionRepository,
                      TagRepository tagRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.tagRepository = tagRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // simulated users

        User u1 = new User(
                UUID.randomUUID(), "Daniel Nunes",
                "daniel@email.com", encoder.encode("daniel"));
        User u2 = new User(UUID.randomUUID(), "Anderson Chaves",
                "anderson@email.com", encoder.encode("anderson"));
        User u3 = new User(UUID.randomUUID(), "Sávia Chistine",
                "savia@email.com", encoder.encode("savia"));

        // saving simulated users
        userRepository.saveAll(Arrays.asList(u1, u2, u3));
    }
}*/
