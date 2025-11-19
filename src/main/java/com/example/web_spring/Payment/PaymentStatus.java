package com.example.web_spring.Payment;

// 결제 상태 Enum
enum PaymentStatus {
    PENDING, // 결제 대기
    COMPLETED, // 결제완료 [cite: 66]
    CANCELED // 결제 취소
}