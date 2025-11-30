package com.example.web_spring.Inquiry;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;

    /** 문의 작성 폼 페이지 */
    @GetMapping("/write")
    public String writeForm() {
        return "inquiry/write"; // inquiry/write.html 반환
    }

    /** 문의 저장 */
    @PostMapping("/write")
    public String writeInquiry(@RequestParam String title,
                               @RequestParam String content,
                               @RequestParam(required = false) MultipartFile imageFile,
                               @AuthenticationPrincipal(expression = "username") String username) {

        inquiryService.writeInquiry(username, title, content, imageFile);
        return "redirect:/mypage/inquiries"; // 작성 후 마이페이지로 이동
    }

    /** 문의 삭제 */
    @PostMapping("/{id}/delete")
    public String deleteInquiry(@PathVariable Long id,
                                @AuthenticationPrincipal(expression = "username") String username) {

        inquiryService.deleteInquiry(id, username);
        return "redirect:/mypage/inquiries";  // 또는 마이페이지로 이동
    }


}


