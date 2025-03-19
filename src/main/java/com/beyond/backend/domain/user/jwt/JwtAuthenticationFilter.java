package com.beyond.backend.domain.user.jwt;

import com.beyond.backend.domain.common.exception.JwtAuthenticationException;
import com.beyond.backend.domain.user.dto.CustomUserDetails;
import com.beyond.backend.domain.user.handler.AuthenticationEntryPointImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증 필터
 * 모든 요청에서 실행되며, JWT 토큰을 검사하여 유효한 경우 SecurityContext 에 인증 정보를 저장한다.
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = jwtTokenProvider.resolveToken(request.getHeader("Authorization")).orElse(null);
            if (token != null) {
                jwtTokenProvider.validateToken(token);
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                checkPermission(token, authentication);
            }
        } catch (JwtAuthenticationException ex) {
            SecurityContextHolder.clearContext();
            new AuthenticationEntryPointImpl().commence(request, response, ex);
            return;
         }
        filterChain.doFilter(request, response);
    }

    private void checkPermission(String token, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        if (!userDetails.isEnabled()) {
            jwtTokenProvider.addBlacklist(token);
            jwtTokenProvider.deleteRefreshToken(token);
            log.info("{}님은 밴된 사용자", username);
        }
        if (!userDetails.isAccountNonLocked()) {
            log.info("{}님은 임시 정지 조치된 사용자", username);
        }
    }



    /**
     * 액세스 토큰이 유효한지 확인하는 메서드
     * - 토큰이 존재하고
     * - 유효하며 (만료되지 않음)
     * - 블랙리스트에 등록되지 않았으며 (로그아웃된 토큰이 아님)
     * - 역할(Role) 정보가 포함되어 있는지 확인
     *
     * @param token 확인할 JWT 토큰
     * @return 유효한 액세스 토큰이면 true, 그렇지 않으면 false
     */
    private boolean isUsableAccessToken(String token) {
        log.info("JWT Authentication Filter - isUsableAccessToken");
        boolean isValid = jwtTokenProvider.validateToken(token)
                && !jwtTokenProvider.isBlacklisted(token)
                && jwtTokenProvider.hasRole(token);

        if (!isValid) {
            log.warn("⚠️ 사용 불가능한 토큰 감지: {}", token);
        }
        return isValid;
    }
}