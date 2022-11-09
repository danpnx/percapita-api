package br.com.project.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.project.models.Tag;
import br.com.project.models.User;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {
	boolean existsByTagNameContainingIgnoreCaseAndUser(String tagName, User user);
	List<Tag> findAllByUser(Optional<User> findById);
	Tag findByTagNameAndUser(String tagName, User user);
}