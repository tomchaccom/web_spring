package com.example.web_spring.global;

import com.example.web_spring.Member.Dto.GetUserInfoDto;
import com.example.web_spring.Member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class GlobalController {

    private final MemberService memberService;


    @GetMapping("/")
    public String home(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("userName", principal.getName());
        }
        return "main"; // 메인 뷰
    }

    @GetMapping("/info")
    public String info(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("userName", principal.getName());
        }
        return "main/info";
    }
    @GetMapping("/support")
    public String support(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("userName", principal.getName());
        }
        return "main/support";
    }

    @GetMapping("/mypage")
    public String mypage(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("userName", principal.getName());
        }
        GetUserInfoDto dto = memberService.getUserInfo(principal.getName());

        model.addAttribute("username", dto.getUsername());
        model.addAttribute("email", dto.getEmail());
        model.addAttribute("phone", dto.getPhone());

        return "main/mypage";
    }

    @GetMapping("/mypage/edit")
    public String edit(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("userName", principal.getName());
        }

        return "main/mypage-edit";
    }
}
