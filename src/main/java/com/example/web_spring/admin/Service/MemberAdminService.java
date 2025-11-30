package com.example.web_spring.admin.Service;

import com.example.web_spring.Member.Member;
import com.example.web_spring.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberAdminService {

    private final MemberRepository memberRepository;

    // 전체 회원 조회
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // 회원 상세 정보 조회
    public Member getMemberDetail(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보 없음"));
    }
}

