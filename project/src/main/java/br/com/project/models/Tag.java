package br.com.project.models;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "TB_TAG")
public class Tag implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Type(type = "org.hibernate.type.UUIDCharType")
	@Column(name = "TAG_ID", columnDefinition = "CHAR(36)")
	private UUID tagId;

	@Column(name = "TAG_NAME", nullable = false, unique = true, length = 25)
	private String tagName;

	@OneToOne
	private User user;

	public Tag() {
	}

	public Tag(UUID tagId, String tagName) {
		this.tagId = tagId;
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