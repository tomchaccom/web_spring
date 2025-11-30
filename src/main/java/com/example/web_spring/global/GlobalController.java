package com.example.web_spring.global;

import com.example.web_spring.Inquiry.Inquiry;
import com.example.web_spring.Inquiry.InquiryService;
import com.example.web_spring.Member.Dto.GetUserInfoDto;
import com.example.web_spring.Member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class GlobalController {

    private final MemberService memberService;
    private final InquiryService inquiryService;


    @GetMapping("/")
    public String home(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("userName", principal.getName());
        }

        List<ProductDto> dummyProducts = List.of(
                new ProductDto(1L, "아이폰 15", 1200000, "images/iphone.png"),
                new ProductDto(2L, "맥북 프로", 3200000, "/images/macbook.jpeg"),
                new ProductDto(3L, "갤럭시 S24", 1100000, "/images/galaxy.jpeg"),
                new ProductDto(4L, "LG 모니터", 450000, "/images/monitor.jpg")
        );

        model.addAttribute("products", dummyProducts);

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


        List<Inquiry> inquiries = inquiryService.getMyInquiries(principal.getName());
        model.addAttribute("inquiries", inquiries);

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
    @GetMapping("/mypage/inquiries")
    public String myInquiries(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("userName", principal.getName());
        }

        List<Inquiry> inquiries = inquiryService.getMyInquiries(principal.getName());
        model.addAttribute("inquiries", inquiries);

        return "mypage/inquiries";
    }
}
