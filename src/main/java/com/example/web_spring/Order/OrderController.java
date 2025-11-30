package com.example.web_spring.Order;

import com.example.web_spring.Cart.CartService;
import com.example.web_spring.Member.Member;
import com.example.web_spring.Member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final CartService cartService;
    private final OrderService orderService;
    private final MemberService memberService;

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

    @GetMapping("/order/complete/{id}")
    public String orderComplete(@PathVariable Long id, Model model, Principal principal) {

        Order order = orderService.findOrderById(id);

        model.addAttribute("order", order);
        model.addAttribute("userName", principal.getName());

        return "order/order_complete";
    }

    @GetMapping("/mypage/orders")
    public String myOrders(Model model, Principal principal) {

        String username = principal.getName();
        Member member = memberService.findByUsername(username);

        List<Order> orders = orderService.findOrdersByUsername(username);

        model.addAttribute("orders", orders);
        model.addAttribute("userName", member.getUsername());

        return "mypage/order_list";
    }

    @PostMapping("/order/cancel/{id}")
    public String cancelOrder(@PathVariable Long id, Principal principal) {

        orderService.cancelOrder(id, principal.getName());

        return "redirect:/mypage/orders";
    }

    @PostMapping("/order/refund/{id}")
    public String refundOrder(
            @PathVariable Long id,
            Principal principal) {

        String username = principal.getName();
        orderService.refundOrder(id, username);

        return "redirect:/mypage/orders";
    }

    @GetMapping("/order/detail/{id}")
    public String orderDetail(@PathVariable Long id,
                              Principal principal,
                              Model model) {

        String username = principal.getName();
        Order order = orderService.findOrderById(id);

        // 본인 주문인지 검증
        if (!order.getMember().getUsername().equals(username)) {
            throw new IllegalStateException("본인의 주문만 조회할 수 있습니다.");
        }

        model.addAttribute("order", order);
        return "order/order_detail";
    }


}