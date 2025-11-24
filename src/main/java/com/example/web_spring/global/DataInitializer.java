package com.example.web_spring.global;


import com.example.web_spring.Member.Member;
import com.example.web_spring.Member.MemberRepository;
import com.example.web_spring.Member.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // 1. 관리자 계정 생성 (ROLE_ADMIN)
        if (memberRepository.findByUsername("admin").isEmpty()) {
            Member admin = Member.createMember(
                    "admin",
                    passwordEncoder.encode("1234"), // 비밀번호: 1234 (반드시 암호화)
                    "admin@shop.com",
                    "010-0000-0000",
                    Role.ROLE_ADMIN// 관리자 권한 할당
            );
            memberRepository.save(admin);
            System.out.println("✅ 관리자 계정 (admin/1234) 생성 완료");
        }

        // 2. 일반 사용자 계정 생성 (ROLE_USER)
        if (memberRepository.findByUsername("user").isEmpty()) {
            Member user = Member.createMember(
                    "user",
                    passwordEncoder.encode("1234"), // 비밀번호: 1234 (반드시 암호화)
                    "user@shop.com",
                    "010-1111-1111",
                    Role.ROLE_USER // 일반 사용자 권한 할당
            );
            memberRepository.save(user);
            System.out.println("✅ 일반 사용자 계정 (user/1234) 생성 완료");
        }
    }
}