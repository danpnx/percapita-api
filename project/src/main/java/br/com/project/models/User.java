package br.com.project.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tb_user")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID", nullable = false)
	private Long userId;
	@Column(name = "COMPLETE_NAME", nullable = false, length = 60)
	private String name;
	@Column(name = "USERNAME", nullable = false, unique = true, length = 100)
	private String username;
	@Column(name = "PASSWORD", nullable = false, length = 200)
	private String password;
	@Column(name = "TOKEN")
	private String token;
	@Column(name = "CREATION_DATE_TOKEN")
	private LocalDateTime tokenCreationDate;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tagId")
	private List<Tag> tag;

	public User() {
	}

	public User(Long userId, String name, String username, String password) {
		this.userId = userId;
		this.name = name;
		this.username = username;
		this.password = password;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getTokenCreationDate() {
		return tokenCreationDate;
	}

	public void setTokenCreationDate(LocalDateTime tokenCreationDate) {
		this.tokenCreationDate = tokenCreationDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
