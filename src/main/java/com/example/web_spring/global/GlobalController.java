package com.example.web_spring.global;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GlobalController {

    @GetMapping("/")
    public String index() {
        return "main";
    }
}
