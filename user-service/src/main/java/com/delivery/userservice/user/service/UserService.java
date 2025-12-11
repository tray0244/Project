package com.delivery.userservice.user.service;

import com.delivery.userservice.user.domain.User;
import com.delivery.userservice.user.dto.SignupRequest;
import com.delivery.userservice.user.dto.UserResponse;
import com.delivery.userservice.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 비즈니스 로직
     * 1. 이메일 중복 확인
     * 2. 비밀번호 암호화
     * 3. DB 저장
     */
    @Transactional
    public UserResponse signup(SignupRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());;
        User user = request.toEntity(encodedPassword);

        return UserResponse.from(userRepository.save(user));
    }

    // 토큰 기반 내 정보 조회
    public UserResponse getMyInfo(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return UserResponse.from(user);
    }
}
