package br.com.project.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Type;

import br.com.project.enums.TagCategory;
import org.springframework.hateoas.RepresentationModel;

@Entity
@Table(name = "TB_TAG")
public class Tag extends RepresentationModel<Tag> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Type(type = "org.hibernate.type.UUIDCharType")
	@Column(name = "TAG_ID", columnDefinition = "CHAR(36)")
	private UUID tagId;

	@Column(name = "TAG_NAME", nullable = false, length = 25)
	private String tagName;
	
	@Enumerated(value = EnumType.STRING)
    @Column(name = "TRANSACTION_CATEGORY", nullable = false)
    private TagCategory tagCategory;

	@ManyToOne
	@JoinColumn(name = "USER_ID")
	@JsonBackReference(value = "user-tag-reference")
	private User user;

	@OneToMany(mappedBy = "tag")
	@JsonManagedReference(value = "transaction-tag-reference")
	private List<FinancialTransaction> transactions = new ArrayList<>();

	public Tag() {
		this.tagId = UUID.randomUUID();
	}

	public Tag(String tagName) {
		this.tagId = UUID.randomUUID();
		this.tagName = tagName;
	}

	public UUID getTagId() {
		return tagId;
	}

	public void setTagId(UUID tagId) {
		this.tagId = tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
	public TagCategory getTagCategory() {
		return tagCategory;
	}

	public void setTagCategory(TagCategory tagCategory) {
		this.tagCategory = tagCategory;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<FinancialTransaction> getTransactions() {
		return transactions;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Tag tag = (Tag) o;
		return tagId.equals(tag.tagId) && tagName.equals(tag.tagName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tagId, tagName);
	}
}