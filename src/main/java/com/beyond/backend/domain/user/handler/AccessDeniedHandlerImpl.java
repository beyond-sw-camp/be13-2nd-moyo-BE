package com.beyond.backend.domain.user.handler;

import com.beyond.backend.domain.common.exception.dto.ApiErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

// AccessDeniedHandler 인터페이스를 구현하는 AccessDeniedHandlerImpl 클래스를 선언
// 이 클래스는 Spring Security 에서 접근이 거부되었을 때 실행되는 핸들러 역할을 한다.
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    // AccessDeniedHandler 의 handle 메서드를 오버라이드하여 접근 거부 시 실행될 로직을 정의
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // HTTP 응답 상태 코드를 403 (Forbidden)으로 설정
        response.setStatus(HttpStatus.FORBIDDEN.value());

        // 응답의 Content-Type 을 JSON 형식으로 설정 (클라이언트가 JSON 응답을 받을 수 있도록 함)
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 응답의 문자 인코딩을 UTF-8로 설정 (한글 및 특수 문자 깨짐 방지)
        response.setCharacterEncoding("UTF-8");

        // Jackson 의 ObjectMapper 를 사용하여 자바 객체를 JSON 문자열로 변환하기 위해 ObjectMapper 객체 생성
        ObjectMapper mapper = new ObjectMapper();

        // API 오류 응답 객체(ApiErrorResponseDto) 생성
        ApiErrorResponseDto errorResponseDto = new ApiErrorResponseDto(
                HttpStatus.FORBIDDEN.value(),       // 403
                HttpStatus.FORBIDDEN.name(),        // "FORBIDDEN"
                HttpStatus.FORBIDDEN.getReasonPhrase() // "Forbidden"
        );

        // 생성한 오류 응답 객체를 JSON 문자열로 변환하여 HTTP 응답 본문에 작성
        response.getWriter().write(
                mapper.writeValueAsString(errorResponseDto)
        );
    }
}

