package br.com.project.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.project.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	public boolean existsByUsername(String username);
	public Optional<User> findByUsername(String username);
	public User findByToken(String token);
}
