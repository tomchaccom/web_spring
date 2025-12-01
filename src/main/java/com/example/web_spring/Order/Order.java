package com.example.web_spring.Order;
import com.example.web_spring.Delivery.Delivery;
import com.example.web_spring.Delivery.DeliveryState;
import com.example.web_spring.Member.Member;
import com.example.web_spring.OrderItem.OrderItem;
import com.example.web_spring.Payment.PaymentMethod;
import com.example.web_spring.Product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 주문자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 총 금액
    private Long totalPrice;

    // 주문 날짜
    private LocalDateTime orderDate;

    // 주문 상태 (결제대기, 결제완료, 배송중 등)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // 배송 정보
    private String receiver;
    private String phoneNumber;
    private String address;

    private Long usedCouponId;    // 사용한 쿠폰 ID
    private int usedPoints;       // 사용한 적립금
    private int earnedPoints;     // 적립된 적립금 (결제금액의 1%)


    // 결제 수단
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    // 주문 상품 목록
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Delivery delivery;

    @PrePersist
    public void prePersist() {
        this.orderDate = LocalDateTime.now();
        if (this.status == null) {
            this.status = OrderStatus.PAYMENT_PENDING;
        }
    }
    public Order(Member member,
                 String receiver,
                 String phoneNumber,
                 String address,
                 Long totalPrice,
                 PaymentMethod paymentMethod,
                 OrderStatus status) {

        this.member = member;
        this.receiver = receiver;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.orderDate = LocalDateTime.now();
    }


    //== 생성 메서드 ==//
    public static Order create(Member member,
                               TemporaryOrder temp,
                               Long totalPrice,
                               PaymentMethod method) {

        Order order = new Order();
        order.member = member;
        order.receiver = temp.getReceiver();
        order.phoneNumber = temp.getPhoneNumber();
        order.address = temp.getAddress();
        order.totalPrice = totalPrice;
        order.paymentMethod = method;
        order.status = OrderStatus.PAYMENT_COMPLETED; // 결제시 주문 완료
        return order;
    }

    public void addOrderItem(OrderItem item) {
        this.orderItems.add(item);
        item.setOrder(this);
    }

    protected void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public void setUsedCouponId(Long usedCouponId) { this.usedCouponId = usedCouponId; }
    public void setUsedPoints(int usedPoints) { this.usedPoints = usedPoints; }
    public void setEarnedPoints(int earnedPoints) { this.earnedPoints = earnedPoints; }

    public Long getUsedCouponId() { return usedCouponId; }
    public int getUsedPoints() { return usedPoints; }
    public int getEarnedPoints() { return earnedPoints; }

    public void changeStatus(OrderStatus status) {
        this.status = status;
    }

    public void changeDeliveryState(DeliveryState state) {
        if (this.delivery != null) {
            this.delivery.setState(state);
        }
    }

}