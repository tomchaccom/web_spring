package com.example.web_spring.Member;

import com.example.web_spring.Member.Dto.FindUserIdDto;
import com.example.web_spring.Member.Dto.FindUserPasswordDto;
import com.example.web_spring.Member.Dto.MemberJoinDto;
import com.example.web_spring.Member.Dto.UpdateUserInfoDto;
import com.example.web_spring.global.NotSignUpUserException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String joinForm(Model model) {
        // 뷰에서 사용할 빈 DTO 객체를 모델에 담아 전달
        model.addAttribute("memberJoinDto", new MemberJoinDto());
        return "join"; //
    }

    @PostMapping("/join")
    public String join(MemberJoinDto memberJoinDto) {
        try {
            // Service 계층을 호출하여 비즈니스 로직 수행
            memberService.join(memberJoinDto);
        } catch (IllegalStateException e) {
            // ID 중복 등 비즈니스 예외 발생 시, 다시 폼으로 돌아가 에러 메시지를 표시할 수 있음.
            // 실제로는 RedirectAttributes를 사용하여 메시지를 전달하는 것이 좋습니다.
            // 임시로 로그를 남기고 리다이렉트
            System.err.println("회원가입 실패: " + e.getMessage());
            return "redirect:/join";
        }
        return "redirect:/join/success";
    }

    @GetMapping("/join/success")
    public String joinSuccess() {
        return "joinSuccess"; //
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/join/dup")
    public ResponseEntity<?> duplicateForm(
            @RequestParam String username) {
        try{
            memberService.validateDuplicateMember(username);
            return ResponseEntity.ok().build();
        }catch (IllegalStateException e){
            return ResponseEntity.badRequest().build();
        }
    }
    // 아이디 비밀번호 찾기 뷰 제공
    @GetMapping("/find")
    public String findForm() {
        return "find/find";
    }

    // 아이디 찾기
    @PostMapping("/find/id")
    public String findId(
            FindUserIdDto dto,
            RedirectAttributes ra
    ) {
        try {
            String userName = memberService.userFindId(dto);
            ra.addFlashAttribute("userName", userName);
            return "redirect:/find";
        } catch (NotSignUpUserException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/find";
        }
    }

    @PostMapping("/find/password")
    public String findPasswordAndChangePassword(
            FindUserPasswordDto dto,
            RedirectAttributes ra
    ) {
        try {
            memberService.findPassword(dto);
            ra.addFlashAttribute("successMessage", "비밀번호가 재설정 되었습니다..");
            return "redirect:/login";
        } catch (NotSignUpUserException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/find";
        }
    }

    @PostMapping("/mypage/edit")
    public String updateUserInfo(
            Principal principal,
            UpdateUserInfoDto dto

    ){
        memberService.updateUserInfo(principal.getName(), dto);
        return "redirect:/mypage";
    }

}