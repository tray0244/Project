package com.delivery.userservice.auth.service;

import com.delivery.userservice.auth.dto.LoginRequest;
import com.delivery.userservice.auth.dto.TokenResponse;
import com.delivery.userservice.global.jwt.JwtTokenProvider;
import com.delivery.userservice.user.domain.User;
import com.delivery.userservice.user.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public TokenResponse login(LoginRequest request){

        // 이메일로 사용자 찾기
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 틀렸습니다."));

        // 비밀번호 검증(DB의 암호화된 비번 vs 입력받은 비번)
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
        }
//        String accessToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole().name());

        String accessToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole().name());

        return new TokenResponse(accessToken, "Bearer", 3600L);
    }

    @Transactional
    public void logout(String accessToken){
        // 토큰 유효성 검사
        if(!jwtTokenProvider.validateToken(accessToken)){
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        // 토큰의 남은 유효 시간 계산, 이 시간동안 Redis에 블랙리스트로 게산
        // Provider에 getExpiration() 메서드가 필요함 (남은 만료 시간 계산)
        long expiration = jwtTokenProvider.getExpiration(accessToken);
        long now = System.currentTimeMillis();

        // Redis에 블랙리스트 등록
        redisTemplate.opsForValue().set(accessToken, "logout", expiration - now, TimeUnit.MILLISECONDS);
    }
}
