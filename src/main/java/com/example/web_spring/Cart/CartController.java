package com.example.web_spring.Cart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /** 장바구니 조회 */
    @GetMapping("/cart")
    public String cartPage(Model model, Principal principal) {

        if (principal != null) {
            model.addAttribute("userName", principal.getName());
        }

        var items = cartService.getCartItems(principal.getName());

        boolean stockError = items.stream()
                .anyMatch(c -> c.getProduct().getStock() < c.getQuantity());
        model.addAttribute("stockError", stockError);

        model.addAttribute("cartItems", items);
        model.addAttribute("totalPrice", cartService.getTotalPrice(items));


        return "cart/cart";
    }

    /** 장바구니 추가 */
    @PostMapping("/cart/add/{productId}")
    public String add(@PathVariable Long productId, Principal principal) {
        cartService.addToCart(productId, principal.getName());
        return "redirect:/cart";
    }

    /** 수량 증가 */
    @PostMapping("/cart/plus/{productId}")
    public String plus(@PathVariable Long productId, Principal principal, RedirectAttributes rttr) {
        try {
            cartService.increaseQuantity(productId, principal.getName());
            return "redirect:/cart";

        } catch (IllegalStateException e) {
            rttr.addFlashAttribute("stockError", e.getMessage());
            return "redirect:/cart";
        }
    }


    /** 수량 감소 */
    @PostMapping("/cart/minus/{productId}")
    public String minus(@PathVariable Long productId, Principal principal) {
        cartService.decreaseQuantity(productId, principal.getName());
        return "redirect:/cart";
    }

    /** 삭제 */
    @PostMapping("/cart/delete/{productId}")
    public String delete(@PathVariable Long productId, Principal principal) {
        cartService.deleteItem(productId, principal.getName());
        return "redirect:/cart";
    }
}