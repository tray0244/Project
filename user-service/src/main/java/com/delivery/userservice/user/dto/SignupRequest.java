package com.delivery.userservice.user.dto;


import com.delivery.userservice.user.domain.User;
import com.delivery.userservice.user.domain.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    private String email;
    private String password;
    private String name;
    private String role; // CUSTOMER, OWNER (String으로 받아서 Enum으로 변환)
    private String phoneNumber;


    // DTO -> Entiry 변환 메서드 (암호화된 비밀번호를 받아서 처리)
    public User toEntity(String encryptedPassword){
        UserRole userRole;
        try{
            userRole = UserRole.valueOf(this.role.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e){
            throw new IllegalArgumentException("유효하지 않은 역할입니다." + this.role);
        }
        return new User(
                this.email,
                encryptedPassword,
                this.name,
                userRole,
                this.phoneNumber
        );
    }
}
