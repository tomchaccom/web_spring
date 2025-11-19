package com.example.web_spring.global;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 비밀번호 해싱에 가장 널리 사용되는 BCrypt 구현체를 빈으로 등록
        return new BCryptPasswordEncoder();
    }
}
