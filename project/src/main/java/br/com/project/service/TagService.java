package br.com.project.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

	public boolean existsByTagNameAndUser(String tagName, Long userId) {
		return tagRepository.existsByTagNameAndUser(tagName, userRepository.findById(userId));
	}

	public HttpStatus registerTag(String tagName, Long userId) {
		Optional<User> userOptional = userRepository.findById(userId);
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
		Optional<Tag> userOptional = tagRepository.findById(tagId);
		if (tagId.equals(userOptional.get().getTagId())) {
			tagRepository.deleteById(tagId);
			return HttpStatus.ACCEPTED;
		}
		return HttpStatus.NOT_FOUND;
	}

	public HttpStatus editTag(UUID tagId, String newName) {
		Optional<Tag> userOptional = tagRepository.findById(tagId);
		if (!newName.equals(userOptional.get().getTagName())) {
			Tag tag = userOptional.get();
			tag.setTagName(newName);
			tagRepository.save(tag);
			return HttpStatus.ACCEPTED;
		}
		return HttpStatus.CONFLICT;
	}

	public List<Tag> findAllTags (Long id) {
		Optional<User> userOptional = userRepository.findById(id);

		if (userOptional.isEmpty()) {
			throw new EmptyResultDataAccessException(1);
		}
		return tagRepository.findAll();
	}
}

