package com.beyond.backend.domain.user.dto;

import com.beyond.backend.domain.user.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
public class CustomUserDetails implements UserDetails {

    private User user;

    /**
     * User 엔티티를 기반으로 CustomUserDetails 생성
     * @param user 사용자 엔티티
     */
    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
    }

    public User getUser() {
        return this.user;
    }

    @Override
    public String getPassword() {
        //return password;
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        //return username;
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getPasswordErrorCount() <= 4;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !user.getBanned();
    }
}