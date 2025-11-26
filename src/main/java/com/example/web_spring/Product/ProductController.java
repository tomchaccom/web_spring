package com.example.web_spring.Product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    @GetMapping("/products")
    public String productList(
            Model model,
            @RequestParam(required = false) String query) {

        if(query == null){
            List<Product> products = productRepository.findAll();
            model.addAttribute("products", products);
        }else{
            List<Product> products = productRepository.findByCategoryName(query);
            model.addAttribute("products", products);
        }

        return "main/product"; //
    }
    @GetMapping("/products/{id}")
    public String getProductDetail(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "products/detail";
    }


}
