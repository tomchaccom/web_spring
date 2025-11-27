package com.example.web_spring.Delivery;

import com.example.web_spring.Order.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
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

    public void setOrder(Order order) {
        this.order = order;
    }

    public static Delivery create(Order order, String address) {
        Delivery delivery = new Delivery();
        delivery.order = order;
        delivery.address = address;
        delivery.trackingNo = "준비중";
        delivery.state = DeliveryState.READY;
        return delivery;
    }

    /** 비즈니스 생성자 */
    public Delivery(Order order, String trackingNo, String address, DeliveryState state) {
        this.order = order;
        this.trackingNo = trackingNo;
        this.address = address;
        this.state = state;
    }
}

