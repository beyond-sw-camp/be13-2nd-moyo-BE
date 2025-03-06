package com.beyond.backend.domain.user.handler;

import com.beyond.backend.domain.common.exception.dto.ApiErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * 인증되지 않은 사용자가 보호된 리소스에 접근하려고 할 때 실행되는 핸들러
 * - Spring Security에서 기본적으로 제공하는 `401 Unauthorized` 응답을 JSON 형식으로 변환하여 반환
 * - API 클라이언트에서 일관된 에러 응답을 받을 수 있도록 처리
 */
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    /**
     * 인증되지 않은 사용자가 보호된 리소스에 접근할 때 실행되는 메서드
     * - HTTP 상태 코드 `401 Unauthorized` 설정
     * - JSON 형식의 응답을 반환하여 API 클라이언트에서 처리 가능하도록 함
     *
     * @param request       요청 객체
     * @param response      응답 객체
     * @param authException 인증 예외 객체 (예외 메시지를 포함)
     * @throws IOException, ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // HTTP 응답 상태 코드 설정 (401 Unauthorized)
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        // 응답을 JSON 형식으로 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // Jackson ObjectMapper를 사용하여 ApiErrorResponseDto를 JSON으로 변환
        ObjectMapper mapper = new ObjectMapper();
        ApiErrorResponseDto errorResponseDto = new ApiErrorResponseDto(
                HttpStatus.UNAUTHORIZED.value(),      // HTTP 상태 코드 (401)
                HttpStatus.UNAUTHORIZED.name(),       // 상태 이름 ("UNAUTHORIZED")
                "인증이 필요합니다. 올바른 인증 정보를 제공해주세요." // 사용자 친화적인 메시지
        );

        // JSON 응답 반환
        response.getWriter().write(mapper.writeValueAsString(errorResponseDto));
    }
}