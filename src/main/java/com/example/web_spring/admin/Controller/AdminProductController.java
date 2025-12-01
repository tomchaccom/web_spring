package com.example.web_spring.admin.Controller;

import com.example.web_spring.Category.Category;
import com.example.web_spring.Category.CategoryRepository;
import com.example.web_spring.Delivery.DeliveryState;
import com.example.web_spring.Member.Member;
import com.example.web_spring.Order.Order;
import com.example.web_spring.Order.OrderRepository;
import com.example.web_spring.Order.OrderStatus;
import com.example.web_spring.OrderItem.OrderItem;
import com.example.web_spring.Product.Product;
import com.example.web_spring.Product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/products")   // ★ 여기서 /admin/products 공통 prefix
public class AdminProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;

    /* ============================
       1. 상품 관리 메인
       ============================ */
    @GetMapping
    public String productMain() {
        // /templates/admin/product/product_manage.html
        return "admin/product/product_manage";
    }

    /* ============================
       2. 상품 목록 / 수정
       ============================ */

    // 상품 목록 (수정 진입용 리스트)
    @GetMapping("/list")
    public String productList(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "admin/product/product_list";
    }

    @GetMapping("/{id}/edit")
    public String editProduct(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());  // 🔥 추가

        return "admin/product/product_edit";
    }


    // 상품 수정 처리
    @PostMapping("/{id}/edit")
    public String updateProduct(@PathVariable Long id,
                                @RequestParam String name,
                                @RequestParam int price,
                                @RequestParam String description,
                                @RequestParam Long categoryId,
                                @RequestParam(required = false) String imageUrl) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));


        // 엔티티 메서드로 필드 변경
        product.updateProduct(name, price, description);
        product.changeCategory(category);

        if (imageUrl != null && !imageUrl.isBlank()) {
            product.changeImage(imageUrl);
        }

        // JPA 영속 상태라 save() 생략해도 되지만 명시적으로
        productRepository.save(product);

        // 수정 후에는 “상품 목록 / 수정” 화면으로
        return "redirect:/admin/products/list";
    }

    /* ============================
       3. 신규 상품 등록
       ============================ */

    // 등록 폼
    @GetMapping("/new")
    public String newProductForm(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/product/product_new";
    }


    @PostMapping("/new")
    public String createProduct(@RequestParam String name,
                                @RequestParam int price,
                                @RequestParam String description,
                                @RequestParam String imageUrl,
                                @RequestParam Long categoryId,
                                @RequestParam int stock) {   // ← 🔥 stock 추가

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        Product product = Product.builder()
                .name(name)
                .price(price)
                .description(description)
                .imageUrl(imageUrl)
                .stock(stock)   // ← 🔥 초기 재고 적용
                .category(category)
                .build();

        productRepository.save(product);

        return "redirect:/admin/products/list";
    }



    /* ============================
       4. 재고 관리
       ============================ */

    // 재고 관리용 상품 목록
    @GetMapping("/stock/list")
    public String stockList(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "admin/product/product_stock_list";
    }

    // 특정 상품 재고 관리 페이지
    @GetMapping("/{id}/stock")
    public String manageStock(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        model.addAttribute("product", product);
        return "admin/product/product_stock";
    }

    // 재고 증감 처리
    @PostMapping("/{id}/stock")
    public String updateStock(@PathVariable Long id,
                              @RequestParam int changeAmount) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        int newStock = product.getStock() + changeAmount;
        product.changeStock(newStock);

        productRepository.save(product);

        // 처리 후 다시 해당 상품 재고 화면으로
        return "redirect:/admin/products/" + id + "/stock";
    }

    // 반품 · 배송 관리 메인 페이지
    @GetMapping("/returns")
    public String manageReturns(Model model) {

        // 배송 중 / 배송 준비중 / 반품 요청된 주문 가져오기
        List<Order> orders = orderRepository.findAll(); // 필요시 상태 필터링해도 됨

        model.addAttribute("orders", orders);

        return "admin/product/product_returns";
    }



// ...

    // 배송 상태 변경 처리
    @Transactional
    @PostMapping("/returns/{orderId}/delivery")
    public String updateDeliveryState(@PathVariable Long orderId,
                                      @RequestParam DeliveryState state) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        order.getDelivery().setState(state);

        // 🔥 명시적으로 저장 (안 해도 영속 상태면 flush 되지만, 이해하기 쉽도록)
        orderRepository.save(order);

        return "redirect:/admin/products/returns";
    }

    // 반품 승인 처리
    @Transactional
    @PostMapping("/returns/{orderId}/approve")
    public String approveReturn(@PathVariable Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        // 상태 변경
        order.changeStatus(OrderStatus.REFUNDED);
        order.changeDeliveryState(DeliveryState.RETURNED);

        // 재고 복원
        for (OrderItem item : order.getOrderItems()) {
            Product p = item.getProduct();
            p.increaseStock(item.getQuantity());
        }

        // 적립금 회수
        Member member = order.getMember();
        member.setPoints(member.getPoints() - order.getEarnedPoints());
        if (member.getPoints() < 0) member.setPoints(0);

        // 🔥 마찬가지로 저장
        orderRepository.save(order);

        return "redirect:/admin/products/returns";
    }



}
