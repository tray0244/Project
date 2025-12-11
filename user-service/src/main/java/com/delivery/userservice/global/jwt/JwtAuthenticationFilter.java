package com.delivery.userservice.global.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        // 요청 헤더에서 토큰 꺼내기
        String token = resolveToken(request);

        // 토큰이 있고, 유효하다면? 통과
        if(token != null && jwtTokenProvider.validateToken(token)){

            String isLogout = redisTemplate.opsForValue().get(token);

            if(ObjectUtils.isEmpty(isLogout)){ // 블랙리스트에 없어야만 통과
                // 토큰에서 사용자 정보 가져오기
                Authentication authentication = jwtTokenProvider.getAuthentication(token);

                // 로그인 하면 스프링 시큐리티에 등록
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("시큐리티 컨텍스트에 '{}' 인증 정보를 저장했습니다.", authentication.getName());
            }else{
                log.warn("로그아웃된 토큰입니다.");
            }
        }

        // 다음 필터로 넘기기
        filterChain.doFilter(request, response);
    }

    // 헤더에서 Bearer 토큰 꺼내는 메서드
    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
