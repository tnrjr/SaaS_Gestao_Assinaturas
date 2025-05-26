package com.tary.saas.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long companyId;

    private String name;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // ADMIN, USER

    public enum Role {
        ADMIN, USER
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retorna as autoridades baseadas no role do usuário
        return Collections.singletonList(() -> "ROLE_" + role.name());
    }

    @Override
    public String getUsername() {
        return email;
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
}