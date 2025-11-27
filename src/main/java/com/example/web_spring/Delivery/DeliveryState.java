package com.example.web_spring.Delivery;

import lombok.Getter;

@Getter
public enum DeliveryState {

    READY("배송준비중", "secondary"),
    SHIPPING("배송중", "info"),
    DELIVERED("배송완료", "success"),
    RETURN_REQUESTED("반품요청", "warning"),
    RETURNED("반품완료", "dark");

    private final String korean;
    private final String badgeClass;

    DeliveryState(String korean, String badgeClass) {
        this.korean = korean;
        this.badgeClass = badgeClass;
    }
}