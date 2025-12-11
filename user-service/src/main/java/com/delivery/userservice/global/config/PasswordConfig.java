package com.delivery.userservice.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){

        // BCrypt 해싱 함수(단방향 암호화)를 사용하여 비밀번호를 보호
        // BCryptPasswordEncoder
        // - 토큰을 발급해주시 전에 이 사람이 진짜 주인인지 비밀번호를 맞춰보는 용도
        return new BCryptPasswordEncoder();
    }
}
