package com.delivery.userservice.user.controller;

import com.delivery.userservice.user.dto.SignupRequest;
import com.delivery.userservice.user.dto.UserResponse;
import com.delivery.userservice.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

//    // 내 정보 조회 API
//    @GetMapping("/{userId}")
//    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
//        return ResponseEntity.ok(userService.getUser(userId));
//    }

    // 내 정보 조회 (마이페이지)
    @GetMapping("/my")
    public ResponseEntity<UserResponse> getMyInfo(){
        // 시큐리티 컨텍스트에서 인증 정보 꺼내기 (이메일)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // JwtTokenProvider에서 넣은 이메일 정보
        String email = auth.getName();

        // 서비스 호출
        UserResponse response = userService.getMyInfo(email);

        return  ResponseEntity.ok(response);
    }
}
