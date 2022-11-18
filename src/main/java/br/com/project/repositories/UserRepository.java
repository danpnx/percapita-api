package br.com.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.project.models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByUsername(String username);

	Optional<User> findByUsername(String username);

	Optional<User> findByToken(String token);
}
