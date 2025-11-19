package com.example.web_spring.Delivery;

// 배송 상태 Enum
enum DeliveryState {
    PENDING, // 배송 대기
    PREPARING, // 상품준비
    SHIPPING, // 배송증
    COMPLETED // 배송완료
}
