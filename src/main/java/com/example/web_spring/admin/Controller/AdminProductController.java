package com.example.web_spring.admin.Controller;

import com.example.web_spring.Product.Product;
import com.example.web_spring.Product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    // 상품 수정 페이지
    @GetMapping("/products/{id}/edit")
    public String editProduct(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        model.addAttribute("product", product);
        return "admin/product/product_edit";
    }

    @PostMapping("/products/{id}/edit")
    public String updateProduct(@PathVariable Long id,
                                @RequestParam String name,
                                @RequestParam int price,
                                @RequestParam String description,
                                @RequestParam(required = false) String imageUrl) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        // 엔티티 메소드 사용
        product.updateProduct(name, price, description);

        if (imageUrl != null && !imageUrl.isBlank()) {
            product.changeImage(imageUrl);
        }

        productRepository.save(product);

        return "redirect:/admin/products";
    }


    // 재고 관리 페이지
    @GetMapping("/products/{id}/stock")
    public String manageStock(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        model.addAttribute("product", product);
        return "admin/product/product_stock";
    }

    // 재고 증감 처리
    @PostMapping("/products/{id}/stock")
    public String updateStock(@PathVariable Long id,
                              @RequestParam int changeAmount) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        int newStock = product.getStock() + changeAmount;

        product.changeStock(newStock);

        productRepository.save(product);

        return "redirect:/admin/products/" + id + "/stock";
    }


}

