package com.example.web_spring.Payment;

import com.example.web_spring.Cart.CartService;
import com.example.web_spring.Order.OrderService;
import com.example.web_spring.Order.TemporaryOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final CartService cartService;
    private final OrderService orderService;

    @GetMapping("/payment")
    public String paymentPage(Model model, Principal principal) {

        String username = principal.getName();

        model.addAttribute("orderTotal", cartService.getTotalPrice(username));
        model.addAttribute("orderCount", cartService.getTotalQuantity(username));

        TemporaryOrder tempOrder = orderService.getTemporaryOrder(username);
        model.addAttribute("tempOrder", tempOrder);

        return "payment/payment";
    }

    @PostMapping("/payment")
    public String pay(@RequestParam String paymentMethod,
                      Principal principal) {

        String username = principal.getName();

        Long orderId = orderService.completeOrder(username, PaymentMethod.valueOf(paymentMethod));

        return "redirect:/order/complete/" + orderId;
    }
}
