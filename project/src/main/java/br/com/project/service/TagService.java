package br.com.project.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.project.exceptions.DatabaseException;
import br.com.project.models.Tag;
import br.com.project.models.User;
import br.com.project.repositories.TagRepository;
import br.com.project.repositories.UserRepository;

@Service
public class TagService {

	@Autowired
	private TagRepository tagRepository;
	@Autowired
	private UserRepository userRepository;

	public TagService(TagRepository tagRepository, UserRepository userRepository) {
		this.tagRepository = tagRepository;
		this.userRepository = userRepository;
	}

	public boolean existsByTagNameAndUser(String tagName, String username) {
		return tagRepository.existsByTagNameAndUser(tagName, userRepository.findByUsername(username));
	}

	private Tag getTag(UUID tagId, String username) {
		try {
			Optional<Tag> optionalTag = tagRepository.findById(tagId);

			if (optionalTag.isEmpty()) {
				throw new NoSuchElementException();
			}

			Tag tag = optionalTag.get();

			if (!tag.getUser().getUsername().equals(username)) {
				throw new EmptyResultDataAccessException(1);
			}

			return tag;
		} catch (EmptyResultDataAccessException | NoSuchElementException e) {
			throw new DatabaseException("Não foi possível realizar a ação");
		}
	}

	public HttpStatus registerTag(String tagName, String username) {
		Optional<User> userOptional = userRepository.findByUsername(username);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			Tag tag = new Tag();
			tag.setTagName(tagName);
			tag.setUser(user);
			tagRepository.save(tag);
			return HttpStatus.ACCEPTED;
		}
		return HttpStatus.CONFLICT;
	}

	public HttpStatus deleteTag(UUID tagId) {
		Optional<Tag> tagOptional = tagRepository.findById(tagId);
		if (tagId.equals(tagOptional.get().getTagId())) {
			tagRepository.deleteById(tagId);
			return HttpStatus.ACCEPTED;
		}
		return HttpStatus.NOT_FOUND;
	}

	public HttpStatus editTag(UUID tagId, String newName) {
		Optional<Tag> tagOptional = tagRepository.findById(tagId);
		if (!newName.equals(tagOptional.get().getTagName())) {
			Tag tag = tagOptional.get();
			tag.setTagName(newName);
			tagRepository.save(tag);
			return HttpStatus.ACCEPTED;
		}
		return HttpStatus.CONFLICT;
	}

	public List<Tag> getAllTags(String username) {
		Optional<User> userOptional = userRepository.findByUsername(username);

		if (userOptional.isEmpty()) {
			throw new EmptyResultDataAccessException(1);
		}
		return tagRepository.findAllByUser(userOptional);
	}

	public Tag getTagById(UUID tagId, String username) {
		Tag tag = getTag(tagId, username);
		tagRepository.save(tag);
		return tag;
	}

}
