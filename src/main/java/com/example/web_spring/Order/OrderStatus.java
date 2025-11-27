package com.example.web_spring.Order;

public enum OrderStatus {
    PAYMENT_PENDING,       // 결제 대기
    PAYMENT_COMPLETED,     // 결제 완료
    PREPARING_SHIPMENT,    // 상품 준비 중
    SHIPPING,              // 배송 중
    DELIVERED,             // 배송 완료
    CANCELED,              // 주문 취소
    REFUNDED;              // 환불 완료

    public String getKorean() {
        return switch (this) {
            case PAYMENT_PENDING -> "결제 대기";
            case PAYMENT_COMPLETED -> "결제 완료";
            case PREPARING_SHIPMENT -> "상품 준비 중";
            case SHIPPING -> "배송 중";
            case DELIVERED -> "배송 완료";
            case CANCELED -> "주문 취소";
            case REFUNDED -> "환불 완료";
        };
    }

    public String getBadgeClass() {
        return switch (this) {
            case PAYMENT_PENDING -> "secondary";
            case PAYMENT_COMPLETED -> "success";
            case PREPARING_SHIPMENT -> "info";
            case SHIPPING -> "primary";
            case DELIVERED -> "dark";
            case CANCELED -> "danger";
            case REFUNDED -> "warning";
        };
    }
}
