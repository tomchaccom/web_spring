package com.example.web_spring.Order;

import com.example.web_spring.Cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final CartService cartService;
    private final OrderService orderService;

    @GetMapping("/order")
    public String orderForm(Model model, Principal principal) {

        String username = principal.getName();

        model.addAttribute("orderTotal", cartService.getTotalPrice(username));
        model.addAttribute("orderCount", cartService.getTotalQuantity(username));

        return "order/order_form";
    }

    @PostMapping("/order")
    public String submitOrder(OrderFormDto form, Principal principal) {

        String username = principal.getName();

        orderService.saveTemporaryOrder(form, username);

        return "redirect:/payment";
    }
}