package com.example.web_spring.Cart;

import com.example.web_spring.Member.Member;
import com.example.web_spring.Member.MemberRepository;
import com.example.web_spring.Product.Product;
import com.example.web_spring.Product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    /** 장바구니 조회 */
    public List<Cart> getCartItems(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        return cartRepository.findByMember(member);
    }

    /** 총 금액 */
    public Long getTotalPrice(List<Cart> cartList) {
        return cartList.stream()
                .mapToLong(c -> c.getPrice() * c.getQuantity())
                .sum();
    }

    /** 장바구니 추가 */
    @Transactional
    public void addToCart(Long productId, String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        // 1) 재고 검사 — 최초 담기
        if (product.getStock() < 1) {
            throw new IllegalStateException("재고가 부족하여 장바구니에 담을 수 없습니다.");
        }

        cartRepository.findByMemberAndProduct(member, product)
                .ifPresentOrElse(
                        cart -> {
                            // 2) 이미 장바구니에 있을 경우 → 증가 시 검사
                            if (product.getStock() < cart.getQuantity() + 1) {
                                throw new IllegalStateException("재고보다 많은 수량을 담을 수 없습니다.");
                            }
                            cart.addQuantity();
                        },
                        () -> cartRepository.save(new Cart(member, product, 1, product.getPrice()))
                );
    }


    @Transactional
    public void increaseQuantity(Long productId, String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        Cart cart = cartRepository.findByMemberAndProduct(member, product)
                .orElseThrow(() -> new IllegalArgumentException("장바구니에 해당 상품이 없습니다."));

        // 재고 검사
        if (product.getStock() <= cart.getQuantity()) {
            throw new IllegalStateException("재고보다 많은 수량은 담을 수 없습니다.");
        }

        cart.addQuantity();
    }


    /** 수량 감소 */
    @Transactional
    public void decreaseQuantity(Long productId, String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        Cart cart = cartRepository.findByMemberAndProduct(member, product)
                .orElseThrow(() -> new IllegalArgumentException("장바구니에 해당 상품이 없습니다."));

        cart.removeQuantity();
    }

    /** 삭제 */
    @Transactional
    public void deleteItem(Long productId, String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        cartRepository.deleteByMemberAndProduct(member, product);
    }

    /** 총 금액(username 기반) */
    public Long getTotalPrice(String username) {
        List<Cart> cartList = getCartItems(username);
        return cartList.stream()
                .mapToLong(c -> c.getPrice() * c.getQuantity())
                .sum();
    }

    /** 총 수량(username 기반) */
    public Long getTotalQuantity(String username) {
        List<Cart> cartList = getCartItems(username);
        return cartList.stream()
                .mapToLong(Cart::getQuantity)
                .sum();
    }
    @Transactional
    public void clearCart(String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        List<Cart> cartItems = cartRepository.findByMember(member);

        cartRepository.deleteAll(cartItems);
    }
}