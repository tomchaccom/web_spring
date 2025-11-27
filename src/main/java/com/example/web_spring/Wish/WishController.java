package com.example.web_spring.Wish;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WishController {

    private final WishService wishService;

    @PostMapping("/products/wish")
    public String addWish(@RequestParam Long id, Principal principal, Model model)  {
        if (principal != null) {
            model.addAttribute("userName", principal.getName());
        }

        wishService.addWishList(id, principal.getName());


        return "redirect:/wish";
    }

    @GetMapping("/wish")
    public String wishList(Model model, Principal principal) {

        if (principal != null) {
            model.addAttribute("userName", principal.getName());
        }

        // 로그인 사용자 위시리스트 조회
        List<Wish> wishList = wishService.getWishList(principal.getName());
        model.addAttribute("wishList", wishList);

        return "products/wish-list"; // 생성 예정 템플릿
    }
}

