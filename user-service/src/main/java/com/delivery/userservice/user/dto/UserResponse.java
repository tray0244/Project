package com.delivery.userservice.user.dto;

import com.delivery.userservice.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private Long userId;
    private String email;
    private String name;
    private String role;
    private String phoneNumber;

    // Entity -> DTO 변환 메서드
    public static UserResponse from(User user){
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole().name(),
                user.getPhoneNumber()
        );
    }
}
