package br.com.project.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "TB_USER")
public class User implements Serializable, UserDetails {
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

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	@JsonManagedReference(value = "user-tag-reference")
	private List<Tag> tags = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	@JsonManagedReference(value = "user-transaction-reference")
	private List<FinancialTransaction> transactions = new ArrayList<>();

	@Column(name = "TOKEN")
	private String token;

	@Column(name = "CREATION_DATE_TOKEN")
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
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
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