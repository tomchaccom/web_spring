package com.example.web_spring.Product;

import com.example.web_spring.global.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping("/products")
    public String productList(Model model) {

        List<Product> products = productRepository.findAll();

        model.addAttribute("products", products);

        return "main/product"; //
    }

}
