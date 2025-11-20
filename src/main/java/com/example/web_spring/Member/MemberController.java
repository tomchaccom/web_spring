package com.example.web_spring.Member;

import com.example.web_spring.Member.Dto.MemberJoinDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import lombok.RequiredArgsConstructor;

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
}