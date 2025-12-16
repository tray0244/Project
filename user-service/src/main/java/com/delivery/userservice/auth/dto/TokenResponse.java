package com.delivery.userservice.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {
    private String accessToken;
    private String tokenType; // 보통 "Bearer"
    private Long expiresIn;   // 만료 시간
}