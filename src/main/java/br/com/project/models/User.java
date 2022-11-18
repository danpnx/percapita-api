package br.com.project.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;

import java.util.*;

import javax.persistence.*;


@Entity
@Table(name = "TB_USER")
public class User implements Serializable, UserDetails {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID", nullable = false)
	@Schema(hidden = true)
	private Long userId;

	@Column(name = "COMPLETE_NAME", nullable = false, length = 60)
	private String name;

	@Column(name = "USERNAME", nullable = false, unique = true, length = 100)
	private String username;

	@Column(name = "PASSWORD", nullable = false, length = 200)
	private String password;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	@JsonManagedReference(value = "user-tag-reference")
	@Schema(hidden = true)
	private List<Tag> tags = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	@JsonManagedReference(value = "user-transaction-reference")
	@Schema(hidden = true)
	private List<FinancialTransaction> transactions = new ArrayList<>();

	@Column(name = "TOKEN")
	@Schema(hidden = true)
	private String token;

	@Column(name = "CREATION_DATE_TOKEN")
	@Schema(hidden = true)
	private LocalDateTime tokenCreationDate;

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

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	@Schema(hidden = true)
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
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

	public List<FinancialTransaction> getTransactions() {
		return transactions;
	}

	public List<Tag> getTags() {
		return tags;
	}

	@Override
	@Schema(hidden = true)
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@Schema(hidden = true)
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@Schema(hidden = true)
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@Schema(hidden = true)
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return userId.equals(user.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId);
	}
}