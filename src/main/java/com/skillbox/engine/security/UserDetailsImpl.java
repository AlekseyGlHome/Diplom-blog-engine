package com.skillbox.engine.security;

import com.skillbox.engine.model.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Data
public class UserDetailsImpl implements UserDetails {
    private final String username;
    private final String password;
    private final Set<SimpleGrantedAuthority> authoritySet;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authoritySet;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

    public static UserDetails fromUser(User user){
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                true,true,true,true,
                user.getRole().getAuthorities());
    }
}
