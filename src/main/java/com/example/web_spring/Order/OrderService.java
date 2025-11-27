package com.example.web_spring.Order;

import com.example.web_spring.Cart.Cart;
import com.example.web_spring.Cart.CartService;
import com.example.web_spring.Delivery.Delivery;
import com.example.web_spring.Delivery.DeliveryState;
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

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        TemporaryOrder temp = temporaryOrderRepository.findByMember(member)
                .orElseThrow(() -> new IllegalArgumentException("임시 주문 정보가 없습니다."));

        List<Cart> cartItems = cartService.getCartItems(username);

        int totalPrice = cartService.getTotalPrice(cartItems);

        // 🔥 주문 생성
        Order order = Order.create(member,temp,totalPrice, method);

        // 🔥 주문 상품(OrderItem) 추가
        for (Cart cart : cartItems) {
            OrderItem item = OrderItem.create(
                    cart.getProduct(),
                    cart.getQuantity(),
                    cart.getPrice()
            );
            order.addOrderItem(item);
        }

        // 🔥 배송정보 생성 & 주문에 연결
        Delivery delivery = Delivery.create(order, temp.getAddress());
        order.setDelivery(delivery);

        // 저장
        orderRepository.save(order);

        // 장바구니 비우기
        cartService.clearCart(username);

        // 임시 주문 삭제
        temporaryOrderRepository.delete(temp);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
    }

    public List<Order> findOrdersByUsername(String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        return orderRepository.findByMemberOrderByIdDesc(member);
    }

    @Transactional
    public void cancelOrder(Long orderId, String username) {

        Order order = findOrderById(orderId);

        if (!order.getMember().getUsername().equals(username)) {
            throw new IllegalArgumentException("본인의 주문만 취소할 수 있습니다.");
        }

        if (order.getStatus() == OrderStatus.SHIPPING ||
                order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("배송 중이거나 배송 완료된 주문은 취소할 수 없습니다.");
        }

        order.setStatus(OrderStatus.CANCELED);
    }

    @Transactional
    public void refundOrder(Long orderId, String username) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));

        if (!order.getMember().getUsername().equals(username)) {
            throw new IllegalStateException("본인의 주문만 반품할 수 있습니다.");
        }

        // 배송완료 상태에서만 반품 가능
        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new IllegalStateException("배송완료 상태에서만 반품 요청이 가능합니다.");
        }

        // 배송 상태 변경
        order.getDelivery().setState(DeliveryState.RETURN_REQUESTED);

        // 주문 상태 변경
        order.setStatus(OrderStatus.REFUNDED);
    }


}
