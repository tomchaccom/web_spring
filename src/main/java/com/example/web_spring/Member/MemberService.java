package com.example.web_spring.Member;

import com.example.web_spring.Member.Dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.web_spring.global.NotSignUpUserException;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor // Lombok을 사용하여 생성자 주입
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화를 위해 사용

    @Transactional
    public Long join(MemberJoinDto joinDto) {

        validateDuplicateMember(joinDto.getUsername());

        // 2. 엔티티 생성 (Setter 없이 DTO를 통해 데이터 주입)
        // createMember 호출 시 엔티티 내부에서 role을 ROLE_USER로 설정
        Member member = Member.createMember(
                joinDto.getUsername(),
                passwordEncoder.encode(joinDto.getPassword()),
                joinDto.getEmail(),
                joinDto.getPhone(),
                Role.ROLE_USER
        );
        memberRepository.save(member);
        return member.getId();
    }

    public void validateDuplicateMember(String username) {
        memberRepository.findByUsername(username)
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원 ID입니다.");
                });
    }

    @Transactional
    // ID 찾기는 이메일과 유저명을 입력받기
    public String userFindId(FindUserIdDto dto) {

        Member member = memberRepository.findByEmailAndPhone(dto.getEmail(), dto.getPhone())
                .orElseThrow(() -> new NotSignUpUserException("아이디 찾기는 회원 가입 후 진행할 수 있습니다."));

        return member.getUsername();
    }

    // 비밀번호 찾기
    @Transactional
    public void findPassword(FindUserPasswordDto dto) {

        Optional<Member> memberOpt = Optional.empty();

        if (dto.getEmail() != null) {
            memberOpt = memberRepository.findByUsernameAndEmail(dto.getUsername(), dto.getEmail());
        } else if (dto.getPhone() != null) {
            memberOpt = memberRepository.findByUsernameAndPhone(dto.getUsername(), dto.getPhone());
        }

        Member member = memberOpt.orElseThrow(
                () -> new NotSignUpUserException("입력하신 정보로 가입된 계정을 찾을 수 없습니다.")
        );

        changePassword(member, dto.getNewPassword());
    }

    private void changePassword(Member member, String password) {
        member.changePassword( passwordEncoder.encode(password));
    }

    @Transactional
    public void updateUserInfo(String username, UpdateUserInfoDto dto){
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NotSignUpUserException("입력하신 정보로 가입된 계정을 찾을 수 없습니다."));
        member.changeUserInfo(dto.getEmail(),dto.getPhone());

    }
    @Transactional(readOnly = true)
    public GetUserInfoDto getUserInfo(String username){
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NotSignUpUserException("입력하신 정보로 가입된 계정을 찾을 수 없습니다."));

        return new GetUserInfoDto(member);

    }

    @Transactional(readOnly = true)
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
    }
}