package br.com.project.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "TB_USER")
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_ID", nullable = false , columnDefinition = "BINARY(16)")
    private UUID userId;

    @Column(name = "COMPLETE_NAME", nullable = false, length = 70)
    private String completeName;

    @Column(name = "USER_NAME", nullable = false, unique = true, updatable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false, length = 60)
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    public User() {
    }

    public User(UUID userId, String completeName, String username, String password) {
        this.userId = userId;
        this.completeName = completeName;
        this.username = username;
        this.password = password;
    }

    public UUID getId() {
        return userId;
    }

    public void setId(UUID id) {
        this.userId = id;
    }

    public String getCompleteNameName() {
        return completeName;
    }

    public void setCompleteNameName(String completeName) {
        this.completeName = completeName;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        return userId.equals(user.userId) && username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username);
    }

    // Método utilizado para testes
    // Deve ser removido na versão final
    @Override
    public String toString() {
        return "ID: " + userId + "\n" +
                "Name: " + completeName + "\n" +
                "Username: " + username + "\n" +
                "Password: " + password;
    }
}