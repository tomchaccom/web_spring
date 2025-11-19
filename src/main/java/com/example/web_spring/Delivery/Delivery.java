package com.example.web_spring.Delivery;

import com.example.web_spring.Order.Order;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private String trackingNo; // 송장 번호
    private String address;

    @Enumerated(EnumType.STRING)
    private DeliveryState state;
}

