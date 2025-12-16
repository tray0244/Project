package com.delivery.userservice.global.config;

import com.delivery.userservice.global.jwt.JwtAuthenticationFilter;
import com.delivery.userservice.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // final 필드 생성자 자동 주입
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                // CSRF 비활성화, Rest API 서버라서 불필요
                .csrf(AbstractHttpConfigurer::disable)

                // H2 Console 허용
                .headers(headers -> headers.frameOptions(options -> options.disable()))

                // JWT는 Stateless라서 세션을 사용 안한다.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // URL별 권한 관리
                .authorizeHttpRequests(auth -> auth
                        // Swagger URL 허용
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // 회원가입, 로그인 URL 허용
                        .requestMatchers("/auth/**", "/users/signup", "/h2-console/**").permitAll()
                        // 그 외의 모든 요청은 인증이 필요
                        .anyRequest().authenticated()
                )

                // JWT 필터 등록
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisTemplate),
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}