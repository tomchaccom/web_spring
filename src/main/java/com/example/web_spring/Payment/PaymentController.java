package com.example.web_spring.Payment;

import com.example.web_spring.Cart.CartService;
import com.example.web_spring.Coupon.CouponRepository;
import com.example.web_spring.Member.Member;
import com.example.web_spring.Member.MemberService;
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
    private final MemberService memberService;
    private final CouponRepository couponRepository;

    @GetMapping("/payment")
    public String paymentPage(Model model, Principal principal) {

        String username = principal.getName();
        Member member = memberService.findByUsername(username);

        model.addAttribute("orderTotal", cartService.getTotalPrice(username));
        model.addAttribute("orderCount", cartService.getTotalQuantity(username));

        TemporaryOrder tempOrder = orderService.getTemporaryOrder(username);
        model.addAttribute("tempOrder", tempOrder);

        model.addAttribute("coupons", couponRepository.findByMemberAndUsedFalse(member));
        model.addAttribute("member", member);

        return "payment/payment";
    }

    @PostMapping("/payment")
    public String pay(@RequestParam String paymentMethod,
                      @RequestParam(required = false) Long couponId,
                      @RequestParam(defaultValue = "0") int usedPoints,
                      Principal principal) {

        String username = principal.getName();

        Long orderId = orderService.completeOrder(
                username,
                PaymentMethod.valueOf(paymentMethod),
                couponId,
                usedPoints
        );

        return "redirect:/order/complete/" + orderId;
    }

}
