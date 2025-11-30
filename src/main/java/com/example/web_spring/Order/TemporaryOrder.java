package com.example.web_spring.Order;

import com.example.web_spring.Member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TemporaryOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String receiver;
    private String phoneNumber;
    private String address;

    // ⭐ 단일 상품 주문 시 필요한 필드
    private Long productId;
    private int quantity;

    // 🔥 단일 상품 주문을 위한 필드 추가
    private Long singleProductId;  // 상품 ID
    private Integer singleQuantity; //

    public static TemporaryOrder create(Member member, OrderFormDto dto) {
        TemporaryOrder order = new TemporaryOrder();
        order.member = member;
        order.receiver = dto.getReceiver();
        order.phoneNumber = dto.getPhoneNumber();
        order.address = dto.getAddress();
        order.productId = dto.getProductId();
        order.quantity = dto.getQuantity();
        return order;
    }
    // 단일 상품 주문용 생성 메서드
    public static TemporaryOrder createForSingleProduct(Member member, Long productId, int quantity) {
        TemporaryOrder order = new TemporaryOrder();
        order.member = member;
        order.singleProductId = productId;
        order.singleQuantity = quantity;
        return order;
    }

}
