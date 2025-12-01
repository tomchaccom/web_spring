package com.example.web_spring.Order;

import com.example.web_spring.Cart.Cart;
import com.example.web_spring.Cart.CartService;
import com.example.web_spring.Coupon.Coupon;
import com.example.web_spring.Coupon.CouponRepository;
import com.example.web_spring.Delivery.Delivery;
import com.example.web_spring.Delivery.DeliveryState;
import com.example.web_spring.Member.Member;
import com.example.web_spring.Member.MemberRepository;
import com.example.web_spring.OrderItem.OrderItem;
import com.example.web_spring.Payment.PaymentMethod;
import com.example.web_spring.Product.Product;
import com.example.web_spring.Product.ProductRepository;
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
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public void saveTemporaryOrder(OrderFormDto form, String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보 없음"));

        temporaryOrderRepository.deleteByMember(member);
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
    public Long completeOrder(String username, PaymentMethod method,
                              Long couponId, int usedPoints) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        TemporaryOrder temp = temporaryOrderRepository.findByMember(member)
                .orElseThrow(() -> new IllegalArgumentException("임시 주문 정보가 없습니다."));

        List<Cart> cartItems = cartService.getCartItems(username);
        long totalPrice = cartService.getTotalPrice(cartItems);

        Coupon usedCoupon = null;

        if (couponId != null && couponId > 0) {
            usedCoupon = couponRepository.findById(couponId)
                    .orElseThrow(() -> new IllegalArgumentException("쿠폰 없음"));

            if (!usedCoupon.isUsed()) {
                totalPrice -= usedCoupon.getDiscountAmount();
                usedCoupon.setUsed(true);
            }
        }

        if (usedPoints > 0) {
            if (member.getPoints() < usedPoints) {
                throw new IllegalStateException("적립금 부족");
            }

            member.setPoints(member.getPoints() - usedPoints);
            totalPrice -= usedPoints;
        }

        if (totalPrice < 0) totalPrice = 0;

        int earnedPoints = (int) (totalPrice * 0.01);

        Order order = Order.create(member, temp, totalPrice, method);

        order.setUsedCouponId(usedCoupon != null ? usedCoupon.getId() : null);
        order.setUsedPoints(usedPoints);
        order.setEarnedPoints(earnedPoints);

        for (Cart cart : cartItems) {
            Product product = cart.getProduct();
            product.reduceStock(cart.getQuantity());

            OrderItem item = OrderItem.create(
                    product,
                    cart.getQuantity(),
                    product.getPrice() * cart.getQuantity()
            );
            order.addOrderItem(item);
        }

        Delivery delivery = Delivery.create(order, temp.getAddress());
        delivery.setState(DeliveryState.READY);
        order.setDelivery(delivery);

        cartService.clearCart(username);
        orderRepository.save(order);
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

        DeliveryState deliveryState = order.getDelivery().getState();

        if (deliveryState == DeliveryState.SHIPPING ||
                deliveryState == DeliveryState.DELIVERED) {
            throw new IllegalStateException("배송 중이거나 배송 완료된 주문은 취소할 수 없습니다.");
        }

        Member member = order.getMember();

        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.increaseStock(item.getQuantity());  // stock += quantity
        }

        if (order.getUsedCouponId() != null) {
            Coupon coupon = couponRepository.findById(order.getUsedCouponId())
                    .orElseThrow(() -> new IllegalArgumentException("쿠폰 존재하지 않음"));
            coupon.setUsed(false); // 다시 사용 가능!
        }

        if (order.getUsedPoints() > 0) {
            member.setPoints(member.getPoints() + order.getUsedPoints());
        }

        if (order.getEarnedPoints() > 0) {
            member.setPoints(member.getPoints() - order.getEarnedPoints());
            if (member.getPoints() < 0) member.setPoints(0); // 방어 코드
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

        if (order.getDelivery().getState() != DeliveryState.DELIVERED) {
            throw new IllegalStateException("배송완료 상태에서만 반품 요청이 가능합니다.");
        }


        Member member = order.getMember();


        order.getDelivery().setState(DeliveryState.RETURN_REQUESTED);
        order.setStatus(OrderStatus.REFUNDED);

        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.increaseStock(item.getQuantity());    // 재고 증가
        }

        if (order.getUsedCouponId() != null) {
            Coupon coupon = couponRepository.findById(order.getUsedCouponId())
                    .orElseThrow(() -> new IllegalArgumentException("쿠폰 없음"));
            coupon.setUsed(false);
        }

        if (order.getUsedPoints() > 0) {
            member.setPoints(member.getPoints() + order.getUsedPoints());
        }

        if (order.getEarnedPoints() > 0) {
            member.setPoints(member.getPoints() - order.getEarnedPoints());
            if (member.getPoints() < 0) member.setPoints(0);
        }

    }

    @Transactional
    public void saveSingleProductTemporaryOrder(Long productId, int quantity, String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        TemporaryOrder temp = TemporaryOrder.createForSingleProduct(member, productId, quantity);

        temporaryOrderRepository.save(temp);
    }

    @Transactional
    public void confirmDelivery(Long orderId, String username) {

        Order order = findOrderById(orderId);

        if (!order.getMember().getUsername().equals(username)) {
            throw new IllegalArgumentException("본인의 주문만 확정할 수 없습니다.");
        }

        if (order.getStatus() == OrderStatus.DELIVERED) return;


        order.setStatus(OrderStatus.DELIVERED);
        order.getDelivery().setState(DeliveryState.DELIVERED);

        int earnedPoints = (int) (order.getTotalPrice() * 0.01);
        order.setEarnedPoints(earnedPoints);

        Member member = order.getMember();
        member.setPoints(member.getPoints() + earnedPoints);
    }
}
