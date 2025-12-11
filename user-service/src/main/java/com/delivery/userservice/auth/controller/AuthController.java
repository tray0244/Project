package com.delivery.userservice.auth.controller;

import com.delivery.userservice.auth.dto.LoginRequest;
import com.delivery.userservice.auth.dto.TokenResponse;
import com.delivery.userservice.auth.service.AuthService;
import com.delivery.userservice.user.dto.SignupRequest;
import com.delivery.userservice.user.dto.UserResponse;
import com.delivery.userservice.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    // 회원가입 (UserController에서 이동)
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody SignupRequest request){
        return ResponseEntity.ok(userService.signup(request));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token){
       // Bearer 제거 후 서비스로 넘기기
       String accessToken = token.substring(7);
       authService.logout(accessToken);
       return ResponseEntity.ok("로그아웃 되었습니다.");
    }
}
