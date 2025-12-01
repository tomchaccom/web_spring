package com.example.web_spring.admin.Controller;

import com.example.web_spring.Product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminProductController {

    private final ProductRepository productRepository;

    // 상품 목록 페이지
    @GetMapping("/products")
    public String manageProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "admin/product/product_manage";
    }
}

