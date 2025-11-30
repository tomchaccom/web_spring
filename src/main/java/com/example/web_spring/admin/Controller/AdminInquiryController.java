package com.example.web_spring.admin.Controller;


import com.example.web_spring.admin.Service.InquiryAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/cs/inquiries")
public class AdminInquiryController {

    private final InquiryAdminService inquiryService;

    /** 문의 목록 */
    @GetMapping
    public String inquiryList(Model model) {
        model.addAttribute("inquiries", inquiryService.findAll());
        return "admin/cs/inquiry_manage";
    }

    /** 문의 상세 보기 */
    @GetMapping("/{id}")
    public String inquiryDetail(@PathVariable Long id, Model model) {
        model.addAttribute("inquiry", inquiryService.findById(id));
        return "admin/cs/inquiry_detail";
    }

    /** 관리자 답변 등록 */
    @PostMapping("/{id}/answer")
    public String submitAnswer(@PathVariable Long id,
                               @RequestParam String answer) {

        inquiryService.answerInquiry(id, answer);

        return "redirect:/admin/cs/inquiries/" + id;
    }
}

