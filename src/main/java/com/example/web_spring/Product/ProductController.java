package com.example.web_spring.Product;

import com.example.web_spring.Review.Review;
import com.example.web_spring.Review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;
    private final ReviewService reviewService;

    @GetMapping("/products")
    public String productList(
            Model model,
            @RequestParam(required = false) String query,
            Principal principal) {

        if (principal != null) {
            model.addAttribute("userName", principal.getName());
        }


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
    public String getProductDetail(@PathVariable Long id, Model model, Principal principal) {

        if (principal != null) {
            model.addAttribute("userName", principal.getName());
        }

        String currentUser = principal != null ? principal.getName() : null;
        Product product = productService.getProductById(id);
        List<Review> reviews = reviewService.getReviewsByProduct(id);

        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        model.addAttribute("currentUser", currentUser);

        return "products/detail";
    }


}
