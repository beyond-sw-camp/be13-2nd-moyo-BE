package com.beyond.backend.config;

import com.beyond.backend.domain.user.entity.UserRoleType;
import com.beyond.backend.domain.user.handler.AuthenticationEntryPointImpl;
import com.beyond.backend.domain.user.jwt.JwtAuthenticationFilter;
import com.beyond.backend.domain.user.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;


    //시큐리티 role 수직적 게층 시큐리티에 적용
    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withRolePrefix("Role_")
                .role(UserRoleType.ADMIN.toString()).implies(UserRoleType.USER.toString())
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenProvider jwtTokenProvider) throws Exception{


        http
                .cors(cors -> cors.configurationSource(getCorsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(new AuthenticationEntryPointImpl())
                                .accessDeniedHandler(new AccessDeniedHandlerImpl()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**",
                                "/join", "/login",
                                "/logout", "/ban",
                                "/refresh",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/api-docs/**",
                                "/swagger-ui.html",
                                "/delete",
                                "/updatePassword",
                                "/swagger-resources/**",
                                // 아래 경로들을 추가
                                "/posts",                    // 게시글 전체 조회
                                "/posts/{postNo}/**",        // 게시글 상세 조회
                                "/posts/search",             // 게시글 검색
                                "/project",                  // 프로젝트 전체 조회
                                "/project/{projectNo}",      // 프로젝트 상세 조회
                                "/project/search"            // 프로젝트 검색

                        ).permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private static CorsConfigurationSource getCorsConfigurationSource() {
        return request -> {  // 🟢 요청(request)을 받아서 CORS 설정을 동적으로 적용하는 람다 함수
            CorsConfiguration configuration = new CorsConfiguration();

            configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:3011","http:183.99.3.15:3011")); // Vue 개발 서버 허용
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드
            configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // 요청에서 허용할 헤더
            configuration.setExposedHeaders(List.of("Authorization"));  // 응답에서 노출할 헤더
            configuration.setAllowCredentials(true);  // 쿠키 인증 허용
            configuration.setMaxAge(3600L); // CORS 설정을 1시간 동안 캐시

            return configuration;
        };
    }
}
