package com.example.web_spring.admin;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class AdminController {

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model, Principal principal) {

        if (principal != null) {
            model.addAttribute("adminName", principal.getName());
        }

        return "admin/dashboard"; // admin/dashboard.html 렌더링
    }
}
