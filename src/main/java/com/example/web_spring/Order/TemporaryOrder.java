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

    public static TemporaryOrder create(Member member, OrderFormDto dto) {
        TemporaryOrder order = new TemporaryOrder();
        order.member = member;
        order.receiver = dto.getReceiver();
        order.phoneNumber = dto.getPhoneNumber();
        order.address = dto.getAddress();
        return order;
    }
}