package com.delivery.userservice.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    //application.yml에서 가져올 비밀키
    @Value("${jwt.secret:c2lsdmVyLW1hZXN0cm8tcHJvamVjdC1zZWNyZXQta2V5LWZvci10ZXN0LW11c3QtYmUtbG9uZ2VyLXRoYW4tMjU2LWJpdHM=}")
    private String secretKey;

    @Value("${jwt.expiration:3600000}")
    private long tokenValidityInMilliseconds;

    private Key key;

    @PostConstruct
    public void init(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성 (로그인 시 호출)
    public String createToken(String email, String role){
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role) // 사용자 권한 담기
                .setIssuedAt(new Date())
                .setExpiration(validity) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 암호화 알고리즘
                .compact();
    }

    // API 호출할 때마다 토큰 유효성 검사
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (SecurityException | MalformedJwtException e){
            log.info("잘못된 JWT 서명입니다.");
        }catch (ExpiredJwtException e){
            log.info("만료된 JWT 토큰입니다.");
        }catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        }catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 꺼내기
    public String getEmail(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 인증 정보 조회
    public Authentication getAuthentication(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Role 정보 가져오기
        String role = claims.get("role", String.class);

        // 권한 목록 생성
        Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + role));

        // UserDetails 객체를 만들어서 Authentication 리턴
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
    }

    // 토큰 만료 시간 계산
    public long getExpiration(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .getTime();
    }
}