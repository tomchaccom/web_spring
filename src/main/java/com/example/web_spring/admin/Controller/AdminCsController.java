package com.example.web_spring.admin.Controller;

import com.example.web_spring.Member.Member;
import com.example.web_spring.admin.Service.MemberAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/cs")
@RequiredArgsConstructor
public class AdminCsController {

    private final MemberAdminService memberAdminService;

    /** 회원 목록 */
    @GetMapping("/users")
    public String userManage(Model model) {
        model.addAttribute("members", memberAdminService.getAllMembers());
        return "admin/cs/user_manage";
    }

    /** 회원 상세 보기 */
    @GetMapping("/users/{id}")
    public String userDetail(@PathVariable Long id, Model model) {
        Member member = memberAdminService.getMemberDetail(id);
        model.addAttribute("member", member);
        return "admin/cs/user_detail";
    }
}

