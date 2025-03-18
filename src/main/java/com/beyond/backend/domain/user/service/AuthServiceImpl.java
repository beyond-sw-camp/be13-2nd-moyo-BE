package com.beyond.backend.domain.user.service;

import com.beyond.backend.domain.common.exception.UserException;
import com.beyond.backend.domain.common.exception.message.ExceptionMessage;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.dto.JoinRequestDto;
import com.beyond.backend.domain.user.dto.LoginRequestDto;
import com.beyond.backend.domain.user.dto.TokenResponseDto;
import com.beyond.backend.domain.user.entity.User;
import com.beyond.backend.domain.user.jwt.JwtTokenProvider;
import com.beyond.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthTransactionService authTransactionService; // 별도 서비스 주입

    @Override
    @Transactional
    public void join(JoinRequestDto dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();
        String confirmPassword = dto.getConfirmPassword();

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username is already taken");
        }

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("New passwords don't match");
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = User.builder().username(username).password(encodedPassword).email(dto.getEmail())
                .phoneNum(dto.getPhoneNum()).build();

        userRepository.save(user);
    }

    @Override
    @Transactional
    public TokenResponseDto login(LoginRequestDto dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        validPwd(password, user);

        // 비밀번호가 일치한 경우 나머지 로그인 로직 실행
        TokenResponseDto tokenResponseDto = new TokenResponseDto(
                jwtTokenProvider.createAccessToken(user.getUsername(), user.getRole().toString()),
                jwtTokenProvider.createRefreshToken(user.getUsername()));

        // 추가 유효성 검사...
        CustomUserDetails customUserDetails = (CustomUserDetails) jwtTokenProvider
                .getAuthentication(tokenResponseDto.getAccessToken()).getPrincipal();

        if (!customUserDetails.isEnabled()) {
            throw new RuntimeException("밴 사용자");
        }
        if (!customUserDetails.isAccountNonLocked()) {
            throw new RuntimeException("임시 정지 사용자");
        }

        return tokenResponseDto;
    }

    @Override
    @Transactional
    public void logout(String bearerToken) {
        String accessToken = jwtTokenProvider.resolveToken(bearerToken)
                .orElseThrow(() -> new IllegalArgumentException("Token is null"));

        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new IllegalArgumentException("Invalid token");
        }

        jwtTokenProvider.addBlacklist(accessToken);
        jwtTokenProvider.deleteRefreshToken(accessToken);
    }

    @Override
    @Transactional
    public TokenResponseDto refresh(String bearerToken) {
        String refreshToken = jwtTokenProvider.resolveToken(bearerToken)
                .orElseThrow(() -> new IllegalArgumentException("Token is null"));

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid token");
        }

        User user = userRepository.findByUsername(jwtTokenProvider.getUserName(refreshToken)).get();
        return new TokenResponseDto(jwtTokenProvider.createAccessToken(user.getUsername(), user.getRole().toString()),
                refreshToken);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return (CustomUserDetails) principal;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public void validateUser(User user) {

        CustomUserDetails currentUser = getCurrentUser();

        //트랜잭션이 닫히거나, 프록시가 적절히 초기화되지 않으면 getUsername()을 호출해도 프록시에서 원하는 값을 가져오지 못할 수 있음
        User savedUser = userRepository.findById(user.getNo())
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        if (!currentUser.getUser().getNo().equals(savedUser.getNo())) {
            throw new IllegalArgumentException(
                    "User is not authorized to perform this action. (username: " + currentUser.getUsername() + ")"
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUser(User user) {
        CustomUserDetails currentUser = getCurrentUser();

        User savedUser = userRepository.findById(user.getNo())
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        return currentUser.getUser().equals(savedUser);
    }


    @Override
    @Transactional(readOnly = true)
    public void validateAdminAuthorization() {
        CustomUserDetails currentUser = getCurrentUser();
        boolean result = currentUser.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if (!result) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isAdmin() {
        CustomUserDetails currentUser = getCurrentUser();
        return currentUser.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));
    }

    // 비밀번호 검증 로직 (login 메서드 내에서 호출)
    public void validPwd(String password, User user) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            // passwordErrorCount 를 별도의 트랜잭션에서 업데이트
            authTransactionService.increasePasswordErrorCount(user);
            log.info("Username : {}, PasswordErrorCount : {}", user.getUsername(), user.getPasswordErrorCount());
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다");
        }
    }
}
