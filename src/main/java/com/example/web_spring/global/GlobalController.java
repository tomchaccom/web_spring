package com.example.web_spring.global;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class GlobalController {


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

}
