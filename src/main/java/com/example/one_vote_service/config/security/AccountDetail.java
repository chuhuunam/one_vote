package com.example.one_vote_service.config.security;


import com.example.one_vote_service.config.constant.UserType;
import com.example.one_vote_service.domain.entity.auth.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Collection;

@Data
public class AccountDetail implements UserDetails {
    private static final long serialVersionUID = 1L;

    private final Long id;

    private final String phone;

    private final String fullName;

    private final String email;

    private String jwt;

    @Enumerated(EnumType.STRING)
    private UserType type;

    public AccountDetail(User user, String jwt) {
        this.id = user.getId();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.type = user.getType();
        this.fullName = user.getFullName();
        this.jwt = jwt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
