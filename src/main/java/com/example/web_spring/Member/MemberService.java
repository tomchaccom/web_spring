package com.example.web_spring.Member;

import com.example.web_spring.Member.Dto.MemberJoinDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor // Lombok을 사용하여 생성자 주입
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화를 위해 사용


    public Long join(MemberJoinDto joinDto) {

        validateDuplicateMember(joinDto.getUsername());

        // 2. 엔티티 생성 (Setter 없이 DTO를 통해 데이터 주입)
        Member member = new Member();
        member.createMember(
                joinDto.getUsername(),
                passwordEncoder.encode(joinDto.getPassword()), // 비밀번호 암호화
                joinDto.getEmail(),
                joinDto.getPhone()
                // role 등 나머지 정보
        );
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(String username) {
        memberRepository.findByUsername(username)
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원 ID입니다.");
                });
    }
}