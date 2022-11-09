package br.com.project.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import br.com.project.controllers.TagController;
import br.com.project.exceptions.DataNotAvailableException;
import br.com.project.exceptions.DatabaseException;
import br.com.project.exceptions.InvalidInputException;
import br.com.project.exceptions.ResourceNotFoundException;
import br.com.project.models.FinancialRecord;
import br.com.project.repositories.FinancialTransactionRepository;
import br.com.project.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.project.models.Tag;
import br.com.project.models.User;
import br.com.project.repositories.TagRepository;
import br.com.project.repositories.UserRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class TagService {

	@Autowired
	private TagRepository tagRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FinancialTransactionRepository transactionRepository;

	public TagService(TagRepository tagRepository, UserRepository userRepository) {
		this.tagRepository = tagRepository;
		this.userRepository = userRepository;
	}

	public boolean existsByTagNameAndUser(String tagName, String username) {
		return tagRepository.existsByTagNameContainingIgnoreCaseAndUser(tagName, userRepository.findByUsername(username).get());
	}

	public Tag getTagByTagNameAndUser(String tagName, User user) {
		return tagRepository.findByTagNameAndUser(tagName, user);
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

	public Tag registerTagInFinancialTransaction(String tagName, User user) {
		Tag tag = new Tag();
		tag.setTagName(tagName);
		tag.setUser(user);
		tagRepository.save(tag);
		return tag;
	}

	public HttpStatus deleteTag(UUID tagId, String username) {
		if(!tagRepository.existsById(tagId)) {
			throw new ResourceNotFoundException("Essa tag não existe");
		}

		Optional<Tag> tagOptional = tagRepository.findById(tagId);

		if(tagOptional.get().getTagName().equals("Unknown")) {
			throw new DatabaseException("Impossível deletar esta tag");
		}

		User u = userRepository.findByUsername(username).get();
		Tag tag = tagRepository.findByTagNameAndUser(tagOptional.get().getTagName(), u);

		if(!tagRepository.existsByTagNameContainingIgnoreCaseAndUser("Unknown", u)) {
			Tag tagTemp = new Tag();
			tagTemp.setTagName("Unknown");
			tagTemp.setUser(u);

			tagRepository.save(tagTemp);
		}

		for(FinancialRecord t: tag.getTransactions()) {
			t.setTag(tagRepository.findByTagNameAndUser("Unknown", u));
			transactionRepository.save(t);
		}
		tagRepository.deleteById(tagId);

		return HttpStatus.ACCEPTED;
	}

	public HttpStatus editTag(UUID tagId, String newName, String username) {
		Optional<Tag> tagOptional = tagRepository.findById(tagId);

		if(tagOptional.isEmpty()) {
			throw new ResourceNotFoundException("Essa tag não existe");
		}

		if(existsByTagNameAndUser(newName, username)) {
			throw new DataNotAvailableException("Você já possui uma tag com esse nome");
		}

		Tag tag = tagOptional.get();
		tag.setTagName(newName);
		tagRepository.save(tag);
		return HttpStatus.ACCEPTED;
	}

	public List<Tag> getAllTags(String username) {
		Optional<User> userOptional = userRepository.findByUsername(username);

		if (userOptional.isEmpty()) {
			throw new DatabaseException("Este usuário não existe");
		}

		List<Tag> list = tagRepository.findAllByUser(userOptional);

		if(list.isEmpty()) {
			throw new ResourceNotFoundException("Você não possui nenhuma tag cadastrada");
		}

		for(Tag t: list) {
			if(t.getTagName().equals("Unknown")) {
				t.add(linkTo(methodOn(TagController.class).getTagById(t.getTagId().toString())).withSelfRel());
				t.add(linkTo(methodOn(TagController.class).createTag(null)).withSelfRel());
				continue;
			}
			t.add(linkTo(methodOn(TagController.class).getTagById(t.getTagId().toString())).withSelfRel());
			t.add(linkTo(methodOn(TagController.class).editTag(t.getTagId().toString(), null)).withSelfRel());
			t.add(linkTo(methodOn(TagController.class).deleteTag(t.getTagId().toString())).withSelfRel());
			t.add(linkTo(methodOn(TagController.class).createTag(null)).withSelfRel());
		}

		return list;
	}

	public Tag getTagById(UUID tagId, String username) {
		Tag tag = getTag(tagId, username);

		if(tag.getTagName().equals("Unknown")) {
			tag.add(linkTo(methodOn(TagController.class).getAll()).withRel("Tags do usuário"));
			return tag;
		}

		tag.add(linkTo(methodOn(TagController.class).getAll()).withRel("Tags do usuário"));
		tag.add(linkTo(methodOn(TagController.class).deleteTag(tag.getTagId().toString())).withSelfRel());
		tag.add(linkTo(methodOn(TagController.class).editTag(tag.getTagId().toString(), null)).withSelfRel());

		tagRepository.save(tag);
		return tag;
	}

}