package com.example.web_spring.Order;

import lombok.Data;

@Data
public class OrderFormDto {

    private String receiver;
    private String phoneNumber;
    private String address;

    // 단일 주문에 이용
    private Long productId;
    private int quantity;
}