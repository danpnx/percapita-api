package br.com.project.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.project.enums.TagCategory;
import br.com.project.models.Tag;
import br.com.project.models.User;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {

	public Optional<Tag> findByTagName(String name);
	public boolean existsByTagNameAndUser(String tagName, Optional<User> findById);
}