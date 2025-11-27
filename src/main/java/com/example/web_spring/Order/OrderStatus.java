package com.example.web_spring.Order;

public enum OrderStatus {
    PAYMENT_PENDING, // 결제전
    PAYMENT_COMPLETED, // 결제완료
    PREPARING_SHIPMENT, // 상품준비
    SHIPPING, // 배송증
    DELIVERED, // 배송완료
    CANCELED, // 주문취소
    REFUNDED // 환불
}
