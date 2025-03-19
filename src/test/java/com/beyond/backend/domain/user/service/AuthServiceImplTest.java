/*
package com.beyond.backend.domain.user.service;

import com.beyond.backend.domain.common.entity.UserStatus;
import com.beyond.backend.domain.common.exception.message.ExceptionMessage;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.entity.UserRoleType;
import com.beyond.backend.domain.user.repository.UserRepository;
import com.mysema.commons.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest
class AuthServiceImplTest {

    @InjectMocks
    private UserService userService;

    private AuthService authService;
    private UserRepository userRepository;
    private CustomUserDetails customUserDetails;
    private User savedUser;



    @BeforeEach
    void setUp() {
        savedUser = User.builder()
                .username("testUser")
                .password("securePassword123")
                .email("testuser@example.com")
                .phoneNum("010-1234-5678")
                .build();


        currentUser = new CustomUserDetails(savedUser);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication())
                .thenReturn(new UsernamePasswordAuthenticationToken(currentUser, null, Set.of(new SimpleGrantedAuthority("ROLE_USER"))));
        SecurityContextHolder.setContext(securityContext);
    }




    @Test
    void testGetCurrentUser() {
        CustomUserDetails user = authService.getCurrentUser();
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("testUser");
    }

    @Test
    void testValidateUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        assertThatCode(() -> userService.validateUser(savedUser)).doesNotThrowAnyException();
    }

    @Test
    void testValidateUser_Failure_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.validateUser(savedUser))
                .isInstanceOf(UserException.class)
                .hasMessageContaining(ExceptionMessage.USER_NOT_FOUND.name());
    }

    @Test
    void testValidateUser_Failure_UserMismatch() {
        User otherUser = new User();
        otherUser.setNo(2L);
        otherUser.setUsername("otherUser");

        when(userRepository.findById(2L)).thenReturn(Optional.of(otherUser));
        assertThatThrownBy(() -> userService.validateUser(otherUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User is not authorized to perform this action.");
    }

    @Test
    void testIsUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        boolean result = userService.isUser(savedUser);
        assertThat(result).isTrue();
    }

    @Test
    void testIsUser_Failure() {
        User otherUser = new User();
        otherUser.setNo(2L);
        otherUser.setUsername("otherUser");

        when(userRepository.findById(2L)).thenReturn(Optional.of(otherUser));
        boolean result = userService.isUser(otherUser);
        assertThat(result).isFalse();
    }

    @Test
    void testValidateAdminAuthorization_Success() {
        CustomUserDetails adminUser = new CustomUserDetails(savedUser);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(adminUser, null, Set.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));

        assertThatCode(() -> userService.validateAdminAuthorization()).doesNotThrowAnyException();
    }

    @Test
    void testValidateAdminAuthorization_Failure() {
        assertThatThrownBy(() -> userService.validateAdminAuthorization())
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class)
                .hasMessageContaining("권한이 없습니다.");
    }

    @Test
    void testIsAdmin_Success() {
        CustomUserDetails adminUser = new CustomUserDetails(savedUser);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(adminUser, null, Set.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));

        boolean result = userService.isAdmin();
        assertThat(result).isTrue();
    }

    @Test
    void testIsAdmin_Failure() {
        boolean result = userService.isAdmin();
        assertThat(result).isFalse();
    }



}*/
