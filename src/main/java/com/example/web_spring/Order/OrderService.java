package com.example.web_spring.Order;

import com.example.web_spring.Cart.Cart;
import com.example.web_spring.Cart.CartService;
import com.example.web_spring.Member.Member;
import com.example.web_spring.Member.MemberRepository;
import com.example.web_spring.OrderItem.OrderItem;
import com.example.web_spring.Payment.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final TemporaryOrderRepository temporaryOrderRepository;
    private final MemberRepository memberRepository;
    private final CartService cartService;
    private final OrderRepository orderRepository;

    @Transactional
    public void saveTemporaryOrder(OrderFormDto form, String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보 없음"));

        TemporaryOrder temp = TemporaryOrder.create(member, form);

        temporaryOrderRepository.save(temp);
    }

    @Transactional(readOnly = true)
    public TemporaryOrder getTemporaryOrder(String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        return temporaryOrderRepository.findTopByMemberOrderByIdDesc(member)
                .orElseThrow(() -> new IllegalStateException("임시 주문 정보가 없습니다."));
    }
    @Transactional
    public Long completeOrder(String username, PaymentMethod method) {

        // 1) 회원 조회
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        // 2) 임시 주문 조회 (배송지 정보)
        TemporaryOrder temp = temporaryOrderRepository.findTopByMemberOrderByIdDesc(member)
                .orElseThrow(() -> new IllegalStateException("임시 주문 정보가 없습니다."));

        // 3) 장바구니 목록 조회
        List<Cart> cartItems = cartService.getCartItems(username);

        // 4) 총 금액 계산
        int totalPrice = cartItems.stream()
                .mapToInt(item -> item.getPrice() * item.getQuantity())
                .sum();

        // 5) 본 주문(Order) 생성
        Order order = Order.create(member, temp, totalPrice, method);

        // 6) 각 장바구니 상품을 OrderItem으로 변환
        for (Cart cart : cartItems) {

            OrderItem item = OrderItem.create(
                    cart.getProduct(),
                    cart.getQuantity(),
                    cart.getPrice()
            );

            order.addOrderItem(item); // order와 item의 양방향 설정
        }

        // 7) DB 저장
        orderRepository.save(order);

        // 8) 장바구니 비우기
        cartService.clearCart(username);

        // 9) 임시 주문 삭제
        temporaryOrderRepository.delete(temp);

        return order.getId();
    }

}
