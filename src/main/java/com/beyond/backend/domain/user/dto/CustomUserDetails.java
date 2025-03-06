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

/*    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;
    private boolean banned;*/

    private User user;

    /**
     * User 엔티티를 기반으로 CustomUserDetails 생성
     * @param user 사용자 엔티티
     */
    public CustomUserDetails(User user) {
        /*this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        this.banned = user.getIsBanned();*/
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
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
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        //return !banned;
        return !user.getBanned();
    }
}