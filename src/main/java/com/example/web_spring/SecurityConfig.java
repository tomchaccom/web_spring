package com.example.web_spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 활성화
public class SecurityConfig {

    // 1. PasswordEncoder 빈 등록 (이전에 누락 문제 해결을 위해 추가)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // 💡 CSRF 보호 설정 (개발 단계에서는 비활성화하는 경우가 많습니다.)
                // 세션 기반 인증을 사용하지 않는 경우에만 씁니다.
                .csrf(csrf -> csrf.disable())

                // 💡 요청에 대한 권한 설정 순서가 매우 중요합니다.
                .authorizeHttpRequests(authorize -> authorize

                        // 1. 정적 리소스 접근 허용 (가장 먼저)
                        // '/css', '/js', '/images' 폴더 내부의 모든 파일 접근 허용
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/error").permitAll()

                        // 2. 인증이 필요 없는 공용 페이지 접근 허용
                        // 메인 페이지, 회원가입, 로그인, 상품 목록/상세 등
                        .requestMatchers("/", "/join/**", "/login", "/find/**", "/list/**", "/product/**").permitAll()

                        // 3. (옵션) 개발 시 H2 Console 사용을 위한 설정 (사용 시 추가)
                        // .requestMatchers("/h2-console/**").permitAll()
                        // .frameOptions(frameOptions -> frameOptions.sameOrigin()) // H2 콘솔 사용을 위해 frameOptions 설정 필요

                        // 4. 관리자 페이지는 ADMIN 권한이 있는 사용자만 접근 허용
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 5. 그 외 모든 요청은 인증(로그인)이 필요함
                        .anyRequest().authenticated()
                )

                // 💡 폼 로그인 설정
                .formLogin(formLogin -> formLogin
                        .loginPage("/login") // 로그인 페이지 URL
                        .defaultSuccessUrl("/", true) // 로그인 성공 시 이동할 URL
                        .failureUrl("/login?error") // 로그인 실패 시 이동할 URL
                        .permitAll()
                )

                // 💡 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }
}